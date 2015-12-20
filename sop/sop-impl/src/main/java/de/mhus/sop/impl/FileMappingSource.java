package de.mhus.sop.impl;

import de.mhus.sop.api.SopApi;
import de.mhus.sop.api.aaa.Account;
import de.mhus.sop.api.aaa.AuthorizationSource;

public class FileMappingSource implements AuthorizationSource {

	@Override
	public Boolean hasResourceAccess(SopApi api, Account account, String mappingName, String id, String action) {

		return null;
	}

}
