package org.tiwindetea.animewarfare.logic.states.events;

import org.lomadriel.lfc.event.Event;

public class BattleStartedEvent implements Event<BattleStartedEventListener> {
	private final int attacker;
	private final int defensor;
	private final int zone;

	public BattleStartedEvent(int attacker, int defensor, int zone) {
		this.attacker = attacker;
		this.defensor = defensor;
		this.zone = zone;
	}

	@Override
	public void notify(BattleStartedEventListener listener) {
		listener.handleBattleStartedEvent(this);
	}

	public int getAttacker() {
		return this.attacker;
	}

	public int getDefensor() {
		return this.defensor;
	}

	public int getZone() {
		return this.zone;
	}
}
