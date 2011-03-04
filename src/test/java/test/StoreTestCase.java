package test;

import java.util.Collection;
import java.util.Map;

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
		in.setAttribute("name", "Frank");
		store.put(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals(1, out.size());
		assertEquals("Frank", out.getAttributeValue("name"));
	}
	
	public void testSeparatePutToSameEntity() {
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		store.put(in);
		
		in = new MutableEntity("E1");
		in.setAttribute("wife", "Margaret");
		store.put(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertNull(out.getAttributeValue("name"));
		assertEquals("Margaret", out.getAttributeValue("wife"));
	}

	public void testAddWithoutPrevious() {
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		in.setAttribute("wife", "Margaret");
		store.add(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals(2, out.size());
		assertEquals("Frank", out.getAttributeValue("name"));
		assertEquals("Margaret", out.getAttributeValue("wife"));
	}

	public void testAddOverwritesPrevious() {
		store.put(Attribute.bySrcRelDest("E1", "name", "Waldo"));
		
		Entity before = store.get("E1");
		assertNotNull(before);
		assertEquals(1, before.size());
		assertEquals("Waldo", before.getAttributeValue("name"));
		
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		in.setAttribute("wife", "Margaret");
		store.add(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals(2, out.size());
		assertEquals("Frank", out.getAttributeValue("name"));
		assertEquals("Margaret", out.getAttributeValue("wife"));
	}

	public void testAddExtendsPrevious() {
		store.put(Attribute.bySrcRelSeqDest("E1", "daughter", "1", "Elizabeth"));
		store.put(Attribute.bySrcRelSeqDest("E1", "daughter", "2", "Katherine"));
		
		Entity before = store.get("E1");
		assertNotNull(before);
		assertEquals(2, before.size());
		Map<String, String> bdaughters = before.getAttributeValues("daughter");
		assertEquals("Elizabeth", bdaughters.get("1"));
		assertEquals("Katherine", bdaughters.get("2"));
		
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		in.setAttribute("wife", "Margaret");
		store.add(in);
		
		Entity out = store.get("E1");
		assertNotNull(out);
		assertEquals(4, out.size());
		assertEquals("Frank", out.getAttributeValue("name"));
		assertEquals("Margaret", out.getAttributeValue("wife"));
		Map<String, String> adaughters = before.getAttributeValues("daughter");
		assertEquals("Elizabeth", adaughters.get("1"));
		assertEquals("Katherine", adaughters.get("2"));
	}
	
	public void testPutSoleAttribute() {
		Attribute at = Attribute.bySrcRelDest("S1", "stuff", "there");
		assertNull(store.get("S1"));
		store.put(at);
		
		Entity out = store.get("S1");
		assertNotNull(out);
		assertEquals("there", out.getAttributeValue("stuff"));
	}
	
	public void testPutSoleAttributeOverwrites() {
		assertNull(store.get("S1"));
		store.put(Attribute.bySrcRelDest("S1", "stuff", "there"));
		
		Entity out = store.get("S1");
		assertNotNull(out);
		assertEquals(1, out.size());
		assertEquals("there", out.getAttributeValue("stuff"));
		
		store.put(Attribute.bySrcRelDest("S1", "stuff", "haha"));
		
		Entity next = store.get("S1");
		assertNotNull(next);
		assertEquals(1, next.size());
		assertEquals("haha", next.getAttributeValue("stuff"));
		
	}
	
	public void testPutAdditionalAttribute() {
		in = new MutableEntity("S1");
		in.setAttribute("name", "Frank");
		store.put(in);

		Attribute at = Attribute.bySrcRelDest("S1", "stuff", "there");
		Entity out = store.get("S1");
		assertNotNull(out);
		assertNull("there", out.getAttributeValue("stuff"));
		assertEquals("Frank", out.getAttributeValue("name"));

		store.put(at);
		
		out = store.get("S1");
		assertNotNull(out);
		assertEquals("there", out.getAttributeValue("stuff"));
		assertEquals("Frank", out.getAttributeValue("name"));
	}
	
	public void testDelete() {
		testNonEmptyEntity();
		store.delete("E1");
		assertNull(store.get("E1"));
	}
	
	public void testMatchToFindSrc() {
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		in.setAttribute("wife", "Margaret");
		store.put(in);
		
		in = new MutableEntity("E2");
		in.setAttribute("name", "Margaret");
		store.put(in);
		
		in = new MutableEntity("E3");
		in.setAttribute("name", "Margaret");
		store.put(in);
		
		Collection<String> out = store.find(Attribute.byRelDest("name", "Frank"));
		assertTrue(new Checklist<String>("E1").check(out));
		
		out = store.find(Attribute.byRelDest("name", "Margaret"));
		assertTrue(new Checklist<String>("E2","E3").check(out));
		
		out = store.find(Attribute.byDest("Margaret"));
		assertTrue(new Checklist<String>("E1","E2","E3").check(out));
	}
	
	public void testMatchToFindDest() {
		in = new MutableEntity("E1");
		in.setAttribute("name", "Frank");
		in.setAttribute("wife", "Margaret");
		store.put(in);
		
		Collection<String> out = store.find(Attribute.bySrcRel("E1", "name"));
		assertTrue(new Checklist<String>("Frank").check(out));
		
		out = store.find(Attribute.bySrcRelDest("E1", "wife", null));
		assertTrue(new Checklist<String>("Margaret").check(out));
	}
	
	public void testFindNone() {
		Collection<Attribute> attributes = store.match(Attribute.bySrcRelDest(null, null, null));
		assertTrue(attributes.isEmpty());
	}
	
	public void testFindOne() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		Collection<Attribute> attributes = store.match(Attribute.byNothing());
		assertTrue(new Checklist<Attribute>(Attribute.bySrcRelDest("U1", "name", "Frank")).check(attributes));
	}
	
	public void testFindSome() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		store.put(Attribute.bySrcRelDest("U2", "name", "Margaret"));
		store.put(Attribute.bySrcRelDest("U1", "wife", "Margaret"));
		Collection<Attribute> attributes = store.match(Attribute.bySrcRelDest(null, null, "Margaret"));
		assertTrue(new Checklist<Attribute>(
				Attribute.bySrcRelDest("U2", "name", "Margaret"),
				Attribute.bySrcRelDest("U1", "wife", "Margaret")
			).check(attributes));
	}
	
	public void testExistsEmpty() {
		assertFalse(store.exists(Attribute.byDest("Margaret")));
	}
	
	public void testExistsNoMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		assertFalse(store.exists(Attribute.byDest("Margaret")));
	}
	
	public void testExistsSingleMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		store.put(Attribute.bySrcRelDest("U2", "name", "Margaret"));
		assertTrue(store.exists(Attribute.byDest("Margaret")));
	}
	
	public void testExistsMultipleMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		store.put(Attribute.bySrcRelDest("U2", "name", "Margaret"));
		assertTrue(store.exists(Attribute.byDest("Margaret")));
		store.put(Attribute.bySrcRelDest("U1", "wife", "Margaret"));
	}
	
	public void testFindOneEmpty() {
		assertNull(store.findOne(Attribute.byDest("Margaret")));
	}
	
	public void testFindOneNoMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		assertNull(store.findOne(Attribute.byDest("Margaret")));
	}
	
	public void testFindOneSingleMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		store.put(Attribute.bySrcRelDest("U2", "name", "Margaret"));
		assertNotNull(store.findOne(Attribute.byDest("Margaret")));
	}
	
	public void testFindOneMultipleMatch() {
		store.put(Attribute.bySrcRelDest("U1", "name", "Frank"));
		store.put(Attribute.bySrcRelDest("U2", "name", "Margaret"));
		assertTrue(store.exists(Attribute.byDest("Margaret")));
		assertNotNull(store.findOne(Attribute.byDest("Margaret")));
	}
}
