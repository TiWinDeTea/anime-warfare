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

package org.tiwindetea.animewarfare.gui.game;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNetevent;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNeteventListener;

/**
 * @author Benoît CORTIER
 */
public class PlayerTurnMonitor implements NextPlayerNeteventListener, FirstPlayerSelectedNeteventListener {
	private static final PlayerTurnMonitor MONITOR = new PlayerTurnMonitor();

	public static GameClientInfo getCurrentPhase() {
		return PlayerTurnMonitor.MONITOR.currentPlayer;
	}

	public static void init() {
	}

	private GameClientInfo currentPlayer = null;

	private PlayerTurnMonitor() {
		EventDispatcher.registerListener(NextPlayerNetevent.class, this);
		EventDispatcher.registerListener(FirstPlayerSelectedNetevent.class, this);
	}

	@Override
	public void handleNextPlayer(NextPlayerNetevent event) {
		this.currentPlayer = event.getGameClientInfo();
	}

	@Override
	public void handlePlayerSelection(FirstPlayerSelectedNetevent firstPlayerSelectedNetevent) {
		this.currentPlayer = firstPlayerSelectedNetevent.getGameClientInfo();
	}
}
