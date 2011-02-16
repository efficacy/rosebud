package test;

import java.util.Collection;

import junit.framework.TestCase;

import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;
import org.stringtree.util.testing.Checklist;

public abstract class StoreTestCase extends TestCase {
	protected abstract Store create();
	
	protected Store store;
	protected MutableEntity in;
	
	public void setUp() {
		store = create();
	}
	
	public void testUnknownEntity() {
		assertNull(store.get("E1"));
	}
	
	public void testEmptyEntity() {
		store.put(new MutableEntity("E1"));
		assertNull(store.get("E1"));
	}
	
	public void testNonEmptyEntity() {
		in = new MutableEntity("E1");
		in.setAttributeValue("name", "Frank");
		store.put(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals("Frank", out.getAttributeValue("name"));
	}
	
	public void testSeparatePutToSameEntity() {
		in = new MutableEntity("E1");
		in.setAttributeValue("name", "Frank");
		store.put(in);
		
		in = new MutableEntity("E1");
		in.setAttributeValue("wife", "Margaret");
		store.put(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals("Frank", out.getAttributeValue("name"));
		assertEquals("Margaret", out.getAttributeValue("wife"));
	}
	
	public void testDelete() {
		testNonEmptyEntity();
		store.delete("E1");
		assertNull(store.get("E1"));
	}
	
	public void testMatch() {
		in = new MutableEntity("E1");
		in.setAttributeValue("name", "Frank");
		store.put(in);
		
		in = new MutableEntity("E1");
		in.setAttributeValue("wife", "Margaret");
		store.put(in);
		
		in = new MutableEntity("E2");
		in.setAttributeValue("name", "Margaret");
		store.put(in);
		
		in = new MutableEntity("E3");
		in.setAttributeValue("name", "Margaret");
		store.put(in);
		
		Collection<String> out = store.match(new Attribute(null, "name", "Frank"));
		assertTrue(new Checklist<String>("E1").check(out));
		
		out = store.match(new Attribute(null, "name", "Margaret"));
		assertTrue(new Checklist<String>("E2","E3").check(out));
		
		out = store.match(new Attribute(null, null, "Margaret"));
		assertTrue(new Checklist<String>("E1","E2","E3").check(out));
	}
}
