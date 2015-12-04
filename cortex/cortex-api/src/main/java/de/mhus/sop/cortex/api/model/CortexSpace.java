package de.mhus.sop.cortex.api.model;

import java.io.IOException;
import java.io.Serializable;

import de.mhus.lib.core.MXml;

public class CortexSpace implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String shortcut;
	protected String title;
	protected String description;
	protected String model;
	private Model modelModel;
	
	public String getShortcut() {
		return shortcut;
	}

	public synchronized Model getModel() throws Exception {
		if ( modelModel == null) modelModel = new Model(MXml.loadXml(model).getDocumentElement());
		return modelModel;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		if (shortcut == null) shortcut = "";
		if (title == null) title = "";
		if (description == null) description = "";
		if (model == null) model = "";
		out.writeUTF(shortcut);
		out.writeUTF(title);
		out.writeUTF(description);
		out.writeUTF(model);
	}
	
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		shortcut = in.readUTF();
		title = in.readUTF();
		description = in.readUTF();
		model = in.readUTF();
	}
	
}
