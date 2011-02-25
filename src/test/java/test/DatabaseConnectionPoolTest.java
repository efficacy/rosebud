package test;

import java.sql.SQLException;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.stringtree.db.DatabaseConnectionPool;
import org.stringtree.db.DatabaseUtils;
import org.stringtree.db.VerifiableConnection;

public class DatabaseConnectionPoolTest extends TestCase {
	DataSource ds;
	DatabaseConnectionPool pool;
	
	public void setUp() {
		ds = DatabaseUtils.getNamedDatasource("unittest");
		pool = new DatabaseConnectionPool(ds, 100);
	}
	
	public void testClaim() throws SQLException {
		VerifiableConnection obj = pool.claim();
		assertFalse(obj.isClosed());
	}
	
	public void testTwoClaims() throws SQLException {
		VerifiableConnection obj1 = pool.claim();
		assertFalse(obj1.isClosed());

		VerifiableConnection obj2 = pool.claim();
		assertFalse(obj2.isClosed());
		
		assertNotSame(obj1, obj2);
	}
	
	public void testClaimReleaseClaim() throws SQLException {
		VerifiableConnection obj1 = pool.claim();
		assertFalse(obj1.isClosed());
		
		pool.release(obj1);

		VerifiableConnection obj2 = pool.claim();
		assertFalse(obj2.isClosed());

		assertSame(obj1, obj2);
	}
	
	public void testClaimInvalidReleaseClaim() throws SQLException {
		VerifiableConnection obj1 = pool.claim();
		assertFalse(obj1.isClosed());
		
		obj1.realConnection().close();
		pool.release(obj1);

		VerifiableConnection obj2 = pool.claim();
		assertFalse(obj2.isClosed());
		
		assertNotSame(obj1, obj2);
	}
	
	public void testClaimReleaseInvalidClaim() throws SQLException {
		VerifiableConnection obj1 = pool.claim();
		assertFalse(obj1.isClosed());
		
		pool.release(obj1);
		obj1.realConnection().close();

		VerifiableConnection obj2 = pool.claim();
		assertFalse(obj2.isClosed());
		
		assertNotSame(obj1, obj2);
	}
}
