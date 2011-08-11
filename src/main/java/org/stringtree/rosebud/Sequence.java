package org.stringtree.rosebud;

import java.util.ArrayList;
import java.util.Collection;

public class Sequence {
	private String attribute;
	private Collection<String> entities;

	public Sequence(String attribute) {
		this.attribute = attribute;
		this.entities = new ArrayList<String>();
	}

	public boolean match(Entity before, Entity after) {
		if (null != after) {
			if (null != after.getAttributeValue(attribute)) {
				add(after);
				return true;
			}
		}
		return false;
	}

	private void add(Entity after) {
		entities.add(after.getId());
	}

	public boolean containsId(String id) {
		return entities.contains(id);
	}
}
