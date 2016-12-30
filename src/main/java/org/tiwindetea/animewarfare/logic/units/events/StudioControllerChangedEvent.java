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

package org.tiwindetea.animewarfare.logic.units.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;

public class StudioControllerChangedEvent implements Event<StudioControllerChangedEventListener> {
	private final int zoneID;
	private final FactionType controllerFaction;
	private final int controllerID;

	public StudioControllerChangedEvent(int zoneID, FactionType controllerFaction, int controllerID) {
		this.zoneID = zoneID;
		this.controllerFaction = controllerFaction;
		this.controllerID = controllerID;
	}

	@Override
	public void notify(StudioControllerChangedEventListener listener) {
		listener.handleStudioController(this);
	}

	/**
	 * @return the id of the zone where is the studio
	 */
	public int getZoneID() {
		return this.zoneID;
	}

	/**
	 * Returns the id of the controller or null if nobody controls the studio.
	 *
	 * @return the id of the controller or null if nobody controls the studio.
	 */
	public FactionType getControllerFaction() {
		return this.controllerFaction;
	}

	/**
	 * Returns the id of the unit controlling the studio or -1 if nobody controls the studio.
	 *
	 * @return the id of the unit controlling the studio or -1 if nobody controls the studio.
	 */
	public int getControllerID() {
		return this.controllerID;
	}
}
