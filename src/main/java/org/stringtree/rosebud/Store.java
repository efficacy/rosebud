package org.stringtree.rosebud;

import java.util.Collection;

public interface Store extends Iterable<Attribute>{
	void put(Attribute attribute);
	void put(Entity entity);
	void add(Entity entity);
	void delete(String id);
	void clear();

	Entity get(String id);
	Collection<String> find(Attribute pattern);
	String findOne(Attribute pattern);
	Collection<Attribute> match(Attribute pattern);
	Attribute matchOne(Attribute pattern);
	boolean exists(Attribute pattern);
}
