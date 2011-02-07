package org.stringtree.rosebud.memory;

import java.util.HashMap;
import java.util.Map;

import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.Store;

public class MemoryStore implements Store {
	
	Map<String, Entity> entities;
	
	public MemoryStore() {
		entities = new HashMap<String, Entity>();
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Entity get(String id) {
		return entities.get(id);
	}

	@Override
	public void put(Entity entity) {
		if (null != entity && !entity.isEmpty()) {
			entities.put(entity.getId(), entity);
		}
	}

}
