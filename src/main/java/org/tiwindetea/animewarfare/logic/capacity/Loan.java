package org.tiwindetea.animewarfare.logic.capacity;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.event.StaffPointUpdatedEvent;
import org.tiwindetea.animewarfare.logic.event.StaffPointUpdatedEventListener;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEventListener;

public class Loan extends PlayerCapacity {
	public static class LoanActivable extends PlayerActivable implements StudioControllerChangedEventListener, StaffPointUpdatedEventListener {
		private int controlledStudioCount;

		public LoanActivable(Player player) {
			super(player);

			EventDispatcher.registerListener(StudioControllerChangedEvent.class, this);
			EventDispatcher.registerListener(StaffPointUpdatedEvent.class, this);
		}

		@Override
		public void destroy() {
			EventDispatcher.unregisterListener(StudioControllerChangedEvent.class, this);
			EventDispatcher.unregisterListener(StaffPointUpdatedEvent.class, this);
		}

		@Override
		public void handleStudioController(StudioControllerChangedEvent event) {
			if (this.player.hasFaction(event.getControllerFaction())) {
				++this.controlledStudioCount;
			}

			if (this.controlledStudioCount == 4) {
				this.player.activateCapacity(new Loan(this.player));
			}
		}

		@Override
		public void onStaffPointChange(StaffPointUpdatedEvent event) {
			if (this.player.getID() == event.getPlayerID() && event.getStaffAvailable() >= 10) {
				this.player.activateCapacity(new Loan(this.player));
			}
		}
	}

	private Loan(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}
}
