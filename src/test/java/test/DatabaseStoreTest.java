package test;

import org.stringtree.rosebud.Store;
import org.stringtree.rosebud.db.MySQLStore;
import org.stringtree.rosebud.util.DatabaseUtils;

public class DatabaseStoreTest extends StoreTestCase {

	@Override
	protected Store create() {
		return new MySQLStore(DatabaseUtils.getNamedDatasource("unittest"), true);
	}

}
