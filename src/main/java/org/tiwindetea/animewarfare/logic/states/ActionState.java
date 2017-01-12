////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.logic.states;

import com.esotericsoftware.minlog.Log;
import javafx.util.Pair;
import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.lomadriel.lfc.statemachine.State;
import org.lomadriel.lfc.statemachine.StateMachine;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.battle.BattleContext;
import org.tiwindetea.animewarfare.logic.battle.PreBattleState;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.states.events.AskMascotToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.NextPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.ActionEvent;
import org.tiwindetea.animewarfare.net.logicevent.CaptureMascotEvent;
import org.tiwindetea.animewarfare.net.logicevent.CaptureMascotEventListener;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEventListener;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MascotToCaptureChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.MascotToCaptureChoiceEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEventListener;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.SkipAllEvent;
import org.tiwindetea.animewarfare.net.logicevent.SkipAllEventListener;
import org.tiwindetea.animewarfare.net.logicevent.StartBattleEvent;
import org.tiwindetea.animewarfare.net.logicevent.StartBattleEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Action phase of the game
 *
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 */
class ActionState extends GameState implements MoveUnitsEventListener, OpenStudioEventListener,
		InvokeUnitEventListener, SkipAllEventListener, StartBattleEventListener,
		CaptureMascotEventListener, MascotToCaptureChoiceEventListener, GameEndedEventListener,
		BattleEventListener, FinishTurnRequestEventListener {
	private static final int MOVE_COST = 1; // TODO: Externalize
	private static final int OPEN_STUDIO_COST = 3; // TODO: Externalize

	private final List<Integer> zonesThatHadABattle = new ArrayList<>();
	private final List<Integer> alreadyMovedUnit = new ArrayList<>();

	private boolean gameEnded;
	private boolean phaseEnded;

	private Player currentPlayer;

	private Player huntedPlayer;
	private Zone huntingZone;

	private StateMachine currentBattleStateMachine = null;

	private boolean nonUlimitedActionDone = false;

	ActionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		registerEventListeners();
		LogicEventDispatcher.getInstance().fire(new PhaseChangedEvent(PhaseChangedEvent.Phase.ACTION));
		this.currentPlayer = this.gameBoard.getFirstPlayer();
	}

	private void registerEventListeners() {
		LogicEventDispatcher.getInstance().addListener(MoveUnitsEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(OpenStudioEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(InvokeUnitEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(SkipAllEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(StartBattleEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(CaptureMascotEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(MascotToCaptureChoiceEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(GameEndedEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(BattleEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(FinishTurnRequestEvent.class, this);
	}

	@Override
	public void update() {
		this.zonesThatHadABattle.clear();
		this.alreadyMovedUnit.clear();
		this.nonUlimitedActionDone = false;

		setNextPlayer();
	}

	private void setNextPlayer() {
		Player localCurrentPlayer = this.currentPlayer;
		do {
			this.currentPlayer = this.currentPlayer.getNextPlayerInGameOrder();
		}
		while (this.currentPlayer.getStaffAvailable() == 0 && localCurrentPlayer != this.currentPlayer); // checking references

		if (this.currentPlayer.getStaffAvailable() == 0) {
			// End the phase.
			this.phaseEnded = true;
		} else {
			LogicEventDispatcher.send(new NextPlayerEvent(this.currentPlayer.getID()));
		}
	}

	@Override
	public void onExit() {
		unregisterEventListeners();
	}

	private void unregisterEventListeners() {
		LogicEventDispatcher.getInstance().removeListener(MoveUnitsEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(OpenStudioEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(InvokeUnitEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(SkipAllEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(StartBattleEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(CaptureMascotEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(MascotToCaptureChoiceEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(GameEndedEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(BattleEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(FinishTurnRequestEvent.class, this);
	}

	@Override
	public State next() {
		if (this.gameEnded) {
			return new GameEndedState(this.gameBoard);
		} else if (this.phaseEnded) {
			return new StaffHiringState(this.gameBoard);
		} else {
			return this;
		}
	}

	private boolean isInvalidPlayer(ActionEvent<?> event) {
		return event.getPlayerID() != this.currentPlayer.getID();
	}

	@Override
	public void handleMoveUnitsEvent(MoveUnitsEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		List<Pair<Unit, MoveUnitsEvent.Movement>> validMovements = new LinkedList<>();

		for (MoveUnitsEvent.Movement movement : event.getMovements()) {
			if (this.gameBoard.getMap().isValid(movement.getSourceZone())
					&& this.gameBoard.getMap().isValid(movement.getDestinationZone())) {

				Unit unitToMove = this.gameBoard.getMap()
						.getZone(movement.getSourceZone())
						.getUnit(movement.getUnitID());

				if (unitToMove != null
						&& unitToMove.hasFaction(this.currentPlayer.getFaction())
						&& !this.alreadyMovedUnit.contains(Integer.valueOf(unitToMove.getID()))) { // A Unit can only be moved once per Action.

					if (GameMap.getDistanceBetween(movement.getSourceZone(),
							movement.getDestinationZone()) <= 1) { // FIXME: replace the 1 by the unit movement capacity.
						validMovements.add(new Pair<>(unitToMove, movement));
					}
				}
			}
		}

		if (this.currentPlayer.hasRequiredStaffPoints(MOVE_COST, validMovements.size())) {
			if (event.getMovements().size() == validMovements.size()) {
				this.currentPlayer.decrementStaffPoints(MOVE_COST, validMovements.size());

				for (Pair<Unit, MoveUnitsEvent.Movement> movement : validMovements) {
					movement.getKey().move(this.gameBoard.getMap().getZone(movement.getValue().getDestinationZone()));
					this.alreadyMovedUnit.add(Integer.valueOf(movement.getKey().getID()));
					this.nonUlimitedActionDone = true;
				}
			}
		}
	}

	@Override
	public void handleOpenStudioEvent(OpenStudioEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(OPEN_STUDIO_COST)) {
			if (this.gameBoard.getMap().isValid(event.getZone())) {
				if (!this.gameBoard.getMap().getZone(event.getZone()).hasStudio()) {
					Unit mascot = this.gameBoard.getMap()
							.getZone(event.getZone())
							.getUnit(UnitLevel.MASCOT, this.currentPlayer.getFaction());

					if (mascot != null) {
						this.currentPlayer.decrementStaffPoints(OPEN_STUDIO_COST);
						Studio newStudio = new Studio(event.getZone(), this.currentPlayer);
						this.gameBoard.getMap().getZone(event.getZone()).setStudio(newStudio);
						newStudio.setController(mascot);
						this.nonUlimitedActionDone = true;
					}
				}
			}
		}
	}

	@Override
	public void handleInvokeUnitEvent(InvokeUnitEvent event) { // FIXME : heroes conditions
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			Log.debug(getClass().getName(), "Invalid zone selected.");
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(this.currentPlayer.getUnitCost(event.getUnitType()))) {
			if (this.currentPlayer.getUnitCounter().getNumberOfUnits(event.getUnitType()) < event.getUnitType()
					.getMaxNumber()) {
				Zone invocationZone = this.gameBoard.getMap().getZone(event.getZone());

				if (invocationZone.hasStudio()
						&& (this.currentPlayer.hasFaction(invocationZone.getStudio().getCurrentFaction())
						|| this.currentPlayer.hasCapacity(CapacityName.MARKET_FLOODING))
						|| (event.getUnitType().isLevel(UnitLevel.MASCOT)
						&& (!this.currentPlayer.getUnitCounter().hasUnits()
						|| invocationZone.getUnits().stream().anyMatch(unit -> this.currentPlayer.hasFaction(unit.getFaction()))))
						) {
					invokeUnit(invocationZone, event.getUnitType());

					this.nonUlimitedActionDone = true;
				}
			}
		}
	}

	private void invokeUnit(Zone zone, UnitType unitType) {
		Unit unit = new Unit(unitType);
		this.currentPlayer.getUnitCounter().addUnit(unitType, unit.getID());
		this.currentPlayer.decrementStaffPoints(this.currentPlayer.getUnitCost(unitType));
		unit.addInZone(zone);
	}

	@Override
	public void handleSkipAllEvent(SkipAllEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		this.currentPlayer.setStaffAvailable(0);

		this.machine.get().update();
	}

	@Override
	public void handleBattleEvent(StartBattleEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.zonesThatHadABattle.contains(Integer.valueOf(event.getZone()))) {
			Log.debug(getClass().getName(), "Zone already had a battle.");
			return;
		}

		if (!this.currentPlayer.hasRequiredStaffPoints(this.currentPlayer.getBattleCost())) {
			Log.debug(getClass().getName(), "Not enough staff points.");
			return;
		}

		if (this.nonUlimitedActionDone && this.currentPlayer.getNumberOfProduction() != 6) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		List<Player> thirdPartPlayers = this.gameBoard.getPlayers().stream()
				.filter(player -> player.getID() != event.getAttackerID() && player.getID() != event.getDefenderID())
				.collect(Collectors.toList());

		BattleContext battleContext = new BattleContext(this.gameBoard.getPlayer(event.getAttackerID()),
				this.gameBoard.getPlayer(event.getDefenderID()),
				this.gameBoard.getMap().getZone(event.getZone()),
				thirdPartPlayers);

		this.currentBattleStateMachine = new DefaultStateMachine(new PreBattleState(battleContext, this.gameBoard.getMap()));

		this.nonUlimitedActionDone = true;
	}

	@Override
	public void handleCaptureMascotEvent(CaptureMascotEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non unlimited action already done.");
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			Log.debug(getClass().getName(), "Invalid zone selected.");
			return;
		}

		if (this.currentPlayer.getID() == event.getHuntedPlayerID()) {
			Log.debug(getClass().getName(), "Cannot hunt himself.");
			return;
		}

		this.huntingZone = this.gameBoard.getMap().getZone(event.getZone());
		this.huntedPlayer = this.gameBoard.getPlayer(event.getHuntedPlayerID());

		Optional<Unit> hunter = this.huntingZone.getUnits()
				.stream()
				.filter(u -> u.hasFaction(this.currentPlayer.getFaction()))
				.max(Unit::bestUnitComparator);

		if (!hunter.isPresent()) {
			Log.debug(getClass().getName(), "No hunter present.");
			return;
		}

		List<Unit> huntedUnits = this.huntingZone.getUnits()
				.stream()
				.filter(u -> u.isLevel(UnitLevel.MASCOT)
						&& u.hasFaction(this.huntedPlayer.getFaction()))
				.collect(Collectors.toList());

		if (huntedUnits.isEmpty()) {
			Log.debug(getClass().getName(), "No hunted unit present.");
			return;
		}

		Optional<Unit> mascotProtector =
				this.huntingZone.getUnits()
						.stream()
						.filter(u -> isMascotProtector(hunter.get(), u))
						.findFirst();

		if (mascotProtector.isPresent()) {
			Log.debug(getClass().getName(), "Mascot protector present.");
			return;
		}

		this.nonUlimitedActionDone = true;

		if (huntedUnits.size() == 1) {
			handleMascotToCaptureChoiceEvent(new MascotToCaptureChoiceEvent(event.getHuntedPlayerID(),
					huntedUnits.get(0).getID()));
		} else {
			LogicEventDispatcher.getInstance().fire(new AskMascotToCaptureEvent(event.getHuntedPlayerID(), event.getZone()));
		}
	}

	private boolean isMascotProtector(Unit hunter, Unit unit) {
		return unit.hasFaction(this.huntedPlayer.getFaction())
				&& Unit.bestUnitComparator(hunter, unit) <= 0;
	}

	@Override
	public void handleMascotToCaptureChoiceEvent(MascotToCaptureChoiceEvent event) {
		if (this.huntedPlayer.getID() != event.getPlayerID()) {
			return;
		}

		Unit mascot = this.huntingZone.getUnit(event.getMascotID());
		if (mascot == null || !mascot.isLevel(UnitLevel.MASCOT)) {
			return;
		}

		mascot.removeFromMap();
		this.currentPlayer.addUnitCaptured(mascot, this.huntedPlayer);

		this.huntingZone = null;
		this.huntedPlayer = null;
	}

	@Override
	public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
		this.gameEnded = true;
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		// nothing to do
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {
		// nothing to do
	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		// nothing to do
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		this.currentBattleStateMachine = null;
	}

	@Override
	public void handleFinishTurnRequest(FinishTurnRequestEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (!this.nonUlimitedActionDone) {
			Log.debug(getClass().getName(), "Non ulimited action not done.");
			return;
		}

		this.machine.get().update();
	}
}
