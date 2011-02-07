package test;

import java.io.IOException;

import javax.sql.DataSource;

import org.stringtree.db.LocalMySQLDataSource;
import org.stringtree.finder.MapStringKeeper;
import org.stringtree.finder.StringKeeper;
import org.stringtree.rosebud.Store;
import org.stringtree.rosebud.db.MySQLStore;
import org.stringtree.util.spec.SpecReader;

public class DatabaseStoreTest extends StoreTestCase {

	@Override
	protected Store create() {
		StringKeeper config = new MapStringKeeper();
		try {
			SpecReader.load(config, "src/main/config/unittest.spec");
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataSource ds = new LocalMySQLDataSource(
			config.get("db.schema"), config.get("db.user"), config.get("db.password"));
		return new MySQLStore(ds, true);
	}

}
