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
import org.tiwindetea.animewarfare.logic.Mask;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.battle.BattleContext;
import org.tiwindetea.animewarfare.logic.battle.PreBattleState;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.states.events.AskUnitToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.NextPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.AbandonStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.AbandonStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.ActionEvent;
import org.tiwindetea.animewarfare.net.logicevent.CaptureStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.CaptureStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.CaptureUnitRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.CaptureUnitRequestEventListener;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEventListener;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEventListener;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.SkipAllEvent;
import org.tiwindetea.animewarfare.net.logicevent.SkipAllEventListener;
import org.tiwindetea.animewarfare.net.logicevent.StartBattleEvent;
import org.tiwindetea.animewarfare.net.logicevent.StartBattleEventListener;
import org.tiwindetea.animewarfare.net.logicevent.UnitToCaptureEvent;
import org.tiwindetea.animewarfare.net.logicevent.UnitToCaptureEventListener;

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
		CaptureUnitRequestEventListener, UnitToCaptureEventListener, GameEndedEventListener,
		BattleEventListener, FinishTurnRequestEventListener, CaptureStudioEventListener,
		AbandonStudioEventListener {
	private static final int MOVE_COST = 1; // TODO: Externalize
	private static final int OPEN_STUDIO_COST = 3; // TODO: Externalize

	private final List<Integer> zonesThatHadABattle = new ArrayList<>();
	private final List<Integer> alreadyMovedUnit = new ArrayList<>();

	private boolean gameEnded;
	private boolean phaseEnded;
	private boolean longActionBeingPerformed;

	private Player currentPlayer;

	private Player huntedPlayer;
	private Zone huntingZone;

	private StateMachine currentBattleStateMachine;

	private boolean nonUnlimitedActionDone;

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
		LogicEventDispatcher.getInstance().addListener(CaptureUnitRequestEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(UnitToCaptureEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(GameEndedEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(BattleEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(FinishTurnRequestEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(AbandonStudioEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(CaptureStudioEvent.class, this);
	}

	@Override
	public void update() {
		this.zonesThatHadABattle.clear();
		this.alreadyMovedUnit.clear();
		this.nonUnlimitedActionDone = false;

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
		LogicEventDispatcher.getInstance().removeListener(CaptureUnitRequestEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(UnitToCaptureEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(GameEndedEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(BattleEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(FinishTurnRequestEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(AbandonStudioEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(CaptureStudioEvent.class, this);
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

		if (this.nonUnlimitedActionDone) {
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
					this.nonUnlimitedActionDone = true;
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

		if (this.nonUnlimitedActionDone) {
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
						this.nonUnlimitedActionDone = true;
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

		if (this.nonUnlimitedActionDone) {
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

				if (event.getUnitType().isLevel(UnitLevel.HERO) &&
						!this.gameBoard.getHeroesInvoked().contains(event.getUnitType())) {
					switch (event.getUnitType()) {
						case SAKAMAKI_IZAYOI:
							izayoiInvocation(invocationZone);
							break;
						case CTHUKO:
							cthukoInvocation(invocationZone);
							break;
						case HIMEJI_MIZUKI:
							himejiInvocation(invocationZone);
							break;
						case LELOUCH:
							lelouchInvocation(invocationZone);
							break;
						case NYARUKO:
							if (checkInvocationConditions(event, invocationZone)) {
								this.gameBoard.getHeroesInvoked().add(UnitType.NYARUKO);
								invokeUnit(invocationZone, UnitType.NYARUKO);
								this.nonUnlimitedActionDone = true;
							}
							break;
					}
				} else if (checkInvocationConditions(event, invocationZone)) {
					invokeUnit(invocationZone, event.getUnitType());
					this.nonUnlimitedActionDone = true;
				}
			}
		}

	}

	private boolean checkInvocationConditions(InvokeUnitEvent event, Zone invocationZone) {
		return invocationZone.hasStudio()
				&& (this.currentPlayer.hasFaction(invocationZone.getStudio().getCurrentFaction())
				|| this.currentPlayer.hasCapacity(CapacityName.MARKET_FLOODING))
				|| (event.getUnitType().isLevel(UnitLevel.MASCOT)
				&& (!this.currentPlayer.getUnitCounter().hasUnits()
				|| invocationZone.getUnits()
				                 .stream()
				                 .anyMatch(unit -> this.currentPlayer.hasFaction(unit.getFaction()))));
	}

	private void lelouchInvocation(Zone invocationZone) {
		Unit cc = invocationZone.getUnits()
		                        .stream()
		                        .filter(u -> u.getType() == UnitType.CC)
		                        .findFirst()
		                        .orElse(null);

		if (cc != null) {
			cc.removeFromMap();
			this.currentPlayer.getUnitCounter().removeUnit(cc.getType(), cc);
			invokeUnit(invocationZone, UnitType.LELOUCH);
			this.nonUnlimitedActionDone = true;
		}
	}

	private void cthukoInvocation(Zone invocationZone) {
		if (this.currentPlayer.getUnitCounter().getNumberOfUnits(UnitType.NYARUKO) > 0) {
			this.gameBoard.getHeroesInvoked().add(UnitType.CTHUKO);

			invokeUnit(invocationZone, UnitType.CTHUKO);
			this.nonUnlimitedActionDone = true;
		}
	}

	private void himejiInvocation(Zone invocationZone) {
		if (this.gameBoard.getMap()
		                  .getStudios()
		                  .stream()
		                  .anyMatch(s -> this.currentPlayer.hasFaction(s.getCurrentFaction()))) {
			List<Unit> mascots = invocationZone.getUnits()
			                                   .stream()
			                                   .filter(u -> u.hasFaction(this.currentPlayer.getFaction())
					                                   && u.isLevel(UnitLevel.MASCOT))
			                                   .collect(Collectors.toList());

			if (mascots.size() >= 2) {
				this.gameBoard.getHeroesInvoked().add(UnitType.HIMEJI_MIZUKI);

				for (int i = 0; i < 2; i++) {
					mascots.get(i).removeFromMap();
					this.currentPlayer.getUnitCounter().removeUnit(mascots.get(i).getType(), mascots.get(i));
				}

				invokeUnit(invocationZone, UnitType.HIMEJI_MIZUKI);
				this.nonUnlimitedActionDone = true;
			}
		}
	}

	private void izayoiInvocation(Zone invocationZone) {
		if (!this.gameBoard.getHeroesInvoked().isEmpty()) {
			this.gameBoard.getHeroesInvoked().add(UnitType.SAKAMAKI_IZAYOI);

			this.currentPlayer.getCostModifier().addUnitCost(UnitType.SAKAMAKI_IZAYOI, new Mask(-6));

			invokeUnit(invocationZone, UnitType.SAKAMAKI_IZAYOI);
			this.nonUnlimitedActionDone = true;
		}
	}

	private void invokeUnit(Zone zone, UnitType unitType) {
		this.currentPlayer.decrementStaffPoints(this.currentPlayer.getUnitCost(unitType));
		Unit unit = new Unit(unitType);
		this.currentPlayer.getUnitCounter().addUnit(unitType, unit);
		unit.addInZone(zone);
	}

	@Override
	public void handleSkipAllEvent(SkipAllEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
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

		if (this.nonUnlimitedActionDone && this.currentPlayer.getNumberOfProduction() != 6) {
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

		this.nonUnlimitedActionDone = true;
		this.currentPlayer.decrementStaffPoints(this.currentPlayer.getBattleCost());
	}

	@Override
	public void handleCaptureUnitRequestEvent(CaptureUnitRequestEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (this.nonUnlimitedActionDone) {
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

		if (event.wantToSteelFans()) {
			if (!this.currentPlayer.hasCapacity(CapacityName.MORE_FANS)) {
				Log.debug(getClass().getName(), "Player can't steel fans without the capacity.");
				return;
			}

			if (event.getUnitLevel() != UnitLevel.MASCOT) {
				Log.debug(getClass().getName(), "Unit should be a mascot.");
				return;
			}

			if (this.currentPlayer.hasRequiredStaffPoints(CapacityName.MORE_FANS.getStaffCost())) {
				Log.debug(getClass().getName(), "Not enough staff points. (More Fans)");
				return;
			}

		} else if (!this.currentPlayer.hasRequiredStaffPoints(1)) {
			Log.debug(getClass().getName(), "Not enough staff points. (Capture)");
			return;
		}

		if (event.getUnitLevel() != UnitLevel.MASCOT
				&& !this.currentPlayer.hasCapacity(CapacityName.GENIUS_KIDNAPPER)) {
			Log.debug(getClass().getName(), "Can't hunt monster without Genius Kidnapper capacity.");
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
		                                         .filter(u -> u.isLevel(event.getUnitLevel())
				                                         && u.hasFaction(this.huntedPlayer.getFaction()))
		                                         .collect(Collectors.toList());

		if (huntedUnits.isEmpty()) {
			Log.debug(getClass().getName(), "No hunted unit present.");
			return;
		}

		Optional<Unit> mascotProtector =
				this.huntingZone.getUnits()
				                .stream()
				                .filter(u -> isUnitProtector(hunter.get(), u))
				                .findFirst();

		if (mascotProtector.isPresent()) {
			Log.debug(getClass().getName(), "Unit protector present.");
			return;
		}

		this.nonUnlimitedActionDone = true;

		if (event.wantToSteelFans()) {
			this.currentPlayer.decrementStaffPoints(CapacityName.MORE_FANS.getStaffCost());
			this.huntedPlayer.decrementFans(1);
			this.currentPlayer.incrementFans(1);
		} else {
			this.currentPlayer.decrementStaffPoints(1);
		}

		if (huntedUnits.size() == 1) {
			handleUnitToCaptureEvent(new UnitToCaptureEvent(event.getHuntedPlayerID(),
					huntedUnits.get(0).getID()));
		} else {
			this.longActionBeingPerformed = true;
			LogicEventDispatcher.getInstance()
			                    .fire(new AskUnitToCaptureEvent(event.getHuntedPlayerID(),
					                    event.getZone(),
					                    event.getUnitLevel()));
		}
	}

	private boolean isUnitProtector(Unit hunter, Unit unit) {
		return unit.hasFaction(this.huntedPlayer.getFaction())
				&& Unit.bestUnitComparator(hunter, unit) <= 0;
	}

	@Override
	public void handleUnitToCaptureEvent(UnitToCaptureEvent event) {
		if (this.huntedPlayer.getID() != event.getPlayerID()) {
			return;
		}

		Unit unit = this.huntingZone.getUnit(event.getUnitID());
		if (unit == null) {
			Log.debug(getClass().getName(), "No such unit.");
			return;
		}

		if (!unit.isLevel(UnitLevel.MASCOT) && !this.currentPlayer.hasCapacity(CapacityName.GENIUS_KIDNAPPER)) {
			Log.debug(getClass().getName(), "Can't hunt monster without Genius Kidnapper capacity.");
			return;
		}

		unit.removeFromMap();
		this.currentPlayer.addUnitCaptured(unit, this.huntedPlayer);

		this.huntingZone = null;
		this.huntedPlayer = null;
		this.longActionBeingPerformed = false;
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
		this.longActionBeingPerformed = false;
	}

	@Override
	public void handleFinishTurnRequest(FinishTurnRequestEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		if (!this.nonUnlimitedActionDone) {
			Log.debug(getClass().getName(), "Non ulimited action not done.");
			return;
		}

		if (this.longActionBeingPerformed) {
			Log.debug(getClass().getName(), "A unit is being captured.");
			return;
		}

		this.machine.get().update();
	}

	@Override
	public void handleAbandonStudioEvent(AbandonStudioEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		// unlimited action !!

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			Log.debug(getClass().getName(), "Invalid zone.");
			return;
		}

		Zone zone = this.gameBoard.getMap().getZone(event.getZone());
		if (!zone.hasStudio()) {
			Log.debug(getClass().getName(), "No studio in the zone.");
			return;
		}

		if (zone.getStudio().getController() == null) {
			Log.debug(getClass().getName(), "Studio not controlled.");
			return;
		}

		if (this.currentPlayer.getFaction() != zone.getStudio().getController().getFaction()) {
			Log.debug(getClass().getName(), "Not controlled by the current player studio...");
			return;
		}

		zone.getStudio().setController(null);
	}

	@Override
	public void handleCaptureStudioEvent(CaptureStudioEvent event) {
		if (isInvalidPlayer(event)) {
			Log.debug(getClass().getName(), "Invalid player.");
			return;
		}

		// unlimited action !!

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			Log.debug(getClass().getName(), "Invalid zone.");
			return;
		}

		Zone zone = this.gameBoard.getMap().getZone(event.getZone());
		if (!zone.hasStudio()) {
			Log.debug(getClass().getName(), "No studio in the zone.");
			return;
		}

		if (zone.getStudio().getController() != null) {
			Log.debug(getClass().getName(), "Studio already controlled.");
			return;
		}

		Unit unit = this.gameBoard.getMap()
				.getZone(event.getZone())
				.getUnit(event.getMascotId());

		if (unit == null || unit.getFaction() != this.currentPlayer.getFaction()
				|| unit.getType().getUnitLevel() != UnitLevel.MASCOT) {
			Log.debug(getClass().getName(), "No current player mascot in zone.");
			return;
		}

		zone.getStudio().setController(unit);
	}
}
