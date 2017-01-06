package org.tiwindetea.animewarfare.net.networkrequests.client;

import org.tiwindetea.animewarfare.logic.capacity.CapacityName;

public class NetUseCapacityRequest implements NetSendable {
	private final CapacityName capacityName;

	public NetUseCapacityRequest(CapacityName capacityName) {
		this.capacityName = capacityName;
	}

	public CapacityName getName() {
		return this.capacityName;
	}
}
