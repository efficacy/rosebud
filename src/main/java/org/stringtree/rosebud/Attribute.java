package org.stringtree.rosebud;

import org.stringtree.util.Utils;

public class Attribute implements Comparable<Attribute> {
	public final String from;
	public final String rel;
	public final long seq;
	public final String to;
	public final long stamp;
	public final String text;
	
	public Attribute(String from, String rel, long seq, String to, long modified) {
		this.from = from;
		this.rel = rel;
		this.seq = seq;
		this.to = to;
		this.stamp = modified;
		this.text = "Link[from=" + from + ",rel=" + rel + ",seq=" + seq + ",to=" + to + ",expiry=" + modified + "]";
	}

	public Attribute(String from, String rel, long seq, String to) {
		this(from, rel, seq, to, System.currentTimeMillis());
	}
	
	public static String getId(String from, String rel, long seq) {
		return "Link[from=" + from + ",rel=" + rel + ",seq=" + seq + "]";
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Attribute)) return false;
		Attribute other = (Attribute)obj;
		return seq == other.seq && Utils.same(from, other.from) && 
			Utils.same(rel, other.rel);
	}
	
	public boolean strictEquals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Attribute)) return false;
		Attribute other = (Attribute)obj;
		return seq == other.seq && stamp == other.stamp && Utils.same(from, other.from) && 
			Utils.same(rel, other.rel) && Utils.same(to, other.to);
	}

	public int compareTo(Attribute other) {
		int ret = (int)(this.seq - other.seq) * 10;
		if (0 == ret) ret = this.rel.compareTo(other.rel);
		if (0 == ret) ret = this.from.compareTo(other.from);
		return ret;
	}
	
	public String toString() {
		return text;
	}
	
	public int hashCode() {
		return text.hashCode();
	}
}
