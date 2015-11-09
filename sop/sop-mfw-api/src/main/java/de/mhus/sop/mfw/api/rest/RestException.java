package de.mhus.sop.mfw.api.rest;

import de.mhus.lib.errors.MException;

public class RestException extends MException {

	private static final long serialVersionUID = 1L;
	private long errId;

	public RestException(long errId, Object... in) {
		super(in);
		this.errId = errId;
	}

	public long getErrorId() {
		return errId;
	}
}
