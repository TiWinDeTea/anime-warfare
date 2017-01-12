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

package org.tiwindetea.animewarfare.net.logicevent;

import org.tiwindetea.animewarfare.logic.units.UnitType;

public class CaptureUnitRequestEvent extends ActionEvent<CaptureUnitRequestEventListener> {
	private final int huntedPlayerID;
	private final int zone;
	private final UnitType unitType;

	public CaptureUnitRequestEvent(int playerID, int huntedPlayerID, int zone, UnitType unitType) {
		super(playerID);

		if (playerID == huntedPlayerID) {
			throw new IllegalArgumentException("The hunter can't be the hunted player.");
		}

		this.huntedPlayerID = huntedPlayerID;
		this.zone = zone;
		this.unitType = unitType;
	}

	@Override
	public void notify(CaptureUnitRequestEventListener listener) {
		listener.handleCaptureUnitRequestEvent(this);
	}

	public int getHuntedPlayerID() {
		return this.huntedPlayerID;
	}

	public int getZone() {
		return this.zone;
	}

	public UnitType getUnitType() {
		return this.unitType;
	}
}
