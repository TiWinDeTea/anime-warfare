package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.buffs.HidingInTheBushBuff;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.logicevent.HidingInTheBushUnitsChoiceEvent;
import org.tiwindetea.animewarfare.net.logicevent.HidingInTheBushUnitsChoiceEventListener;

import java.util.HashSet;
import java.util.Set;

public class HidingInTheBush extends PlayerCapacity implements PhaseChangedEventListener, HidingInTheBushUnitsChoiceEventListener {
	public static class HidingInTheBushActivable extends PlayerActivable implements UnitCounterEventListener {
		private final GameMap map;

		public HidingInTheBushActivable(Player player, GameMap map) {
			super(player);
			this.map = map;

			LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		}

		@Override
		public void handleUnitEvent(UnitCounterEvent event) {
			if (event.getUnitType() == UnitType.CTHUKO) {
				activateAndDestroy(new HidingInTheBush(getPlayer(), this.map));
			}
		}
	}

	private final GameMap map;
	private Set<Integer> unitsID;

	private HidingInTheBush(Player player, GameMap map) {
		super(player);
		this.map = map;

		LogicEventDispatcher.registerListener(PhaseChangedEvent.class, this);
	}

	@Override
	public void use() {
		if (this.unitsID == null) {
			return;
		}

		Set<Unit> units = new HashSet<>();

		for (Zone zone : this.map.getZones()) {
			zone.getUnits().stream().filter(u -> this.unitsID.contains(u.getID())).forEach(units::add);
		}

		int cost = 0;
		for (Unit unit : units) {
			if (unit.isLevel(UnitLevel.HERO)) {
				cost += 2;
			} else {
				++cost;
			}
		}

		if (!getPlayer().hasRequiredStaffPoints(cost)) {
			return;
		}

		getPlayer().decrementStaffPoints(cost);
		getPlayer().getBuffManager().addBuff(new HidingInTheBushBuff(units));
	}

	@Override
	public void handlePhaseChanged(PhaseChangedEvent event) {
		if (event.getNewPhase() == PhaseChangedEvent.Phase.ACTION) {
			LogicEventDispatcher.registerListener(HidingInTheBushUnitsChoiceEvent.class, this);
		} else {
			LogicEventDispatcher.unregisterListener(HidingInTheBushUnitsChoiceEvent.class, this);
		}
	}

	@Override
	public void handleUnitsChoiceEvent(HidingInTheBushUnitsChoiceEvent event) {
		this.unitsID = event.getUnits();
	}

	@Override
	public CapacityName getName() {
		return CapacityName.HIDING_BUSH;
	}
}
