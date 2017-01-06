package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;

public class BadLuck extends PlayerCapacity {
	public static class BadLuckActivable extends PlayerActivable implements BattleEventListener {
		public BadLuckActivable(Player player) {
			super(player);

			LogicEventDispatcher.registerListener(BattleEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
		}

		@Override
		public void handlePreBattle(BattleEvent event) {
			// Nothing to do
		}

		@Override
		public void handleDuringBattle(BattleEvent event) {
			// Nothing to do
		}

		@Override
		public void handlePostBattle(BattleEvent event) {
			// Nothing to do
		}

		@Override
		public void handleBattleFinished(BattleEvent event) {
			int deadCount = 0;
			if (event.getBattleContext().getAttacker().getPlayer().equals(getPlayer())) {
				deadCount = event.getBattleContext().getAttacker().getDeads();
			} else if (event.getBattleContext().getDefender().getPlayer().equals(getPlayer())) {
				deadCount = event.getBattleContext().getDefender().getDeads();
			}

			if (deadCount > 0) {
				activateAndDestroy(new BadLuck(getPlayer()));
			}
		}
	}

	BadLuck(Player player) {
		super(player);
	}

	@Override
	public void use() {
		// TODO
	}

	@Override
	public CapacityName getName() {
		return CapacityName.BAD_LUCK;
	}
}
