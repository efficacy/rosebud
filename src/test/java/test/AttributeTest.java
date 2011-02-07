package test;

import junit.framework.TestCase;

import org.stringtree.rosebud.Attribute;

public class AttributeTest extends TestCase {
	Attribute a;
	
	public void testSingleValueConstructor() {
		a = new Attribute("E1", "name", "Frank");
		assertEquals("E1", a.from);
		assertEquals("name", a.rel);
		assertEquals(0L, a.seq);
		assertEquals("Frank", a.to);
	}
}
