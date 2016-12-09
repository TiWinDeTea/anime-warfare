package org.tiwindetea.animewarfare.net.logicevent;

public class StartBattleEvent extends ActionEvent<StartBattleEventListener> {
	private final int defender;
	private final int zone;

	public StartBattleEvent(int attacker, int defender, int zone) {
		super(attacker);
		this.defender = defender;
		this.zone = zone;
	}

	@Override
	public void notify(StartBattleEventListener listener) {
		listener.handleBattleEvent(this);
	}

	public int getAttackerID() {
		return getPlayerID();
	}

	public int getDefenderID() {
		return this.defender;
	}

	public int getZone() {
		return this.zone;
	}
}
