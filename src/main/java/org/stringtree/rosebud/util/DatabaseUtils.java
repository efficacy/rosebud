package org.stringtree.rosebud.util;

import java.io.IOException;

import javax.sql.DataSource;

import org.stringtree.db.LocalMySQLDataSource;
import org.stringtree.finder.MapStringKeeper;
import org.stringtree.finder.StringKeeper;
import org.stringtree.util.spec.SpecReader;

public class DatabaseUtils {
	public static DataSource getNamedDatasource(String name) {
		StringKeeper config = new MapStringKeeper();
		try {
			SpecReader.load(config, "src/main/config/" + name + ".spec");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new LocalMySQLDataSource(
			config.get("db.schema"), config.get("db.user"), config.get("db.password"));
	}
}
