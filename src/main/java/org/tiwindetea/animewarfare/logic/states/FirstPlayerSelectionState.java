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

import java.util.ArrayList;
import java.util.List;

public class FirstPlayerSelectionState extends GameState {
	protected Player firstPlayer;
	private Boolean clockWise;

	public FirstPlayerSelectionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		int maxStaff = 0;
		List<Player> maxPlayers = new ArrayList<>();

		for (Player player : this.gameBoard.getPlayers()) {
			if (player.getStaffAvailable() > maxStaff) {
				maxPlayers.clear();
				maxPlayers.add(player);
				maxStaff = player.getStaffAvailable();
			} else if (player.getStaffAvailable() == maxStaff) {
				maxPlayers.add(player);
			}
		}

		if (maxPlayers.size() == 1) {
			this.firstPlayer = maxPlayers.get(0);
			// TODO : Asks first player the playing order.
		} else {
			// TODO: Asks the last first player to choose the new first player.
		}
	}

	@Override
	public void update() {
		// Nothing to do.
	}

	@Override
	public void onExit() {
		// Nothing to do.
	}

	public State next() {
		if (this.firstPlayer == null || this.clockWise == null) {
			throw new RuntimeException("Turn not initialized");
		}

		return new MarketingState(this.gameBoard);
	}

	// TODO: Handle the first player
	// TODO: Handle the playing order
}
