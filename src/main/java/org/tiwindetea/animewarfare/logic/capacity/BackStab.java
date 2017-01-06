package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEventListener;

public class BackStab extends PlayerCapacity {
	public static class BackStabActivable extends PlayerActivable implements FirstPlayerSelectedEventListener {
		public BackStabActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(FirstPlayerSelectedEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(FirstPlayerSelectedEvent.class, this);
		}

		@Override
		public void firstPlayerSelected(FirstPlayerSelectedEvent event) {
			if (getPlayer().getID() == event.getFirstPlayer()) {
				activateAndDestroy(new BackStab(getPlayer()));
			}
		}
	}

	BackStab(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}

	@Override
	public CapacityName getName() {
		return CapacityName.BACK_STAB;
	}
}
