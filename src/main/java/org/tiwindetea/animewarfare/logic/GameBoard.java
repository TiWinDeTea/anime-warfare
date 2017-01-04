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

package org.tiwindetea.animewarfare.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard {
	private final GameMap gameMap;

	private final Map<Integer, Player> players = new HashMap<>();
	private final List<Player> playersInOrder = new ArrayList<>(); // Used to cache.

	private Player lastFirstPlayer;
	private int firstPlayerID;
	private boolean clockWiseRotationTurn;

	private AdvertisingCampaignRightsPool advertisingCampaignRightsPool = new AdvertisingCampaignRightsPool();

	private final EndGameMonitor endGameMonitor = new EndGameMonitor();

	private final MarketingLadder marketingLadder;

	public GameBoard(Map<Integer, FactionType> players) {
		if (players.size() < 2 && players.size() > 4) {
			throw new IllegalArgumentException("Incorrect number of players, "
					+ players.size()
					+ " player(s) requested, should be between 2 and 4");
		}

		initializePlayers(players);
		this.gameMap = new GameMap(this.players.size());
		this.marketingLadder = new MarketingLadder(players.size());
	}

	public int getLastFirstPlayerID() {
		return this.lastFirstPlayer.getID();
	}

	public Player getPlayer(int id) {
		return this.players.get(new Integer(id));
	}

	public Player getPlayer(FactionType factionType) {
		return this.players.values().stream()
		                   .filter(p -> p.hasFaction(factionType))
		                   .findFirst()
		                   .orElse(null);
	}

	public Collection<Player> getPlayers() {
		return Collections.unmodifiableCollection(this.players.values());
	}

	public Player selectRandomPlayer() {
		return this.players.get(new Random().nextInt(this.players.size()));
	}

	public List<Player> getPlayersInOrder() {
		return Collections.unmodifiableList(this.playersInOrder);
	}

	public static List<Integer> getPlayersIndex(List<Player> players) {
		return players.stream().map(player -> Integer.valueOf(player.getID())).collect(Collectors.toList());
	}

	public void initializeTurn(Player firstPlayer, boolean clockWiseRotationTurn) {
		this.lastFirstPlayer = getPlayer(this.firstPlayerID);
		this.firstPlayerID = firstPlayer.getID();
		this.clockWiseRotationTurn = clockWiseRotationTurn;

		buildPlayerList();
	}

	private void initializePlayers(Map<Integer, FactionType> players) {
		players.entrySet()
		       .stream()
		       .map(entry -> new Player(entry.getKey().intValue(), entry.getValue()))
		       .collect(Collectors.toMap(Player::getID, player -> player));
	}

	private void buildPlayerList() { // FIXME: Doesn't work (Linked list of players ??)
		this.playersInOrder.clear();
		if (this.clockWiseRotationTurn) {
			for (int i = 0, j = this.firstPlayerID; i < this.players.size(); ++i) {
				this.playersInOrder.add(this.players.get((i + j) % this.players.size()));
			}
		} else {
			for (int i = this.players.size(), j = this.firstPlayerID; i > 0; --i) {
				this.playersInOrder.add(this.players.get((i + j) % this.players.size()));
			}
		}
	}

	public GameMap getMap() {
		return this.gameMap;
	}

	public AdvertisingCampaignRightsPool getAdvertisingCampaignRightsPool() {
		return this.advertisingCampaignRightsPool;
	}

	public MarketingLadder getMarketingLadder() {
		return this.marketingLadder;
	}

	public void destroy() {
		this.endGameMonitor.destroy();
	}
}
