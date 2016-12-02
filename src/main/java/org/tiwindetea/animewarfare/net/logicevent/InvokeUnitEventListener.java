package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface InvokeUnitEventListener extends EventListener {
	void handleInvokeUnitEvent(InvokeUnitEvent event);
}
