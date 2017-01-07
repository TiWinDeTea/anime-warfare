package org.tiwindetea.animewarfare.net.logicevent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HidingInTheBushUnitsChoiceEvent extends ActionEvent<HidingInTheBushUnitsChoiceEventListener> {
	private final Set<Integer> units = new HashSet<>();

	public HidingInTheBushUnitsChoiceEvent(int playerID, Set<Integer> units) {
		super(playerID);

		this.units.addAll(units);
	}

	@Override
	public void notify(HidingInTheBushUnitsChoiceEventListener listener) {
		listener.handleUnitsChoiceEvent(this);
	}

	public Set<Integer> getUnits() {
		return Collections.unmodifiableSet(this.units);
	}
}
