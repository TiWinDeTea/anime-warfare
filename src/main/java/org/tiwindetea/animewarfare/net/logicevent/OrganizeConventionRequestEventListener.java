package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface OrganizeConventionRequestEventListener extends EventListener {
	void handleOrganizeConventionRequest(OrganizeConventionRequestEvent event);
}
