package org.stringtree.rosebud;

import org.stringtree.finder.StringFinder;

public interface ConfigurableStore extends Store {
	boolean create(StringFinder context) throws Exception;
}
