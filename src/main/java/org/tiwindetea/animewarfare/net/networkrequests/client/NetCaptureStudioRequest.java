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

package org.tiwindetea.animewarfare.net.networkrequests.client;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class NetCaptureStudioRequest implements NetSendable {

	private final int zoneID;
	private final int mascotId;

	/**
	 * Default constructor, required by kryonet
	 */
	public NetCaptureStudioRequest() {
		this.zoneID = 0;
		this.mascotId = 0;
	}

	/**
	 * @param zoneID   Zone where you want to capture a studio
	 * @param mascotId Unit that capture the studio.
	 */
	public NetCaptureStudioRequest(int zoneID, int mascotId) {
		this.zoneID = zoneID;
		this.mascotId = mascotId;
	}

	public int getZoneID() {
		return this.zoneID;
	}

	public int getMascotId() {
		return this.mascotId;
	}
}
