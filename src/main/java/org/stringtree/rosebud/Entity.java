package org.stringtree.rosebud;

public interface Entity extends Iterable<Attribute> {
	String getId();
	boolean isEmpty();
	String getAttributeValue(String name);
	
}
