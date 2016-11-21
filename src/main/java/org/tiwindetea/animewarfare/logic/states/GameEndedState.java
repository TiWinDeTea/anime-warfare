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

import org.lomadriel.lfc.event.EventDispatcher;
import org.lomadriel.lfc.statemachine.State;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;

public class GameEndedState extends GameState {
	private static final String END_GAME_EXCEPTION = "End game event should be catched";

	private final int winner;

	public GameEndedState(int winner, GameBoard gameBoard) {
		super(gameBoard);
		this.winner = winner;
	}

	@Override
	public void onEnter() {
		EventDispatcher.getInstance().fire(new GameEndedEvent(Integer.valueOf(this.winner)));
	}

	@Override
	public void update() {
		throw new UnsupportedOperationException(END_GAME_EXCEPTION);
	}

	@Override
	public void onExit() {
		throw new UnsupportedOperationException(END_GAME_EXCEPTION);
	}

	@Override
	public State next() {
		throw new UnsupportedOperationException(END_GAME_EXCEPTION);
	}
}
