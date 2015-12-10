package de.mhus.sop.impl;

public class RootContext extends AaaContextImpl {

	public RootContext() {
		super(new AccountRoot());
		adminMode = true;
	}

}
