package org.stringtree.rosebud.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.stringtree.rosebud.Attribute;
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
				entity = new MutableEntity(old).merge(entity);
			}
			entities.put(key, entity);
		}
	}

	@Override
	public void put(Attribute attribute) {
		Entity entity = get(attribute.from);
		if (null == entity) {
			entity = new MutableEntity(attribute.from);
		}
		entity.setAttribute(attribute);
		put(entity);
	}

	@Override
	public void clear() {
		entities.clear();
	}

	@Override
	public Collection<String> match(Attribute pattern) {
		Collection<String> ret = new HashSet<String>();
		
		for (Entity entity : entities.values()) {
			for (Attribute candidate : entity) {
				if (matchAttribute(candidate, pattern)) {
					ret.add(candidate.from);
				}
			}
			
		}
		
		return ret;
	}
	
	private boolean matchAttribute(Attribute candidate, Attribute pattern) {
		boolean ret = true;
		
		if (null != pattern.from) ret &= pattern.from.equals(candidate.from);
		if (null != pattern.rel) ret &= pattern.rel.equals(candidate.rel);
		if (Attribute.NO_SEQ != pattern.seq) ret &= (pattern.seq == candidate.seq);
		if (null != pattern.to) ret &= pattern.to.equals(candidate.to);
		
		return ret;
	}
}
