package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.battle.BattleSide;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;

public class ClownArtist extends PlayerCapacity implements BattleEventListener {
	public static class ClownArtistActivable extends PlayerActivable implements StudioEventListener {
		private final GameMap map;

		private int numberOfControlledStudioInTown;
		private int numberOfStudioInTown;

		public ClownArtistActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(StudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioEvent.class, this);
		}

		@Override
		public void handleStudioAddedEvent(StudioEvent event) {
			Zone zone = this.map.getZone(event.getZoneID());

			if (!zone.isCountrySide()) {
				++this.numberOfStudioInTown;

				if (getPlayer().hasFaction(zone.getStudio().getCurrentFaction())) {
					++this.numberOfControlledStudioInTown;
				}

				if (this.numberOfControlledStudioInTown >= 3 || this.numberOfStudioInTown >= 4) {
					activateAndDestroy(new ClownArtist(getPlayer()));
				}
			}
		}

		@Override
		public void handleStudioRemovedEvent(StudioEvent event) {
			Zone zone = this.map.getZone(event.getZoneID());

			if (!zone.isCountrySide()) {
				--this.numberOfStudioInTown;

				if (getPlayer().hasFaction(zone.getStudio().getCurrentFaction())) {
					--this.numberOfControlledStudioInTown;
				}
			}
		}
	}

	private static final int COST = 2;
	private BattleSide enemy;

	ClownArtist(Player player) {
		super(player);

		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	@Override
	public void use() {
		if (!getPlayer().hasRequiredStaffPoints(COST) || this.enemy == null) {
			return;
		}

		this.enemy.setNumberOfDices(this.enemy.getNumberOfDices());
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		if (event.getBattleContext().getAttacker().equals(getPlayer())) {
			this.enemy = event.getBattleContext().getDefender();
		} else if (event.getBattleContext().getDefender().equals(getPlayer())) {
			this.enemy = event.getBattleContext().getAttacker();
		}
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
		// Nothing to do
	}

	@Override
	public CapacityName getName() {
		return CapacityName.CLOWN_ARTIST;
	}
}
