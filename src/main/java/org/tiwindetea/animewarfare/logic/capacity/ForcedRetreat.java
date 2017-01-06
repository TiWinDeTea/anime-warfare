package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

public class ForcedRetreat extends PlayerCapacity {
	public static class ForcedRetreatActivable extends PlayerActivable implements UnitMovedEventListener {
		public ForcedRetreatActivable(Player player) {
			super(player);
		}

		@Override
		public void destroy() {
		}

		@Override
		public void handleUnitMoved(UnitMovedEvent event) {
			if (event.getUnit().isLevel(UnitLevel.HERO)) {
				if (isHeroInTheSameZoneThanAnEnnemyHero(event)) {
					activateAndDestroy(new ForcedRetreat(getPlayer()));
				}
			}
		}

		private boolean isHeroInTheSameZoneThanAnEnnemyHero(UnitMovedEvent event) {
			boolean ennemyPresent = false;
			boolean heroPresent = false;

			for (Unit unit : event.getDestination().getUnits()) {
				if (unit.isLevel(UnitLevel.HERO)) {
					if (unit.hasFaction(getPlayer().getFaction())) {
						heroPresent = true;
					} else {
						ennemyPresent = true;
					}
				}
			}

			return ennemyPresent && heroPresent;
		}
	}

	ForcedRetreat(Player player) {
		super(player);
	}

	@Override
	public void use() {
	}

	@Override
	public CapacityName getName() {
		return CapacityName.FORCED_RETREAT;
	}
}
