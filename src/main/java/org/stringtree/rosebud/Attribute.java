package org.stringtree.rosebud;

import org.stringtree.util.Utils;

public class Attribute implements Comparable<Attribute> {
	public static final String NO_SEQ = "";
	
	public final String src;
	public final String rel;
	public final String seq;
	public final String dest;
	public final String data;
	public final long stamp;
	public final String text;
	
	public Attribute(String src, String rel, String seq, String dest, String data, long modified) {
		this.src = src;
		this.rel = rel;
		this.seq = seq;
		this.dest = dest;
		this.data = data;
		this.stamp = modified;
		
		this.text = "Attribute[src=" + src + ",rel=" + rel + ",seq=" + seq + ",dest=" + dest + ",data=" + data + ",expiry=" + modified + "]";
	}

	public Attribute(String src, String rel, String seq, String dest, String data) {
		this(src, rel, seq, dest, data, System.currentTimeMillis());
	}

	public Attribute(String src, String rel, String dest, String data) {
		this(src, rel, NO_SEQ, dest, data, System.currentTimeMillis());
	}

	public Attribute(String src, String rel, String dest) {
		this(src, rel, NO_SEQ, dest, null, System.currentTimeMillis());
	}
	
	public static String getId(String src, String rel, long seq) {
		return "[" + src + "~" + rel + "~" + seq + "]";
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
		int ret = this.seq.compareTo(other.seq);
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
