package org.tiwindetea.animewarfare.logic.states.events;

import java.util.EventListener;

public interface ConventionOrganizedEventListener extends EventListener {
	void onConventionOrganized(ConventionOrganizedEvent event);
}
