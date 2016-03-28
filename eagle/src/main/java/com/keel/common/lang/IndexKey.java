package com.keel.common.lang;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class IndexKey implements Serializable {
	private static final long serialVersionUID = -7248545707581906626L;

	private final long id;
	private final long p;
	private final long u;

    public IndexKey(long id, long p, long u) {
		super();
		this.id = id;
		this.p = p;
		this.u = u;
	}

	public long getId() {
		return id;
	}

	public long getP() {
		return p;
	}

	public long getU() {
		return u;
	}

	public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        } catch (Exception e) {
            return super.toString();
        }
    }
	
	
}
