package org.tiwindetea.animewarfare.net.logicevent;

import java.util.EventListener;

public interface CaptureMascotEventListener extends EventListener {
	void handleCaptureMascotEvent(CaptureMascotEvent event);
}
