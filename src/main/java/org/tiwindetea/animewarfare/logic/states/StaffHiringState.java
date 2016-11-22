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

import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;

import java.util.List;
import java.util.stream.Collectors;

public class StaffHiringState extends GameState {
	public StaffHiringState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		computeStaffAvailable();

		// TODO: Send event phase ended.
	}

	@Override
	public void update() {
		// TODO
	}

	@Override
	public void onExit() {
		// TODO
	}

	@Override
	public State next() {
		return new FirstPlayerSelectionState(this.gameBoard);
	}

	private void computeStaffAvailable() {
		// TODO

		List<Studio> studios = this.gameBoard.getZones().stream()
				.map(Zone::getStudio)
				.filter(studio -> studio != null)
				.collect(Collectors.toList());

		int numberOfNonControlledPortal = getNumberOfNonControlledPortal(studios);
		int maxStaffPoints = 0;

		int numberOfCapturedAcolytes = 0; // TODO
		for (Player player : this.gameBoard.getPlayers()) {
			int staffPoints = 2 * getNumberOfControlledPortal(studios, player)
					+ player.getNumberOfUnits(UnitLevel.MASCOT)
					+ numberOfCapturedAcolytes
					+ numberOfNonControlledPortal;

			player.setStaffAvailable(staffPoints);

			if (staffPoints > maxStaffPoints) {
				maxStaffPoints = staffPoints;
			}
		}

		// TODO Free captured acolytes

		// Increment maxStaffPoints to an even number.
		if (maxStaffPoints % 2 == 1) {
			++maxStaffPoints;
		}

		adjustNumberOfStaffMembersTo(maxStaffPoints / 2);
	}

	private void adjustNumberOfStaffMembersTo(int halfStaffPoints) {
		this.gameBoard.getPlayers().stream()
				.filter(player -> player.getStaffAvailable() < halfStaffPoints)
				.forEach(player -> player.setStaffAvailable(halfStaffPoints));
	}

	private static int getNumberOfNonControlledPortal(List<Studio> studios) {
		return (int) studios.stream().filter(faction -> faction == null).count();
	}

	private static int getNumberOfControlledPortal(List<Studio> studios, Player player) {
		return (int) studios.stream().map(Studio::getCurrentFaction)
				.filter(faction -> faction == player.getFaction()).count();
	}
}
