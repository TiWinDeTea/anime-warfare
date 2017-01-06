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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * @author Benoît CORTIER
 */
public class AdvertisingCampaignRightsPool {
	private static final Map<Integer, Integer> NUMBER_OF_RIGHTS_BY_WEIGHTS = new HashMap<>();

	static {
		// TODO: externalize
		NUMBER_OF_RIGHTS_BY_WEIGHTS.put(1, 18);
		NUMBER_OF_RIGHTS_BY_WEIGHTS.put(2, 12);
		NUMBER_OF_RIGHTS_BY_WEIGHTS.put(3, 6);
	}

	private final List<AdvertisingCampaignRight> pool = new LinkedList<>();

	public AdvertisingCampaignRightsPool() {
		// create the advertising campaign rights.
		List<AdvertisingCampaignRight> advertisingCampaignRights = new ArrayList<>();
		for (Map.Entry<Integer, Integer> integerIntegerEntry : NUMBER_OF_RIGHTS_BY_WEIGHTS.entrySet()) {
			for (int i = 0; i < integerIntegerEntry.getValue(); i++) {
				advertisingCampaignRights.add(new AdvertisingCampaignRight(integerIntegerEntry.getKey()));
			}
		}

		// shuffle the rights and add them to the pool.
		Collections.shuffle(advertisingCampaignRights);
		this.pool.addAll(advertisingCampaignRights);
	}

	public AdvertisingCampaignRight getAdvertisingCampaignRight() {
		// TODO: fire an event when it become empty?
		AdvertisingCampaignRight right = this.pool.get(0);
		return right;
	}

	public void returnAdvertisingCampaignRight(AdvertisingCampaignRight right) {
		this.pool.add(right);
		Collections.shuffle(this.pool);
	}

	public boolean isEmpty() {
		return this.pool.isEmpty();
	}
}
