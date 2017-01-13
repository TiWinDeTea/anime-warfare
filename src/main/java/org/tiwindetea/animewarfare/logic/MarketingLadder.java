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

import org.tiwindetea.animewarfare.logic.events.MarketingLadderUpdatedEvent;

/*
 * @author Benoît CORTIER
 */
public class MarketingLadder {
	public static final int[] COSTS;

	static {
		COSTS = new int[8];
		for (int i = 0; i < 2; ++i) {
			COSTS[i] = 6;
		}

		for (int i = 2; i < 4; ++i) {
			COSTS[i] = 7;
		}

		for (int i = 4; i < 6; ++i) {
			COSTS[i] = 8;
		}

		COSTS[6] = 9;
		COSTS[7] = 10;
	}

	private int currentPosition;

	public MarketingLadder() {
	}

	public int getCurrentCost() {
		if (isAtMax()) {
			throw new IllegalStateException("Cannot return the cost, ladder already at max.");
		}

		return this.COSTS[this.currentPosition];
	}

	public int getCurrentPosition() {
		return this.currentPosition;
	}

	public void incrementPosition() {
		this.currentPosition++;
		LogicEventDispatcher.getInstance()
		                    .fire(new MarketingLadderUpdatedEvent(this.currentPosition,
				                    this.COSTS.length,
				                    this.COSTS[this.currentPosition]));
	}

	public boolean isAtMax() {
		return this.currentPosition >= this.COSTS.length;
	}
}
