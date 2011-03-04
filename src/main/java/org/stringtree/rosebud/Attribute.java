package org.stringtree.rosebud;

import org.stringtree.util.Utils;

public class Attribute implements Comparable<Attribute> {
	public final String src;
	public final String rel;
	public final String seq;
	public final String dest;
	public final String data;
	public final long stamp;
	
	public Attribute(String src, String rel, String seq, String dest, String data, long stamp) {
		this.src = src;
		this.rel = rel;
		this.seq = seq;
		this.dest = dest;
		this.data = data;
		this.stamp = stamp;
	}

	public Attribute(String src, String rel, String seq, String dest, String data) {
		this(src, rel, seq, dest, data, System.currentTimeMillis());
	}
	
	public static Attribute byNothing() {
		return new Attribute(null, null, null, null, null);
	}
	
	public static Attribute bySrc(String src) {
		return new Attribute(src, null, null, null, null);
	}
	
	public static Attribute byRel(String rel) {
		return new Attribute(null, rel, null, null, null);
	}
	
	public static Attribute byDest(String dest) {
		return new Attribute(null, null, null, dest, null);
	}
	
	public static Attribute bySrcRel(String src, String rel) {
		return new Attribute(src, rel, null, null, null);
	}
	
	public static Attribute bySrcDest(String src, String dest) {
		return new Attribute(src, null, null, dest, null);
	}
	
	public static Attribute byRelSeq(String rel, String seq) {
		return new Attribute(null, rel, seq, null, null);
	}
	
	public static Attribute byRelDest(String rel, String dest) {
		return new Attribute(null, rel, null, dest, null);
	}
	
	public static Attribute bySrcRelDest(String src, String rel, String dest) {
		return new Attribute(src, rel, null, dest, null);
	}
	
	public static Attribute bySrcRelSeq(String src, String rel, String seq) {
		return new Attribute(src, rel, seq, null, null);
	}
	
	public static Attribute bySrcRelSeqDest(String src, String rel, String seq, String dest) {
		return new Attribute(src, rel, seq, dest, null);
	}
	
	public static Attribute bySrcDestData(String src, String dest, String data) {
		return new Attribute(src, null, null, dest, data);
	}
	
	public static Attribute bySrcRelDestData(String src, String rel, String dest, String data) {
		return new Attribute(src, rel, null, dest, data);
	}
	
	public static Attribute bySrcRelSeqDest(String src, String rel, String seq, String dest, String data) {
		return new Attribute(src, rel, seq, dest, data);
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
			Utils.same(rel, other.rel) && Utils.same(dest, other.dest) && Utils.same(data, other.data);
	}

	public int compareTo(Attribute other) {
		int ret = 0;
		if (0 == ret && null != src) ret += this.src.compareTo(other.src);
		if (0 == ret && null != rel) ret += this.rel.compareTo(other.rel);
		if (0 == ret && null != seq) ret += this.seq.compareTo(other.seq);
		return ret;
	}
	
	public String toString() {
		return "Attribute[src=" + src + ",rel=" + rel + ",seq=" + seq + ",dest=" + dest + ",data=" + data + ",stamp=" + stamp + "]";
	}
	
	private final String key() {
		return src + "~" + rel + "~" + seq;
	}
	
	public final int hashCode() {
		return 24761 ^ key().hashCode();
	}
}
