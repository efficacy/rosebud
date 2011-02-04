package org.stringtree.rosebud;

public interface Store {
	Entity get(String id);
	void put(Entity entity);
	void delete(String id);
}
