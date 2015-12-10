package de.mhus.sop.api.rest;



public interface RestNodeService extends Node {

	String ROOT_ID = "";

	String[] getParentNodeIds();
	
	String getNodeId();
			
}
