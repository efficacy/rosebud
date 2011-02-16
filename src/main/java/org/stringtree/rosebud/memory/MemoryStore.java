package org.stringtree.rosebud.memory;

import java.util.HashMap;
import java.util.Map;

import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;

public class MemoryStore implements Store {
	
	Map<String, Entity> entities;
	
	public MemoryStore() {
		entities = new HashMap<String, Entity>();
	}

	@Override
	public void delete(String id) {
		entities.remove(id);
	}

	@Override
	public Entity get(String id) {
		return entities.get(id);
	}

	@Override
	public void put(Entity entity) {
		if (null != entity && !entity.isEmpty()) {
			String key = entity.getId();
			Entity old = entities.get(key);
			if (null != old) {
				MutableEntity merged = new MutableEntity(old);
				merged.merge(entity);
				entity = merged;
			}
			entities.put(key, entity);
		}
	}

	@Override
	public void clear() {
		entities.clear();
	}

}
