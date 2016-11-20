package org.tiwindetea.animewarfare.logic;

import org.tiwindetea.animewarfare.logic.units.Entity;

import java.util.ArrayList;
import java.util.List;

public class Zone {
	private boolean isCountrySide;
	private final List<Entity> entities = new ArrayList<>();

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public boolean remove(Entity entity) {
		return this.entities.remove(entity);
	}
}
