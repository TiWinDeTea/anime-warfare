package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEventListener;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEventListener;

import java.util.ArrayList;
import java.util.List;

public class HimejiAttackBuff extends Buff implements StudioControllerChangedEventListener, UnitCounterEventListener, UnitMovedEventListener {
	private final List<Integer> controlledStudio = new ArrayList<>();
	private final BuffMask himejiAttackBuff = new BuffMask();

	public HimejiAttackBuff() {
		super(-1);

		LogicEventDispatcher.registerListener(StudioControllerChangedEvent.class, this);
		LogicEventDispatcher.registerListener(UnitCounterEvent.class, this);
		LogicEventDispatcher.registerListener(UnitMovedEvent.class, this);
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(StudioControllerChangedEvent.class, this);
		LogicEventDispatcher.unregisterListener(UnitCounterEvent.class, this);
		LogicEventDispatcher.unregisterListener(UnitMovedEvent.class, this);
	}

	@Override
	public void handleStudioController(StudioControllerChangedEvent event) {
		if (event.getControllerFaction() == UnitType.HIMEJI_MIZUKI.getDefaultFaction()) {
			++(this.himejiAttackBuff.attackPoints);
			this.controlledStudio.add(Integer.valueOf(event.getZoneID()));
		} else if (event.getControllerFaction() == null && this.controlledStudio.contains(event.getZoneID())) {
			--(this.himejiAttackBuff.attackPoints);
			this.controlledStudio.remove(Integer.valueOf(event.getZoneID()));
		}
	}

	@Override
	public void handleUnitEvent(UnitCounterEvent event) {
		if (event.getFaction() == UnitType.HIMEJI_MIZUKI.getDefaultFaction()
				&& event.getUnitType().isLevel(UnitLevel.MASCOT)) {
			if (event.getType() == UnitCounterEvent.Type.ADDED) {
				++(this.himejiAttackBuff.attackPoints);
			} else {
				--(this.himejiAttackBuff.attackPoints);
			}
		}
	}

	@Override
	public void handleUnitMoved(UnitMovedEvent event) {
		if (event.getSource() == null && event.getUnit().getType() == UnitType.HIMEJI_MIZUKI) {
			event.getUnit().getUnitBuffedCharacteristics().addBuffMask(this.himejiAttackBuff);
		}
	}
}
