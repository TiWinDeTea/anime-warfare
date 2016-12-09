package org.tiwindetea.animewarfare.net.logicevent;

import java.util.Collections;
import java.util.List;

public class MoveUnitEvent extends ActionEvent<MoveUnitEventListener> {
	private final List<Movement> movements;

	public static class Movement {
		private final int unitID;
		private final int sourceZone;
		private final int destinationZone;

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
	}

	public MoveUnitEvent(int playerID,
	                     List<Movement> movements) {
		super(playerID);
		this.movements = Collections.unmodifiableList(movements);
	}

	@Override
	public void notify(MoveUnitEventListener listener) {
		listener.handleMoveUnitEvent(this);
	}

	/**
	 * @return an unmodifiable list as specified in {@link Collections#unmodifiableList(List)}
	 */
	public List<Movement> getMovements() {
		return this.movements;
	}
}
