package org.stringtree.rosebud;

public abstract class CommonStore implements Store {

	@Override
	public void put(Entity entity) {
		delete(entity.getId());
		add(entity);
	}
}
