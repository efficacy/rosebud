package manual;

import org.stringtree.db.DatabaseWrapper;
import org.stringtree.rosebud.util.DatabaseUtils;

public class RunDDL {
	public static void main(String[] args) {
		String source = args.length > 0 ? args[0] : "unittest";
		String script = args.length > 1 ? args[1] : "src/main/scripts/rosebud.ddl";
		DatabaseWrapper db = new DatabaseWrapper(DatabaseUtils.getNamedDatasource(source));
		db.script(script);
	}
}
