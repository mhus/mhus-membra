package de.mhus.sop.mfw.api.aaa;

import java.util.UUID;

public class Ace {
	
	public static final String GENERAL_LEADER = "leader";
	public static final String GENERAL_MEMBER = "member";
	public static final String GENERAL_BACKEND = "backend";
	public static final String GENERAL_ADMIN = "admin";
	public static final String GENERAL_RESELLER = "reseller";
	public static final String GENERAL_USERADMIN = "useradm";

	public static final String RIGHTS_NONE = "";
	public static final String RIGHTS_ALL = "crud";
	public static final String RIGHTS_RO = "r";
	public static final String GENERAL_OPERATOR = "operator";
	public static final Ace ACE_NONE = new Ace(null,null,null,null,RIGHTS_NONE);
	public static final Ace ACE_ALL = new Ace(null,null,null,null,RIGHTS_ALL);
	public static final Ace ACE_RO = new Ace(null,null,null,null,RIGHTS_RO);

	private String account;
	private String type;
	private UUID target;
	private String key;
	private String rights;
	
	
	public Ace(String account, String type, UUID target,
			String key, String rights) {
		super();
		this.account = account;
		this.type = type;
		this.target = target;
		this.key = key;
		this.rights = rights;
	}
	
	public Ace(String rights) {
		this(null, null, null, null, rights);
	}

	public String getAccount() {
		return account;
	}
	public String getType() {
		return type;
	}
	public UUID getTarget() {
		return target;
	}
	public String getKey() {
		return key;
	}
	public String getRights() {
		return rights;
	}

	public boolean canCreate() {
		return hasFlag('c');
	}
	
	public boolean canRead() {
		return hasFlag('r');
	}
	
	public boolean canUpdate() {
		return hasFlag('u');
	}
	
	public boolean canDelete() {
		return hasFlag('d');
	}

	public boolean hasFlag(char f) {
		return getRights() != null && getRights().indexOf(f) > -1;
	}

	private void setFlag(char f) {
		if (rights == null) { rights = String.valueOf(f); return; }
		if (rights.indexOf(f) > -1) return;
		rights = rights + f;
	}

	/**
	 * Additional rights merging.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Ace unionOfAces(Ace first, Ace second) {
		if (first == null && second == null) return new Ace(null, null, null, null, RIGHTS_NONE);
		if (first == null) return second;
		if (second == null) return first;
		Ace next = new Ace(first.getAccount(), first.getType(), first.getTarget(), first.getKey(), first.getRights());
		for (int i = 0; i < second.getRights().length(); i++)
			next.setFlag(second.getRights().charAt(i));
		return next;
	}
	
	public String toString() {
		return account + "," + type + "," + key + "," + rights +"," + target + "@Ace";
	}
	
}
