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
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.logicevent.FirstPlayerChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.FirstPlayerChoiceEventListener;
import org.tiwindetea.animewarfare.net.logicevent.PlayingOrderChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.PlayingOrderChoiceEventListener;

import java.util.List;

class FirstPlayerSelectionState extends GameState implements FirstPlayerChoiceEventListener, PlayingOrderChoiceEventListener {
	protected static final String TURN_NOT_INITIALIZED = "Turn not initialized"; // TODO: Externalize

	private List<Player> drawPlayers;

	protected Player firstPlayer;
	protected Boolean clockWise;

	protected FirstPlayerSelectionState(GameBoard gameBoard) {
		super(gameBoard);
	}

	FirstPlayerSelectionState(GameBoard gameBoard, List<Player> drawPlayers) {
		super(gameBoard);

		this.drawPlayers = drawPlayers;
	}

	@Override
	public void onEnter() {
		registerEventListeners();

		LogicEventDispatcher.getInstance().fire(new PhaseChangedEvent(PhaseChangedEvent.Phase.PLAYER_SELECTION));

		selectionFirstPlayer();
	}

	@Override
	public void update() {
		// Nothing to do.
	}

	@Override
	public void onExit() {
		unregisterEventListeners();
	}

	@Override
	public State next() {
		if (this.firstPlayer == null || this.clockWise == null) {
			throw new IllegalStateException(TURN_NOT_INITIALIZED);
		}

		return new MarketingState(this.gameBoard);
	}

	@Override
	public void handleFirstPlayer(FirstPlayerChoiceEvent event) {
		this.firstPlayer = this.gameBoard.getPlayer(event.getFirstPlayer());

		LogicEventDispatcher.getInstance().fire(new FirstPlayerSelectedEvent(this.firstPlayer.getID()));
	}

	@Override
	public void handlePlayingOrder(PlayingOrderChoiceEvent event) {
		this.clockWise = event.isClockWiseTurn();
		this.gameBoard.initializeTurn(this.firstPlayer, this.clockWise.booleanValue());

		this.machine.get().update();
	}

	protected void registerEventListeners() {
		LogicEventDispatcher.getInstance().addListener(PlayingOrderChoiceEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(FirstPlayerChoiceEvent.class, this);
	}

	private void unregisterEventListeners() {
		LogicEventDispatcher.getInstance().removeListener(PlayingOrderChoiceEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(FirstPlayerChoiceEvent.class, this);
	}

	private void selectionFirstPlayer() {
		if (this.drawPlayers.size() == 1) { // No draw
			this.firstPlayer = this.drawPlayers.get(0);
			LogicEventDispatcher.getInstance().fire(new FirstPlayerSelectedEvent(this.firstPlayer.getID()));
		} else {
			LogicEventDispatcher.getInstance().fire(new AskFirstPlayerEvent(this.gameBoard.getLastFirstPlayerID(),
					GameBoard.getPlayersIndex(this.drawPlayers)));
		}
	}
}
