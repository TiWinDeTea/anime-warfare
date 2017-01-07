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

import java.util.Collections;
import java.util.Set;

/**
 * @author Benoît CORTIER
 */
public class SelectUnitsEvent extends ActionEvent<SelectUnitsEventListener> {
	private final Set<Integer> units;

	public SelectUnitsEvent(int playerID, Set<Integer> units) {
		super(playerID);
		this.units = Collections.unmodifiableSet(units);
	}

	@Override
	public void notify(SelectUnitsEventListener listener) {
		listener.handleSelectUnits(this);
	}

	/**
	 * @return an unmodifiable set as specified in {@link Collections#unmodifiableSet(Set)}
	 */
	public Set<Integer> getUnits() {
		return this.units;
	}
}