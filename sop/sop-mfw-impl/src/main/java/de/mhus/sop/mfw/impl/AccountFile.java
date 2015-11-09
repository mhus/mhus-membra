package de.mhus.sop.mfw.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.aaa.Account;
import de.mhus.sop.mfw.api.aaa.Ace;

public class AccountFile extends MLog implements Account {

	// TODO Optimize performance finding ACEs, use indexes !!!!
	
	private Document doc;
	private String account;
	private boolean valide;
	private File file;
	private long modified;
	private String name;
	
	private LinkedList<Ace> acl = new LinkedList<Ace>();
	private String password = null;
	private long timeout;
	private Boolean isPasswordValidated = null;
	
	public AccountFile(File f, String account) throws ParserConfigurationException, SAXException, IOException {
		FileInputStream is = new FileInputStream(f);
		doc = MXml.loadXml(is);
		is.close();
		this.account = account;
		valide = account != null;
		file = f;
		modified = f.lastModified();
		
		
		Element pwE = MXml.getElementByPath(doc.getDocumentElement(),"password");
		if (pwE != null)
				password = pwE.getAttribute("plain");
		name = MXml.getElementByPath(doc.getDocumentElement(),"information").getAttribute("name");
		
		timeout = MCast.tolong( doc.getDocumentElement().getAttribute("timeout"), 0);
		
		
		Element xmlAcl = MXml.getElementByPath(doc.getDocumentElement(), "acl");
		for (Element xmlAce : MXml.getLocalElementIterator(xmlAcl,"ace")) {
			Ace ace = new Ace(
					account, 
					xmlAce.getAttribute("type"),
					toUUID(xmlAce.getAttribute("target")),
					xmlAce.getAttribute("key"),
					xmlAce.getAttribute("rights")
					);
			acl.add(ace);
		}
	}

	private UUID toUUID(String value) {
		if (MString.isEmpty(value)) return null;
		try {
			return UUID.fromString(value);
		} catch (IllegalArgumentException e) {}
		return null;
	}

	@Override
	public String getAccount() {
		return account;
	}

	@Override
	public boolean isValide() {
		return valide;
	}

	@Override
	public synchronized boolean validatePassword(String password) {
		if (isPasswordValidated == null) {
			try {
				if (password != null) {
					boolean out = validatePasswordInternal(password);
					if (out) {
						isPasswordValidated = true;
						return true;
					}
				}
				isPasswordValidated = Mfw.getApi(MfwApi.class).validatePassword(this, password);
			} catch (Throwable t) {
				log().w("validatePassword",account,t);
			}
		}
		return isPasswordValidated == null ? false : isPasswordValidated;
	}

	public boolean isChanged() {
		return !file.exists() || modified != file.lastModified();
	}

	public Ace findGlobalAce(String key) {
		for (Ace ace :acl)
			if ( key.equals(ace.getKey()))
				return ace;
		return null;
	}

	public Ace findAce(String type, UUID id) {
		for (Ace ace :acl)
			if (type.equals(ace.getType()) && id.equals(ace.getTarget()))
				return ace;
		return null;
	}

	public List<Ace> findGlobalAcesForAccount(String key) {
		LinkedList<Ace> out = new LinkedList<Ace>();
		for (Ace ace :acl)
			if (key == null || key.equals(ace.getKey()))
				out.add(ace);
		return out;
	}

	public List<Ace> findAcesForAccount(String type) {
		LinkedList<Ace> out = new LinkedList<Ace>();
		for (Ace ace :acl)
			if (type == null || type.equals(ace.getType()))
				out.add(ace);
		return out;
	}

	public String toString() {
		return account + " " + name;
	}
	
	@Override
	public boolean isSyntetic() {
		return false;
	}

	@Override
	public String getDisplayName() {
		return name;
	}
	
	public long getTimeout() {
		return timeout;
	}

	public boolean validatePasswordInternal(String password) {
		return this.password.equals(password);
	}

}
