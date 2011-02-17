package org.stringtree.rosebud;

import java.util.Collection;

public interface Entity extends Iterable<Attribute> {
	String getId();
	boolean isEmpty();
	String getAttributeValue(String name);
	
	void clear();
	void setAttribute(Attribute attribute);
	void setAttributeValue(String name, String value);
	void setAttributes(Collection<Attribute> collection);
	Entity merge(Entity other);
}
