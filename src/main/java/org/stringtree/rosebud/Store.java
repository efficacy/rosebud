package org.stringtree.rosebud;

import java.util.Collection;

public interface Store {
	void put(Attribute attribute);
	void put(Entity entity);
	void delete(String id);
	void clear();

	Entity get(String id);
	Collection<String> match(Attribute pattern);
}
