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
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangedNeteventListener;

/**
 * @author Benoît CORTIER
 */
public class GamePhaseMonitor implements PhaseChangedNeteventListener {
	private static final GamePhaseMonitor MONITOR = new GamePhaseMonitor();

	public static PhaseChangedEvent.Phase getCurrentPhase() {
		return GamePhaseMonitor.MONITOR.currentPhase;
	}

	public static void init() {
	}

	private PhaseChangedEvent.Phase currentPhase = PhaseChangedEvent.Phase.STAFF_HIRING;

	private GamePhaseMonitor() {
		EventDispatcher.registerListener(PhaseChangeNetevent.class, this);
	}

	@Override
	public void handlePhaseChanged(PhaseChangeNetevent event) {
		this.currentPhase = event.getPhase();
	}
}
