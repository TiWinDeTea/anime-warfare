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
import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class StaffHiringState extends GameState {
	private final List<Player> drawPlayers = new LinkedList<>();

	StaffHiringState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		computeStaffAvailable();

		LogicEventDispatcher.getInstance().fire(new PhaseChangedEvent(PhaseChangedEvent.Phase.STAFF_HIRING));
	}

	@Override
	public void update() {
		// Nothing to do
	}

	@Override
	public void onExit() {
		this.gameBoard.getPlayers().forEach(this::releaseCapturedUnits);
	}

	@Override
	public State next() {
		return new FirstPlayerSelectionState(this.gameBoard, this.drawPlayers);
	}

	private void computeStaffAvailable() {
		List<Studio> studios = this.gameBoard.getMap().getStudios();

		int numberOfNonControlledPortal = getNumberOfNonControlledPortal(studios);
		int maxStaffPoints = 0;


		List<Pair<Integer, Integer>> playerStaff = new LinkedList<>();
		for (Player player : this.gameBoard.getPlayers()) {
			int numberOfCapturedMascot = (int) player.getUnitCaptured()
			                                         .stream()
			                                         .filter(unit -> unit.isLevel(UnitLevel.MASCOT))
			                                         .count();

			int staffPoints = 2 * getNumberOfControlledPortal(studios, player)
					+ player.getUnitCounter().getNumberOfUnits(UnitLevel.MASCOT)
					+ numberOfCapturedMascot
					+ numberOfNonControlledPortal;

			playerStaff.add(new Pair<>(Integer.valueOf(player.getID()), Integer.valueOf(staffPoints)));

			if (staffPoints > maxStaffPoints) {
				maxStaffPoints = staffPoints;
				this.drawPlayers.clear();
				this.drawPlayers.add(player);
			} else if (staffPoints == maxStaffPoints) {
				this.drawPlayers.add(player);
			}
		}

		// Increment maxStaffPoints to an even number.
		if (maxStaffPoints % 2 == 1) {
			++maxStaffPoints;
		}

		adjusteAndSetStaffPoint(playerStaff, maxStaffPoints / 2);

		this.machine.get().update();
	}

	private void releaseCapturedUnits(Player player) {
		for (Unit unit : player.getUnitCaptured()) {
			this.gameBoard.getPlayer(unit.getFaction()).getUnitCounter().removeUnit(unit.getType(), unit.getID());
		}

		player.getUnitCaptured().clear();

		// TODO: Units released event
	}

	private void adjusteAndSetStaffPoint(List<Pair<Integer, Integer>> playerStaff, int minStaffPoints) {
		for (Pair<Integer, Integer> staff : playerStaff) {
			if (staff.getValue().intValue() < minStaffPoints) {
				this.gameBoard.getPlayer(staff.getKey()).setStaffAvailable(minStaffPoints);
			} else {
				this.gameBoard.getPlayer(staff.getKey()).setStaffAvailable(staff.getValue());
			}
		}
	}

	private static int getNumberOfNonControlledPortal(List<Studio> studios) {
		return (int) studios.stream().filter(Objects::isNull).count();
	}

	private static int getNumberOfControlledPortal(List<Studio> studios, Player player) {
		return (int) studios.stream().map(Studio::getCurrentFaction)
		                    .filter(faction -> player.hasFaction(faction)).count();
	}
}
