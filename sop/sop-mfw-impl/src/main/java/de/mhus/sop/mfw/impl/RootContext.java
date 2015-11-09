package de.mhus.sop.mfw.impl;

public class RootContext extends AaaContextImpl {

	public RootContext() {
		super(new AccountRoot());
		adminMode = true;
	}

}
