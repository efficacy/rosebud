package test;

import org.stringtree.rosebud.Store;
import org.stringtree.rosebud.memory.MemoryStore;

public class MemoryStoreTest extends StoreTestCase {

	@Override
	protected Store create() {
		return new MemoryStore();
	}
}
