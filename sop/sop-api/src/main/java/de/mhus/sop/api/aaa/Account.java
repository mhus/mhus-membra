package de.mhus.sop.api.aaa;

public interface Account {
	
	String getAccount();

	boolean isValide();

	boolean validatePassword(String password);

	boolean isSyntetic();

	String getDisplayName();

}
