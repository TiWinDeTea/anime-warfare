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
import org.tiwindetea.animewarfare.logic.event.MarketingLadderUpdatedEvent;

/*
 * @author Benoît CORTIER
 */
public class MarketingLadder {
	private int[] costs;

	private int currentPosition = 0;

	public MarketingLadder(int numberOfPlayers) {
		// TODO: check rules about that:
		this.costs = new int[7 * numberOfPlayers];
		for (int i = 0; i < this.costs.length; i++) {
			this.costs[i] = 1 + (i / 2);
		}
	}

	public int getCurrentCost() {
		if (isAtMax()) {
			throw new RuntimeException("Cannot return the cost, ladder already at max.");
		}

		return this.costs[this.currentPosition];
	}

	public int getCurrentPosition() {
		return this.currentPosition;
	}

	public void incrementPosition() {
		this.currentPosition++;
		EventDispatcher.getInstance().fire(new MarketingLadderUpdatedEvent(this.currentPosition, this.costs.length));
	}

	public boolean isAtMax() {
		return this.currentPosition >= this.costs.length;
	}
}
