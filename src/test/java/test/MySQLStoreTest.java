package test;

import org.stringtree.db.DatabaseUtils;
import org.stringtree.rosebud.Store;
import org.stringtree.rosebud.db.MySQLStore;

public class MySQLStoreTest extends StoreTestCase {

	@Override
	protected Store create() {
		return new MySQLStore(DatabaseUtils.getNamedDatasource("unittest"), true);
	}

}
