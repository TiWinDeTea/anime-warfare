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
import org.tiwindetea.animewarfare.logic.AdvertisingCampaignRightsPool;
import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.MarketingLadder;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.states.events.ConventionOrganizedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEventListener;
import org.tiwindetea.animewarfare.logic.states.events.NextPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.FinishTurnRequestEventListener;
import org.tiwindetea.animewarfare.net.logicevent.OrganizeConventionRequestEvent;
import org.tiwindetea.animewarfare.net.logicevent.OrganizeConventionRequestEventListener;

/**
 * Marketing phase of the game
 * @author Benoît CORTIER
 */
class MarketingState extends GameState
		implements OrganizeConventionRequestEventListener, FinishTurnRequestEventListener,
		GameEndedEventListener {
	private Player currentPlayer;

	private State nextState = this;

	MarketingState(GameBoard gameBoard) {
		super(gameBoard);
	}

	@Override
	public void onEnter() {
		LogicEventDispatcher.getInstance().fire(new PhaseChangedEvent(PhaseChangedEvent.Phase.MARKETING));

		// TODO: differ in long games.
		for (Player player : this.gameBoard.getPlayers()) {
			player.incrementFans(
					(int) this.gameBoard.getMap().getStudios().stream()
					                    .filter(studio -> player.hasFaction(studio.getCurrentFaction()))
					                    .count()
			);
		}

		this.currentPlayer = this.gameBoard.getFirstPlayer();
		LogicEventDispatcher.send(new NextPlayerEvent(this.currentPlayer.getID()));

		registerEventListeners();
	}

	@Override
	public void update() {
		this.currentPlayer = this.currentPlayer.getNextPlayerInGameOrder();
		if (this.currentPlayer != this.gameBoard.getFirstPlayer()) {
			LogicEventDispatcher.send(new NextPlayerEvent(this.currentPlayer.getID()));
		} else {
			this.nextState = new ActionState(this.gameBoard);
		}
	}

	@Override
	public void onExit() {
		unregisterEventListeners();
	}

	@Override
	public State next() {
		return this.nextState;
	}

	private void registerEventListeners() {
		LogicEventDispatcher.getInstance().addListener(OrganizeConventionRequestEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(FinishTurnRequestEvent.class, this);
		LogicEventDispatcher.getInstance().addListener(GameEndedEvent.class, this);
	}

	private void unregisterEventListeners() {
		LogicEventDispatcher.getInstance().removeListener(OrganizeConventionRequestEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(FinishTurnRequestEvent.class, this);
		LogicEventDispatcher.getInstance().removeListener(GameEndedEvent.class, this);
	}

	@Override
	public void handleOrganizeConventionRequest(OrganizeConventionRequestEvent event) {
		if (event.getPlayerID() == this.currentPlayer.getID()) {
			MarketingLadder marketingLadder = this.gameBoard.getMarketingLadder();
			if (this.currentPlayer.getStaffAvailable() >= marketingLadder.getCurrentCost()) {
				this.currentPlayer.decrementStaffPoints(marketingLadder.getCurrentCost());
				marketingLadder.incrementPosition();

				AdvertisingCampaignRightsPool pool = this.gameBoard.getAdvertisingCampaignRightsPool();
				if (!pool.isEmpty()) {
					this.currentPlayer.addAdvertisingCampaignRights(pool.getAdvertisingCampaignRight());
					this.currentPlayer.incrementFans(1);
				} else {
					this.currentPlayer.incrementFans(2);
				}

				LogicEventDispatcher.getInstance().fire(new ConventionOrganizedEvent(this.currentPlayer));

				this.machine.get().update();
			}
		}
	}

	@Override
	public void handleFinishTurnRequest(FinishTurnRequestEvent event) {
		this.machine.get().update();
	}

	@Override
	public void handleGameEndedEvent(GameEndedEvent gameEndedEvent) {
		this.nextState = new GameEndedState(this.gameBoard);

		this.machine.get().update();
	}
}
