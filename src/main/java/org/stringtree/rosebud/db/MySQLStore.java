package org.stringtree.rosebud.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.stringtree.db.CollectingResultRowListener;
import org.stringtree.db.DatabaseWrapper;
import org.stringtree.db.ResultRowListener;
import org.stringtree.db.StatementPopulator;
import org.stringtree.finder.StringFinder;
import org.stringtree.rosebud.Attribute;
import org.stringtree.rosebud.ConfigurableStore;
import org.stringtree.rosebud.Entity;
import org.stringtree.rosebud.MutableEntity;
import org.stringtree.util.Utils;

public class MySQLStore implements ConfigurableStore {
	DatabaseWrapper db;
	
	static final String CREATE =
		"drop table if exists attribute;" +
		"create table attribute (" +
		"  src varchar(255)," +
		"  rel varchar(255)," +
		"  seq varchar(255)," +
		"  dest varchar(255)," +
		"  data text," +
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
		Collection<Attribute> attributes = (Collection<Attribute>) db.query("select src,rel,seq,dest,data,modified from attribute where src=?", new StatementPopulator() {
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
						results.getString("seq"),
						results.getString("dest"),
						results.getString("data"),
						results.getLong("modified")
				));
				return null;
			}
		});
		return attributes.isEmpty() ? null : new MutableEntity(id, attributes);
	}

	private static final String PUT_DML = "replace into attribute (src,rel,seq,dest,data,modified) values ";
	private static final String PLACEHOLDERS = "(?,?,?,?,?,?)";
	int maxLength = 1024;
	
	@Override
	public void put(Entity entity) {
		db.update("delete from attribute where src=?", entity.getId());
		if (!entity.isEmpty()) {
			StringBuffer buf = new StringBuffer(PUT_DML);
			List<Object> values = new ArrayList<Object>();
			int count = 0;
			for (final Attribute attribute : entity) {
				if (count > 0) buf.append(",");
				buf.append(PLACEHOLDERS);
				values.add(attribute.src);
				values.add(attribute.rel);
				values.add(attribute.seq);
				values.add(attribute.dest);
				values.add(attribute.data);
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
		db.update(PUT_DML + PLACEHOLDERS, attribute.src, attribute.rel, attribute.seq, attribute.dest, attribute.data, attribute.stamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Attribute> match(Attribute pattern) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("select src,rel,seq,dest,data,modified from attribute");
		addColumnMatch(query, "src", pattern.src, args);
		addColumnMatch(query, "rel", pattern.rel, args);
		addColumnMatch(query, "seq", pattern.seq, args);
		addColumnMatch(query, "dest", pattern.dest, args);
		String sql = query.toString();
System.err.println("MySQLStore.match sql=" + sql + " args=" + args);
		return (Collection<Attribute>) db.query(sql, new CollectingResultRowListener<Attribute>() {
			@Override public Object row(ResultSet results, int rowNumber) throws SQLException {
System.err.println("MySQLStore.match found row " + results);
				add(new Attribute(
						results.getString("src"),
						results.getString("rel"),
						results.getString("seq"),
						results.getString("dest"),
						results.getString("data"),
						results.getLong("modified")
				));
				return null;
			}
		}, args.toArray());
	}

	@Override
	public Attribute matchOne(Attribute pattern) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("select src,rel,seq,dest,data,modified from attribute");
		addColumnMatch(query, "src", pattern.src, args);
		addColumnMatch(query, "rel", pattern.rel, args);
		addColumnMatch(query, "seq", pattern.seq, args);
		addColumnMatch(query, "dest", pattern.dest, args);
		query.append(" limit 1");
		Attribute found = (Attribute) db.query(query.toString(), new ResultRowListener() {
			@Override public Object row(ResultSet results, int rowNumber) throws SQLException {
				return new Attribute(
						results.getString("src"),
						results.getString("rel"),
						results.getString("seq"),
						results.getString("dest"),
						results.getString("data"),
						results.getLong("modified")
				);
			}
		}, args.toArray());
		return found;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> find(final Attribute pattern) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder query = new StringBuilder("select src,rel,seq,dest,data,modified from attribute");
		addColumnMatch(query, "src", pattern.src, args);
		addColumnMatch(query, "rel", pattern.rel, args);
		addColumnMatch(query, "seq", pattern.seq, args);
		addColumnMatch(query, "dest", pattern.dest, args);
		return (Collection<String>) db.query(query.toString(), new CollectingResultRowListener<String>() {
			@Override public Object row(ResultSet results, int rowNumber) throws SQLException {
				add(returnable(results, pattern));
				return null;
			}
		}, args.toArray());
	}

	@Override
	public String findOne(Attribute pattern) {
		return returnable(matchOne(pattern), pattern);
	}
	
	private String returnable(ResultSet results, Attribute pattern) throws SQLException {
		if (null == pattern.src) return results.getString("src");
		if (null == pattern.rel) return results.getString("rel");
		if (null == pattern.dest) return results.getString("dest");
		if (null == pattern.data) return results.getString("data");
		return results.getString("src");
	}
	
	private String returnable(Attribute result, Attribute pattern) {
		if (null == result) return null;
		if (null == pattern.src) return result.src;
		if (null == pattern.rel) return result.rel;
		if (null == pattern.dest) return result.dest;
		if (null == pattern.data) return result.data;
		return result.src;
	}

	@Override
	public boolean exists(Attribute pattern) {
		return null != matchOne(pattern);
	}

	private void addColumnMatch(StringBuilder query, String colname, Object colvalue, List<Object> args) {
		if (null != colvalue && !Utils.same(Attribute.NO_SEQ, colvalue)) {
			if (args.isEmpty()) {
				query.append(" where "); 
			} else {
				query.append(" and "); 
			}
			query.append(colname);
			query.append("=?");
			args.add(colvalue);
		}
	}

	// TODO could be a memory hog for large stores - consdider chunking
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Attribute> iterator() {
		Collection<Attribute> ret = (Collection<Attribute>) db.query("select src,rel,seq,dest,data,modified from attribute", new CollectingResultRowListener<Attribute>() {
			@Override public Object row(ResultSet results, int rowNumber) throws SQLException {
				add(new Attribute(
						results.getString("src"),
						results.getString("rel"),
						results.getString("seq"),
						results.getString("dest"),
						results.getString("data"),
						results.getLong("modified")
				));
				return null;
			}
		});
		return ret.iterator();
	}
}
