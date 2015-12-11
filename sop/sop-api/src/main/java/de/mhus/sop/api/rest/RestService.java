package de.mhus.sop.api.rest;

import java.util.List;
import java.util.Map;

import de.mhus.sop.api.SApi;


public interface RestService extends SApi {

	Node lookup(List<String> parts, String lastNodeId, CallContext context)
			throws Exception;

	Map<String, RestNodeService> getRestNodeRegistry();
	
}
