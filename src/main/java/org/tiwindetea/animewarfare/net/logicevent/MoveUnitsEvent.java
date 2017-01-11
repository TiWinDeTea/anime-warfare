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
import java.util.Objects;
import java.util.Set;

public class MoveUnitsEvent extends ActionEvent<MoveUnitsEventListener> {
	private final Set<Movement> movements;

	public static class Movement {
		private final int unitID;
		private final int sourceZone;
		private final int destinationZone;

		// kryo
		public Movement() {
			this.unitID = 0;
			this.sourceZone = 0;
			this.destinationZone = 0;
		}

		public Movement(int unitID, int sourceZone, int destinationZone) {
			this.unitID = unitID;
			this.sourceZone = sourceZone;
			this.destinationZone = destinationZone;
		}

		public int getUnitID() {
			return this.unitID;
		}

		public int getSourceZone() {
			return this.sourceZone;
		}

		public int getDestinationZone() {
			return this.destinationZone;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Movement movement = (Movement) o;
			return this.unitID == movement.unitID;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.unitID);
		}
	}

	public MoveUnitsEvent(int playerID,
						  Set<Movement> movements) {
		super(playerID);
		this.movements = Collections.unmodifiableSet(movements);
	}

	@Override
	public void notify(MoveUnitsEventListener listener) {
		listener.handleMoveUnitsEvent(this);
	}

	/**
	 * @return an unmodifiable set as specified in {@link Collections#unmodifiableSet(Set)}
	 */
	public Set<Movement> getMovements() {
		return this.movements;
	}
}
