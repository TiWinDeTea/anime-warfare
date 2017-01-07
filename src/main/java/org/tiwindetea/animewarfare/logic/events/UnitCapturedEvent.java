package org.tiwindetea.animewarfare.logic.events;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.units.UnitType;

public class UnitCapturedEvent implements Event<UnitCapturedEventListener> {
	private final Player hunter;
	private final Player hunted;
	private final FactionType capturedUnitFaction;
	private final UnitType capturedUnitType;

	public UnitCapturedEvent(Player hunter, Player hunted, FactionType capturedUnitFaction, UnitType capturedUnitType) {
		this.hunter = hunter;
		this.hunted = hunted;
		this.capturedUnitFaction = capturedUnitFaction;
		this.capturedUnitType = capturedUnitType;
	}

	@Override
	public void notify(UnitCapturedEventListener listener) {
		listener.onUnitCaptured(this);
	}

	public Player getHunter() {
		return this.hunter;
	}

	public Player getHuntedPlayer() {
		return this.hunted;
	}

	public FactionType getCapturedUnitFaction() {
		return this.capturedUnitFaction;
	}

	public UnitType getCapturedUnitType() {
		return this.capturedUnitType;
	}
}
