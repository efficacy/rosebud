package org.stringtree.rosebud;

import java.util.Collection;

public interface Entity extends Iterable<Attribute> {
	String getId();
	boolean isEmpty();
	String getAttributeValue(String name);
	
	void clear();
	void setAttribute(Attribute attribute);
	void setAttribute(String name, String value);
	void setAttribute(String name, String value, String data);
	void setAttributes(Collection<Attribute> collection);
	Entity merge(Entity other);
}
