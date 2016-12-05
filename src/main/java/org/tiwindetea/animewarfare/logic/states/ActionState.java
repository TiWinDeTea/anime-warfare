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

import java.util.LinkedList;
import java.util.List;

public class ActionState extends GameState {
	private List<Player> players;
	private boolean gameEnded;
	private boolean phaseEnded;
	private final List<Integer> winners = new LinkedList<>();

	private Player currentPlayer;
	private int currentPlayerPosition;

	private Player huntedPlayer;
	private Zone huntingZone;

	public ActionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		this.players = this.gameBoard.getPlayersInOrder();
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
		if (this.gameEnded) {
			return new GameEndedState(this.winners, this.gameBoard);
		} else if (this.phaseEnded) {
			return new StaffHiringState(this.gameBoard);
		} else {
			return this;
		}
	}
}
