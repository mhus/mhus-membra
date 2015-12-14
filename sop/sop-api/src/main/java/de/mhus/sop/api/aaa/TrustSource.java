package de.mhus.sop.api.aaa;

public interface TrustSource {

	Trust findTrust(String trust);

	String createTrustTicket(AaaContext user);

}
