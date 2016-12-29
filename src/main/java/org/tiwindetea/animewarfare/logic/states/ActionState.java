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
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
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

public class ActionState extends GameState implements MoveUnitEventListener, OpenStudioEventListener,
		InvokeUnitEventListener, SkipTurnEventListener, StartBattleEventListener,
		CaptureMascotEventListener, MascotToCaptureChoiceEventListener, GameEndedEventListener {
	private static final int MOVE_COST = 1; // TODO: Externalize
	private static final int OPEN_STUDIO_COST = 3; // TODO: Externalize

	private final List<Integer> zonesWithBattle = new ArrayList<>();

	private List<Player> players;
	private boolean gameEnded;
	private boolean phaseEnded;

	private Player currentPlayer;
	private int currentPlayerPosition;

	private Player huntedPlayer;
	private Zone huntingZone;

	public ActionState(GameBoard gameBoard) {
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
		// TODO
	}

	private void setNextPlayer() {
		this.zonesWithBattle.clear(); // TODO: Move this out of this method.

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
			// TODO: Send next player event.
		}
	}

	@Override
	public void onExit() {
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
		if (event.getPlayerID() != this.currentPlayer.getID()) {
			// TODO: This player should not send this event now.
			// Cheater.
			return true;
		}

		return false;
	}

	@Override
	public void handleMoveUnitEvent(MoveUnitEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(MOVE_COST, event.getMovements().size())) {
			List<Pair<Unit, MoveUnitEvent.Movement>> validMovements = new LinkedList<>();
			for (MoveUnitEvent.Movement movement : event.getMovements()) {
				if (this.gameBoard.getMap().isValid(movement.getSourceZone()) && this.gameBoard.getMap()
						.isValid(movement.getDestinationZone())) {
					Unit unitToMove = this.gameBoard.getMap()
							.getZone(movement.getSourceZone())
							.getUnit(movement.getUnitID());

					if (unitToMove != null) {
						if (this.gameBoard.getMap().areAdjacent(movement.getSourceZone(),
								movement.getDestinationZone())) {
							validMovements.add(new Pair<>(unitToMove, movement));
						} else {
							// TODO: Illegal Movement
						}
					} else {
						// TODO: No such unit.
					}
				}
			}

			if (event.getMovements().size() == validMovements.size()) {
				this.currentPlayer.decrementStaffPoints(MOVE_COST, validMovements.size());
				for (Pair<Unit, MoveUnitEvent.Movement> movement : validMovements) {
					movement.getKey().move(this.gameBoard.getMap().getZone(movement.getValue().getDestinationZone()));
				}
			}
		}

		// TODO: Get next action or get next player.
	}

	@Override
	public void handleOpenStudioEvent(OpenStudioEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(OPEN_STUDIO_COST)) {
			if (this.gameBoard.getMap().isValid(event.getZone())) {
				if (this.gameBoard.getMap().getZone(event.getZone()).getStudio() != null) {
					Unit mascot = this.gameBoard.getMap()
							.getZone(event.getZone())
							.getUnit(UnitLevel.MASCOT,
									this.currentPlayer.getFaction());

					if (mascot != null) {
						this.gameBoard.getMap().getZone(event.getZone()).setStudio(new Studio());
						this.currentPlayer.decrementStaffPoints(OPEN_STUDIO_COST);
					} else {
						// TODO: Invalid action.
					}
				} else {
					// TODO: Invalid action, a studio already exists in this zone.
				}
			}
		}
	}

	@Override
	public void handleInvokeUnitEvent(InvokeUnitEvent event) {
		if (isInvalidPlayer(event)) {
			return;
		}

		if (!this.gameBoard.getMap().isValid(event.getZone())) {
			// TODO: Invalid zone.
			return;
		}

		if (this.currentPlayer.hasRequiredStaffPoints(this.currentPlayer.getUnitCostModifier()
		                                                                .getCost(event.getUnitType()))) {
			if (event.getUnitType().getUnitLevel() == UnitLevel.MASCOT) {
				if (!this.currentPlayer.getUnitCounter().hasUnits()) {
					this.gameBoard.getMap().getZone(event.getZone()).addUnit(new Unit(event.getUnitType()));
					return;
				}
			}
		}

		if (this.currentPlayer.getUnitCounter().getNumberOfUnits(event.getUnitType()) <= event.getUnitType()
				.getMaxNumber()) {
			if (this.gameBoard.getMap().getZone(event.getZone()).hasUnitOfFaction(this.currentPlayer.getFaction())) {
				this.gameBoard.getMap().getZone(event.getZone()).addUnit(new Unit(event.getUnitType()));
			} else {
				// TODO: No unit of the faction in the target zone.
			}
		} else {
			// TODO: Too much unit.
		}
	}

	@Override
	public void handleSkipTurnEvent(SkipTurnEvent event) {
		if (isInvalidPlayer(event)) {
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
			// TODO
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
			// TODO
			return;
		}

		this.huntingZone = this.gameBoard.getMap().getZone(event.getZone());
		Unit hunter = this.huntingZone.getUnit(event.getHunterUnit());

		this.huntedPlayer = this.gameBoard.getPlayer(event.getHuntedPlayerID());
		if (hunter.getFaction() == this.huntedPlayer.getFaction()) {
			// TODO
			return;
		}

		Optional<Unit> mascotProtector =
				this.huntingZone.getUnits()
						.stream()
						.filter(u -> isMascotProtector(hunter, u))
						.findFirst();

		if (mascotProtector.isPresent()) {
			// TODO
			return;
		}

		EventDispatcher.getInstance().fire(new AskMascotToCaptureEvent(event.getHuntedPlayerID()));
	}

	private boolean isMascotProtector(Unit hunter, Unit unit) {
		return unit.getFaction() == this.huntedPlayer.getFaction()
				&& hunter.getType().getUnitLevel().ordinal() <= unit.getType()
				.getUnitLevel()
				.ordinal();
	}

	@Override
	public void handleMascotToCaptureChoiceEvent(MascotToCaptureChoiceEvent event) {
		if (this.huntedPlayer.getID() != event.getPlayerID()) {
			// TODO
			return;
		}

		Unit mascot = this.huntingZone.getUnit(event.getMascotID());
		if (mascot == null || mascot.getType().getUnitLevel() != UnitLevel.MASCOT) {
			// TODO
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
