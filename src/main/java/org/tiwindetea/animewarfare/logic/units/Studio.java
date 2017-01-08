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

package org.tiwindetea.animewarfare.logic.units;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;

public class Studio {
	private final int zoneID;
	private FactionType currentFaction;
	private Unit controller;

	public Studio(int zoneID) {
		this.zoneID = zoneID;
	}

	private void setCurrentFaction(FactionType currentFaction) {
		this.currentFaction = currentFaction;
	}

	public FactionType getCurrentFaction() {
		return this.currentFaction;
	}

	public Unit getController() {
		return this.controller;
	}

	public void setController(Unit controller) {
		this.controller = controller;

		if (this.controller != null) {
			setCurrentFaction(controller.getFaction());

			EventDispatcher.send(new StudioControllerChangedEvent(this.zoneID,
					controller.getFaction(),
					controller.getID()));
		} else {
			setCurrentFaction(null);

			EventDispatcher.send(new StudioControllerChangedEvent(this.zoneID, null, -1));
		}
	}
}
