package manual;

import org.stringtree.rosebud.ConfigurableStore;
import org.stringtree.rosebud.db.MySQLStore;
import org.stringtree.rosebud.util.DatabaseUtils;

public class RunDDL {
	public static void main(String[] args) throws Exception {
		String source = args.length > 0 ? args[0] : "unittest";
		
		ConfigurableStore store = new MySQLStore(DatabaseUtils.getNamedDatasource(source));
		store.configure();
		System.out.println("Sucessfully configured database for " + store.getClass());
	}
}
