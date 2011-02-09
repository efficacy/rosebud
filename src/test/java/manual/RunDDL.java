package manual;

import org.stringtree.finder.StringFinder;
import org.stringtree.rosebud.ConfigurableStore;
import org.stringtree.rosebud.db.MySQLStore;
import org.stringtree.rosebud.util.DatabaseUtils;

public class RunDDL {
	public static void main(String[] args) throws Exception {
		String source = args.length > 0 ? args[0] : "unittest";
		String user = args.length > 1 ? args[1] : null;
		String password = args.length > 2 ? args[2] : null;
		
		StringFinder config = DatabaseUtils.getNamedConfig(source);
		ConfigurableStore store = new MySQLStore(DatabaseUtils.getDatasource(config, user, password));
		boolean ok = true;
		if (null != user && null != password) {
			ok = store.create(config);
			System.out.println("Sucessfully created database for " + store.getClass());
		}
		if (ok) {
			store.configure(config);
			System.out.println("Sucessfully configured database for " + store.getClass());
		} else {
			System.out.println("failed to configure database for " + store.getClass());
		}
	}
}
