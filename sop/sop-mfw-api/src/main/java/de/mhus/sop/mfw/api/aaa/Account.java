package de.mhus.sop.mfw.api.aaa;

public interface Account {
	
	String getAccount();

	boolean isValide();

	boolean validatePassword(String password);

	boolean isSyntetic();

	String getDisplayName();

}
