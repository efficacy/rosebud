package org.stringtree.rosebud.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.CommonStore;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;

public class MemoryStore extends CommonStore implements Store {
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
			if (id.equals(attribute.src)) {
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
	public void add(Entity entity) {
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
	public Collection<Attribute> match(Attribute pattern) {
		Collection<Attribute> ret = new ArrayList<Attribute>();
		
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				ret.add(candidate);
			}
		}
		
		return ret;
	}

	@Override
	public Attribute matchOne(Attribute pattern) {
		Attribute ret = null;
		
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				ret = candidate;
				break;
			}
		}
		
		return ret;
	}

	@Override
	public Collection<String> find(Attribute pattern) {
		Collection<String> ret = new HashSet<String>();
		
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				String returnable = returnable(candidate, pattern);
				ret.add(returnable);
			}
		}
		
		return ret;
	}

	@Override
	public String findOne(Attribute pattern) {
		String ret = null;
		
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				ret = returnable(candidate, pattern);
				break;
			}
		}
		
		return ret;
	}

	@Override
	public boolean exists(Attribute pattern) {
		for (Attribute candidate : attributes) {
			if (matchAttribute(candidate, pattern)) {
				return true;
			}
		}
		
		return false;
	}
	
	private String returnable(Attribute candidate, Attribute pattern) {
		if (null == pattern.src) return candidate.src;
		if (null == pattern.rel) return candidate.rel;
		if (null == pattern.dest) return candidate.dest;
		if (null == pattern.seq) return candidate.seq;
		return candidate.src;
	}

	private boolean matchAttribute(Attribute candidate, Attribute pattern) {
		boolean ret = true;
		
		if (null != pattern.src) ret &= pattern.src.equals(candidate.src);
		if (null != pattern.rel) ret &= pattern.rel.equals(candidate.rel);
		if (null != pattern.seq) ret &= pattern.seq.equals(candidate.seq);
		if (null != pattern.dest) ret &= pattern.dest.equals(candidate.dest);
		
		return ret;
	}

	@Override
	public Iterator<Attribute> iterator() {
		return attributes.iterator();
	}
}
