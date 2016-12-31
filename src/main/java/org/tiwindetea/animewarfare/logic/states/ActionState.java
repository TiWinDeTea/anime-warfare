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

import javafx.util.Pair;
import org.lomadriel.lfc.event.EventDispatcher;
import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.states.events.AskMascotToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.BattleStartedEvent;
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
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.InvokeUnitEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MascotToCaptureChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.MascotToCaptureChoiceEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitEventListener;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEvent;
import org.tiwindetea.animewarfare.net.logicevent.OpenStudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.SkipTurnEvent;
import org.tiwindetea.animewarfare.net.logicevent.SkipTurnEventListener;
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
 * @author Jérome BOULMIER
 */
class ActionState extends GameState implements MoveUnitEventListener, OpenStudioEventListener,
		InvokeUnitEventListener, SkipTurnEventListener, StartBattleEventListener,
		CaptureMascotEventListener, MascotToCaptureChoiceEventListener, GameEndedEventListener {
	private static final int MOVE_COST = 1; // TODO: Externalize
	private static final int OPEN_STUDIO_COST = 3; // TODO: Externalize

	private final List<Integer> zonesWithBattle = new ArrayList<>();
	private final List<Integer> movedUnit = new ArrayList<>();

	private List<Player> players;
	private boolean gameEnded;
	private boolean phaseEnded;

	private Player currentPlayer;
	private int currentPlayerPosition;

	private Player huntedPlayer;
	private Zone huntingZone;

	ActionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		registerEventListeners();
		EventDispatcher.getInstance().fire(new PhaseChangedEvent(PhaseChangedEvent.Phase.ACTION));
		this.players = this.gameBoard.getPlayersInOrder();
	}

	private void registerEventListeners() {
		EventDispatcher.getInstance().addListener(MoveUnitEvent.class, this);
		EventDispatcher.getInstance().addListener(OpenStudioEvent.class, this);
		EventDispatcher.getInstance().addListener(InvokeUnitEvent.class, this);
		EventDispatcher.getInstance().addListener(SkipTurnEvent.class, this);
		EventDispatcher.getInstance().addListener(StartBattleEvent.class, this);
		EventDispatcher.getInstance().addListener(CaptureMascotEvent.class, this);
		EventDispatcher.getInstance().addListener(MascotToCaptureChoiceEvent.class, this);
		EventDispatcher.getInstance().addListener(GameEndedEvent.class, this);
	}

	@Override
	public void update() {
		this.zonesWithBattle.clear();
		this.movedUnit.clear();

		setNextPlayer();
	}

	private void setNextPlayer() {
		int counter = 0;

		do {
			this.currentPlayerPosition = (this.currentPlayerPosition + 1) % this.players.size();
			this.currentPlayer = this.players.get(this.currentPlayerPosition);
			++counter;
		} while (this.currentPlayer.getStaffAvailable() == 0 && counter != this.players.size());

		if (counter == this.players.size()) {
			// End the phase.
			this.phaseEnded = true;
		} else {
			EventDispatcher.send(new NextPlayerEvent(this.currentPlayer.getID()));
		}
	}

	@Override
	public void onExit() {
		for (Player player : this.players) {
			player.setCanPass(false);
		}

		unregisterEventListeners();
	}

	private void unregisterEventListeners() {
		EventDispatcher.getInstance().removeListener(MoveUnitEvent.class, this);
		EventDispatcher.getInstance().removeListener(OpenStudioEvent.class, this);
		EventDispatcher.getInstance().removeListener(InvokeUnitEvent.class, this);
		EventDispatcher.getInstance().removeListener(SkipTurnEvent.class, this);
		EventDispatcher.getInstance().removeListener(StartBattleEvent.class, this);
		EventDispatcher.getInstance().removeListener(CaptureMascotEvent.class, this);
		EventDispatcher.getInstance().removeListener(MascotToCaptureChoiceEvent.class, this);
		EventDispatcher.getInstance().removeListener(GameEndedEvent.class, this);
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
	public void handleMoveUnitEvent(MoveUnitEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}


		List<Pair<Unit, MoveUnitEvent.Movement>> validMovements = new LinkedList<>();

		for (MoveUnitEvent.Movement movement : event.getMovements()) {
			if (this.gameBoard.getMap().isValid(movement.getSourceZone())
					&& this.gameBoard.getMap().isValid(movement.getDestinationZone())) {

				Unit unitToMove = this.gameBoard.getMap()
				                                .getZone(movement.getSourceZone())
				                                .getUnit(movement.getUnitID());

				if (unitToMove != null
						&& unitToMove.hasFaction(this.currentPlayer.getFaction())
						&& !this.movedUnit.contains(Integer.valueOf(unitToMove.getID()))) { // A Unit can only be moved once per Action.

					if (this.gameBoard.getMap().areAdjacent(movement.getSourceZone(),
							movement.getDestinationZone())) {
						validMovements.add(new Pair<>(unitToMove, movement));
					}
				}
			}
		}

		if (this.currentPlayer.hasRequiredStaffPoints(MOVE_COST, validMovements.size())) {
			if (event.getMovements().size() == validMovements.size()) {
				this.currentPlayer.decrementStaffPoints(MOVE_COST, validMovements.size());
				this.currentPlayer.setCanPass(true);

				for (Pair<Unit, MoveUnitEvent.Movement> movement : validMovements) {
					movement.getKey().move(this.gameBoard.getMap().getZone(movement.getValue().getDestinationZone()));
					this.movedUnit.add(Integer.valueOf(movement.getKey().getID()));
				}
			}
		}

		this.machine.get().update();
	}

	@Override
	public void handleOpenStudioEvent(OpenStudioEvent event) {
		if (isInvalidPlayer(event)) {
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
						Studio newStudio = new Studio(event.getZone());
						this.gameBoard.getMap().getZone(event.getZone()).setStudio(newStudio);
						newStudio.setController(mascot);
					}
				}
			}
		}

		this.machine.get().update();
	}

	@Override
	public void handleInvokeUnitEvent(InvokeUnitEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(this.currentPlayer.getUnitCostModifier()
		                                                                .getCost(event.getUnitType()))) {
			if (this.currentPlayer.getUnitCounter().getNumberOfUnits(event.getUnitType()) <= event.getUnitType()
			                                                                                      .getMaxNumber()) {
				Zone invocationZone = this.gameBoard.getMap().getZone(event.getZone());

				if (invocationZone.hasStudio() && this.currentPlayer.hasFaction(invocationZone.getStudio()
				                                                                              .getCurrentFaction())
						|| !this.currentPlayer.getUnitCounter().hasUnits() && event.getUnitType()
						                                                           .isLevel(UnitLevel.MASCOT)) {
					invokeUnit(invocationZone, event.getUnitType());
				}
			}
		}
	}

	private void invokeUnit(Zone zone, UnitType unitType) {
		zone.addUnit(new Unit(unitType));
		this.currentPlayer.getUnitCounter().addUnit(unitType);
	}

	@Override
	public void handleSkipTurnEvent(SkipTurnEvent event) {
		if (isInvalidPlayer(event) || !this.currentPlayer.canPass()) {
			return;
		}

		this.currentPlayer.setStaffAvailable(0);
		EventDispatcher.getInstance().fire(new SkipTurnEvent(this.currentPlayer.getID()));
	}

	@Override
	public void handleBattleEvent(StartBattleEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (this.zonesWithBattle.contains(Integer.valueOf(event.getZone()))) {
			return;
		}

		// TODO: Setup battle.

		EventDispatcher.getInstance()
		               .fire(new BattleStartedEvent(this.gameBoard.getPlayer(event.getAttackerID()),
				               this.gameBoard.getPlayer(event.getDefenderID()),
				               this.gameBoard.getMap().getZone(event.getZone())));
	}

	@Override
	public void handleCaptureMascotEvent(CaptureMascotEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			return;
		}

		if (this.currentPlayer.getID() == event.getHuntedPlayerID()) {
			return;
		}

		this.huntingZone = this.gameBoard.getMap().getZone(event.getZone());
		this.huntedPlayer = this.gameBoard.getPlayer(event.getHuntedPlayerID());

		Optional<Unit> hunter = this.huntingZone.getUnits()
		                                        .stream()
		                                        .filter(u -> u.hasFaction(this.currentPlayer.getFaction()))
		                                        .max(Unit::bestUnitComparator);

		if (!hunter.isPresent()) {
			return;
		}

		List<Unit> huntedUnits = this.huntingZone.getUnits()
		                                         .stream()
		                                         .filter(u -> u.isLevel(UnitLevel.MASCOT)
				                                         && u.hasFaction(this.huntedPlayer.getFaction()))
		                                         .collect(Collectors.toList());

		if (huntedUnits.isEmpty()) {
			return;
		}

		Optional<Unit> mascotProtector =
				this.huntingZone.getUnits()
				                .stream()
				                .filter(u -> isMascotProtector(hunter.get(), u))
				                .findFirst();

		if (mascotProtector.isPresent()) {
			return;
		}

		if (huntedUnits.size() == 1) {
			handleMascotToCaptureChoiceEvent(new MascotToCaptureChoiceEvent(event.getHuntedPlayerID(),
					huntedUnits.get(0).getID()));
		} else {
			EventDispatcher.getInstance().fire(new AskMascotToCaptureEvent(event.getHuntedPlayerID(), event.getZone()));
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

		this.currentPlayer.addUnitCaptured(mascot);
		this.huntingZone.removeUnit(mascot);

		this.huntingZone = null;
		this.huntedPlayer = null;
	}

	@Override
	public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
		this.gameEnded = true;
	}
}
