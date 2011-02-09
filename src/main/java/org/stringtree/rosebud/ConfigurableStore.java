package org.stringtree.rosebud;

public interface ConfigurableStore extends Store {
	void configure() throws Exception;
}
