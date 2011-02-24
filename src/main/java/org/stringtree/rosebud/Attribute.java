package org.stringtree.rosebud;

import org.stringtree.util.Utils;

public class Attribute implements Comparable<Attribute> {
	public static final long NO_SEQ = 0;
	public static final Long NO_SEQ_OBJECT = NO_SEQ;
	
	public final String src;
	public final String rel;
	public final long seq;
	public final String dest;
	public final long stamp;
	public final String text;
	
	public Attribute(String from, String rel, long seq, String to, long modified) {
		this.src = from;
		this.rel = rel;
		this.seq = seq;
		this.dest = to;
		this.stamp = modified;
		this.text = "Link[from=" + from + ",rel=" + rel + ",seq=" + seq + ",to=" + to + ",expiry=" + modified + "]";
	}

	public Attribute(String from, String rel, long seq, String to) {
		this(from, rel, seq, to, System.currentTimeMillis());
	}

	public Attribute(String from, String rel, String to) {
		this(from, rel, NO_SEQ, to, System.currentTimeMillis());
	}
	
	public static String getId(String from, String rel, long seq) {
		return "Link[from=" + from + ",rel=" + rel + ",seq=" + seq + "]";
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Attribute)) return false;
		Attribute other = (Attribute)obj;
		return seq == other.seq && Utils.same(src, other.src) && 
			Utils.same(rel, other.rel);
	}
	
	public boolean strictEquals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Attribute)) return false;
		Attribute other = (Attribute)obj;
		return seq == other.seq && stamp == other.stamp && Utils.same(src, other.src) && 
			Utils.same(rel, other.rel) && Utils.same(dest, other.dest);
	}

	public int compareTo(Attribute other) {
		int ret = (int)(this.seq - other.seq) * 10;
		if (0 == ret) ret = this.rel.compareTo(other.rel);
		if (0 == ret) ret = this.src.compareTo(other.src);
		return ret;
	}
	
	public String toString() {
		return text;
	}
	
	public int hashCode() {
		return 24761 ^ text.hashCode();
	}
}
