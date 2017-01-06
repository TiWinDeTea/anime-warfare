package org.tiwindetea.animewarfare.logic.capacity.events;

import java.util.EventListener;

public interface BadBookCapacityChoseEventListener extends EventListener {
	void handleCapacityChose(BadBookCapacityChoseEvent event);
}
