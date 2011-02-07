package test;

import junit.framework.TestCase;

import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;

public abstract class StoreTestCase extends TestCase {
	protected abstract Store create();
	
	protected Store store;
	
	public void setUp() {
		store = create();
	}
	
	public void testUnknownEntity() {
		assertNull(store.get("E1"));
	}
	
	public void testEmptyEntity() {
		store.put(new MutableEntity("E1"));
		Entity out = store.get("E1");
		assertNull(out);
	}
	
	public void testNonEmptyEntity() {
		MutableEntity in = new MutableEntity("E1");
		in.setAttributeValue("name", "Frank");
		store.put(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals("Frank", out.getAttributeValue("name"));
	}
}
