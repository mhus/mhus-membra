package de.mhus.sop.api.aaa;

public interface GroupMappingSource {
	boolean isGroupMapping(Account account, String mappingName, String id, String action);
}
