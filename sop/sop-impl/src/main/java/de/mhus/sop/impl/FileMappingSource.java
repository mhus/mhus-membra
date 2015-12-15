package de.mhus.sop.impl;

import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.GroupMappingSource;

public class FileMappingSource implements GroupMappingSource {

	@Override
	public Boolean isGroupMapping(SopApi api, Account account, String mappingName, String id, String action) {

		return null;
	}

}
