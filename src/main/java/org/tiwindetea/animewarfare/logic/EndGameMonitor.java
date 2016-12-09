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

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.event.GameEndConditionsReachedEvent;
import org.tiwindetea.animewarfare.logic.event.MarketingLadderUpdatedEvent;
import org.tiwindetea.animewarfare.logic.event.MarketingLadderUpdatedEventListener;
import org.tiwindetea.animewarfare.logic.event.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.event.NumberOfFansChangedEventListener;

/*
 * @author Benoît CORTIER
 */
public class EndGameMonitor implements NumberOfFansChangedEventListener, MarketingLadderUpdatedEventListener {
	// TODO: externalize
	private final int fansEnd = 30;

	public EndGameMonitor() {
		EventDispatcher.getInstance().addListener(NumberOfFansChangedEvent.class, this);
		EventDispatcher.getInstance().addListener(MarketingLadderUpdatedEvent.class, this);
	}

	public void destroy() {
		EventDispatcher.getInstance().removeListener(NumberOfFansChangedEvent.class, this);
		EventDispatcher.getInstance().removeListener(MarketingLadderUpdatedEvent.class, this);
	}

	@Override
	public void handleNumberOfFansChanged(NumberOfFansChangedEvent event) {
		if (event.getPlayer().getFanNumber() >= this.fansEnd) {
			EventDispatcher.getInstance().fire(new GameEndConditionsReachedEvent());
		}
	}

	@Override
	public void handleMarketingLadderUpdated(MarketingLadderUpdatedEvent event) {
		if (event.getNewPosition() >= event.getEndPosition()) {
			EventDispatcher.getInstance().fire(new GameEndConditionsReachedEvent());
		}
	}
}
