package de.mhus.sop.api.aaa;

import de.mhus.sop.api.SopApi;

public interface GroupMappingSource {
	
	/**
	 * Return null if the mapping was not found, true or false if there is a concrete result.
	 * @param api
	 * @param account
	 * @param mappingName
	 * @param id
	 * @param action
	 * @return
	 */
	Boolean isGroupMapping(SopApi api, Account account, String mappingName, String id, String action);
}
