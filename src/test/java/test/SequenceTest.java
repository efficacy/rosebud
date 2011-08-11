package test;

import junit.framework.TestCase;

import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Sequence;

public class SequenceTest extends TestCase {
	Sequence seq;
	
	public void setUp() {
		seq = new Sequence("size");
	}
	
	public void testCreateEntityWithNoAttributes() {
		Entity e1 = new MutableEntity("a");
		assertFalse(seq.match(null, e1));
		assertFalse(seq.containsId("a"));
	}
	
	public void testCreateEntityWithNoMatchingAttributes() {
		Entity e1 = new MutableEntity("a");
		e1.setAttribute("colour", "blue");
		assertFalse(seq.match(null, e1));
		assertFalse(seq.containsId("a"));
	}
	
	public void testCreateEntityWithMatchingAttribute() {
		Entity e1 = new MutableEntity("a");
		e1.setAttribute("size", "14");
		assertTrue(seq.match(null, e1));
		assertTrue(seq.containsId("a"));
	}
}
