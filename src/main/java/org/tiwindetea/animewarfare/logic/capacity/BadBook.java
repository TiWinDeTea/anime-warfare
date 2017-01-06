package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;
import org.tiwindetea.animewarfare.net.logicevent.BadBookCapacityChoseEvent;
import org.tiwindetea.animewarfare.net.logicevent.BadBookCapacityChoseEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BadBook extends PlayerCapacity implements BadBookCapacityChoseEventListener {
	public static class BadBookActivable extends PlayerActivable implements StudioEventListener {
		private int studioCounter;

		public BadBookActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(StudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioEvent.class, this);
		}

		@Override
		public void handleStudioAddedEvent(StudioEvent event) {
			if (++this.studioCounter == 8) {
				activateAndDestroy(new BadBook(getPlayer()));
			}
		}

		@Override
		public void handleStudioRemovedEvent(StudioEvent studioEvent) {
			--this.studioCounter;
		}
	}

	private final static int COST = 1;
	private final List<CapacityName> capacities = new ArrayList<>();

	BadBook(Player player) {
		super(player);
	}

	@Override
	public void use() {
		if (getPlayer().getActivatedCapacities().size() == 6 || !getPlayer().hasRequiredStaffPoints(COST)) {
			return;
		}

		LogicEventDispatcher.registerListener(BadBookCapacityChoseEvent.class, this);
		getPlayer().decrementStaffPoints(COST);
	}

	@Override
	public void handleCapacityChose(BadBookCapacityChoseEvent event) {
		LogicEventDispatcher.unregisterListener(BadBookCapacityChoseEvent.class, this);
		getPlayer().desactivateCapactiy(event.getName());

		CapacityName type;
		Random random = new Random();
		do {
			type = this.capacities.get(random.nextInt(this.capacities.size()));
		} while (getPlayer().hasCapacity(type));

		// TODO
	}

	@Override
	public CapacityName getName() {
		return CapacityName.BAD_BOOK;
	}
}
