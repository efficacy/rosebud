package org.stringtree.rosebud.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.stringtree.db.CollectingResultRowListener;
import org.stringtree.db.DatabaseWrapper;
import org.stringtree.db.StatementPopulator;
import org.stringtree.finder.StringFinder;
import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.ConfigurableStore;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;

public class MySQLStore implements ConfigurableStore {
	DatabaseWrapper db;
	
	static final String CREATE =
		"drop table if exists attribute;" +
		"create table attribute (" +
		"  src varchar(128)," +
		"  rel varchar(128)," +
		"  seq bigint," +
		"  dest text," +
		"  modified bigint," +
		"  primary key (src,rel,seq)" +
		");";

	public MySQLStore(DatabaseWrapper db, boolean clear) {
		this.db = db;
		if (clear) clear();
	}

	public MySQLStore(DatabaseWrapper db) {
		this(db,false);
	}

	public MySQLStore(DataSource ds, boolean clear) {
		this(new DatabaseWrapper(ds), clear);
	}

	public MySQLStore(DataSource ds) {
		this(ds, false);
	}
	
	public boolean create(StringFinder context) throws SQLException, IOException {
		db.scriptString(CREATE);
		return true;
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

	private static final String PUT_DML = "replace into attribute (src,rel,seq,dest,modified) values ";
	private static final String PLACEHOLDERS = "(?,?,?,?,?)";
	int maxLength = 1024;
	
	@Override
	public void put(Entity entity) {
		if (!entity.isEmpty()) {
			StringBuffer buf = new StringBuffer(PUT_DML);
			List<Object> values = new ArrayList<Object>();
			int count = 0;
			for (final Attribute attribute : entity) {
				if (count > 0) buf.append(",");
				buf.append(PLACEHOLDERS);
				values.add(attribute.from);
				values.add(attribute.rel);
				values.add(attribute.seq);
				values.add(attribute.to);
				values.add(attribute.stamp);

				if (buf.length() > maxLength) {
					putChunk(buf, values);
					count = 0;
				} else {
					++count;
				}
			}
			
			putChunk(buf, values);
		}
	}

	private void putChunk(StringBuffer buf, List<Object> values) {
		db.update(buf.toString(), values);
		buf.setLength(PUT_DML.length());
		values.clear();
	}

	@Override
	public void put(Attribute attribute) {
		db.update(PUT_DML + PLACEHOLDERS, attribute.from, attribute.rel, attribute.seq, attribute.to, attribute.stamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> match(Attribute attribute) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("select src,rel,seq,dest,modified from attribute where");
		addColumnMatch(query, "src", attribute.from, args);
		addColumnMatch(query, "rel", attribute.rel, args);
		addColumnMatch(query, "seq", attribute.seq, args);
		addColumnMatch(query, "dest", attribute.to, args);
		return (Collection<String>) db.query(query.toString(), new CollectingResultRowListener<String>() {
			@Override public Object row(ResultSet results, int rowNumber) throws SQLException {
				add(results.getString("src"));
				return null;
			}
		}, args.toArray());
	}

	private void addColumnMatch(StringBuilder query, String colname, Object colvalue, List<Object> args) {
		if (null != colvalue) {
			if (!args.isEmpty()) query.append(" and"); 
			query.append(" ");
			query.append(colname);
			query.append("=?");
			args.add(colvalue);
		}
	}
}
