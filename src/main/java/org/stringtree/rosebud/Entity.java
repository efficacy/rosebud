package org.stringtree.rosebud;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Entity {
	public final String id;
	
	private Set<Attribute> attributes;
	private Map<String, String> singleIndex;
	
	public Entity(String id) {
		this.id = id;
		this.attributes = new HashSet<Attribute>();
		this.singleIndex = new HashMap<String, String>();
	}
	
	public Entity(String id, Collection<Attribute> collection) {
		this(id);
		setAttributes(collection);
	}

	public void setAttributes(Collection<Attribute> collection) {
		clear();
		for (Attribute attribute : collection) {
			setAttribute(attribute);
		}
	}

	public void clear() {
		singleIndex.clear();
		attributes.clear();
	}

	public void setAttribute(Attribute attribute) {
		attributes.add(attribute);
		singleIndex.put(attribute.rel, attribute.to);
	}

	public void setAttributeValue(String name, String value) {
		setAttribute(new Attribute(id, name, value));
	}

	public boolean isEmpty() {
		return attributes.isEmpty();
	}

	public String getAttributeValue(String name) {
		return singleIndex.get(name);
	}
}
