package org.stringtree.rosebud.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;

public class MemoryStore implements Store {
	Collection<Attribute> attributes;
	
	public MemoryStore() {
		attributes = new ArrayList<Attribute>();
	}

	@Override
	public void delete(String id) {
		Collection<Attribute> found = findAll(id);
		for (Attribute attribute : found) {
			attributes.remove(attribute);
		}
	}

	private Collection<Attribute> findAll(String id) {
		Collection<Attribute> found = new ArrayList<Attribute>();
		for (Attribute attribute : attributes) {
			if (id.equals(attribute.from)) {
				found.add(attribute);
			}
		}
		return found;
	}

	@Override
	public Entity get(String id) {
		Collection<Attribute> found = findAll(id);
		return found.isEmpty() ? null : new MutableEntity(id, found);
	}

	@Override
	public void put(Entity entity) {
		delete(entity.getId());
		for (Attribute attribute : entity) {
			attributes.add(attribute);
		}
	}

	@Override
	public void put(Attribute attribute) {
		attributes.add(attribute);
	}

	@Override
	public void clear() {
		attributes.clear();
	}

	@Override
	public Collection<String> match(Attribute pattern) {
		Collection<String> ret = new HashSet<String>();
		
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				ret.add(returnable(candidate, pattern));
			}
		}
		
		return ret;
	}
	
	private String returnable(Attribute candidate, Attribute pattern) {
		if (null == pattern.from) return candidate.from;
		if (null == pattern.rel) return candidate.rel;
		if (null == pattern.to) return candidate.to;
		return candidate.from;
	}

	private boolean matchAttribute(Attribute candidate, Attribute pattern) {
		boolean ret = true;
		
		if (null != pattern.from) ret &= pattern.from.equals(candidate.from);
		if (null != pattern.rel) ret &= pattern.rel.equals(candidate.rel);
		if (Attribute.NO_SEQ != pattern.seq) ret &= (pattern.seq == candidate.seq);
		if (null != pattern.to) ret &= pattern.to.equals(candidate.to);
		
		return ret;
	}

	@Override
	public Iterator<Attribute> iterator() {
		return null;
	}
}
