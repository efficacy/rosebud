package org.stringtree.rosebud;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	public void setAttribute(String name, String value) {
		setAttribute(new Attribute(id, name, value));
	}

	public void setAttribute(String name, String value, String data) {
		setAttribute(new Attribute(id, name, "", value, data));
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
	public Iterator<Attribute> iterator() {
		return attributes.iterator();
	}
	
	@Override
	public String toString() {
		return "Entity(" + id + ")" + attributes;
	}
}
