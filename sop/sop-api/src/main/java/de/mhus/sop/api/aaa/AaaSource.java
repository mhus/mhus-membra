package de.mhus.sop.api.aaa;

public interface AaaSource {

	Account findAccount(String account);

	Trust findTrust(String trust);

	String createTrustTicket(AaaContext user);

}
