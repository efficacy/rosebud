package test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {
    
    public static TestSuite suite() {
        TestSuite ret = new TestSuite();

        ret.addTestSuite(MySQLStoreTest.class);
        ret.addTestSuite(MemoryStoreTest.class);
        ret.addTestSuite(AttributeTest.class);
        ret.addTestSuite(EmptyTest.class);

        return ret;
    }
}
