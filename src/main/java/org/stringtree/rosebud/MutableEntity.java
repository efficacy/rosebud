package org.stringtree.rosebud;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.stringtree.util.testing.Checklist;

public class MutableEntity implements Entity {
	private final String id;
	
	private Set<Attribute> attributes;
	private Map<String, String> singleIndex;
	
	public MutableEntity(String id) {
		this.id = id;
		this.attributes = new HashSet<Attribute>();
		this.singleIndex = new HashMap<String, String>();
	}
	
	public MutableEntity(String id, Collection<Attribute> collection) {
		this(id);
		setAttributes(collection);
	}
	
	public MutableEntity(Entity other) {
		this(other.getId());
		merge(other);
	}

	@Override
	public Entity merge(Entity other) {
		for (Attribute attribute : other) {
			setAttribute(attribute);
		}
		return this;
	}

	@Override
	public void setAttributes(Collection<Attribute> collection) {
		clear();
		for (Attribute attribute : collection) {
			setAttribute(attribute);
		}
	}

	@Override
	public void clear() {
		singleIndex.clear();
		attributes.clear();
	}

	@Override
	public void setAttribute(Attribute attribute) {
		attributes.add(attribute);
		singleIndex.put(attribute.rel, attribute.dest);
	}

	@Override
	public void clearAttribute(Attribute attribute) {
		attributes.remove(attribute);
		if (null != attribute.rel) {
			singleIndex.remove(attribute.rel);
			rebuildSingleIndex(attribute.rel);
		}
	}

	private void rebuildSingleIndex(String rel) {
		for (Attribute attribute : attributes) {
			if (rel.equals(attribute.rel)) {
				singleIndex.put(attribute.rel, attribute.dest);
				break;
			}
		}
	}

	public void setAttribute(String name, String value) {
		setAttribute(Attribute.bySrcRelDest(id, name, value));
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	@Override
	public String getAttributeValue(String name) {
		return singleIndex.get(name);
	}

	@Override
	public Map<String, String> getAttributeValues(String name) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Attribute attribute : attributes) {
			if (name.equals(attribute.rel)) {
				ret.put(attribute.seq, attribute.dest);
			}
		}
		return ret;
	}

	@Override
	public Iterator<Attribute> iterator() {
		return attributes.iterator();
	}

	@Override
	public int size() {
		return attributes.size();
	}
	
	@Override
	public String toString() {
		return "Entity(" + id + ")" + attributes;
	}
	
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Entity)) return false;
		Entity other = (Entity) object;

		return new Checklist<Attribute>(attributes).check(other.iterator());
	}
}
