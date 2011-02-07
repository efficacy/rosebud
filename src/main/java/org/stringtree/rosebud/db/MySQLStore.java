package org.stringtree.rosebud.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.stringtree.db.CollectingResultRowListener;
import org.stringtree.db.DatabaseWrapper;
import org.stringtree.db.StatementPopulator;
import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.rosebud.Store;

public class MySQLStore implements Store {
	
	DatabaseWrapper db;

	public MySQLStore(DataSource ds, boolean clear) {
		db = new DatabaseWrapper(ds);
		if (clear) clear();
	}

	@Override
	public void clear() {
		db.update("truncate attribute");
	}

	@Override
	public void delete(final String id) {
		db.update("delete from attribute where src=?", new StatementPopulator() {
			@Override
			public void populate(PreparedStatement ps) throws SQLException {
				ps.setString(1, id);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entity get(final String id) {
		Collection<Attribute> attributes = (Collection<Attribute>) db.query("select src,rel,seq,dest,modified from attribute where src=?", new StatementPopulator() {
			@Override
			public void populate(PreparedStatement ps) throws SQLException {
				ps.setString(1, id);
			}
		}, new CollectingResultRowListener() {
			@Override
			public Object row(ResultSet results, int rowNumber) throws SQLException {
				add(new Attribute(
						results.getString("src"),
						results.getString("rel"),
						results.getLong("seq"),
						results.getString("dest"),
						results.getLong("modified")
				));
				return null;
			}
		});
		return attributes.isEmpty() ? null : new MutableEntity(id, attributes);
	}

	@Override
	public void put(Entity entity) {
		for (final Attribute attribute : entity) {
			db.update("replace into attribute (src,rel,seq,dest,modified) values (?,?,?,?,?)", new StatementPopulator() {
				@Override
				public void populate(PreparedStatement ps) throws SQLException {
					ps.setString(1, attribute.from);
					ps.setString(2, attribute.rel);
					ps.setLong(3, attribute.seq);
					ps.setString(4, attribute.to);
					ps.setLong(5, attribute.stamp);
				}
			}); 
		}
	}
}
