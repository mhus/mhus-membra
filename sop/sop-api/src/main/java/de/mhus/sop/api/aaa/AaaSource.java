package de.mhus.sop.api.aaa;

public interface AaaSource {

	Account findAccount(String account);

	Trust findTrust(String trust);

	boolean validatePassword(Account account, String password);

	String createTrustTicket(AaaContext user);

}
