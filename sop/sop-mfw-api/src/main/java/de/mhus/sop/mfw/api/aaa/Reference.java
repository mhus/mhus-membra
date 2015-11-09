package de.mhus.sop.mfw.api.aaa;

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.model.DbMetadata;

public class Reference<T extends DbMetadata> extends MLog {

	public enum TYPE {CHILD,PARENT,OTHER}
	private T object;
	private TYPE type;

	public Reference(T object, TYPE type) {
		this.object = object;
		this.type = type;
	}
	
	public T getObject() {
		return object;
	}

	public TYPE getType() {
		return type;
	}
	
	public void doDelete() throws MException {
		log().d("start delete",object,type);
		Mfw.getApi(MfwApi.class).onDelete(object);
		log().d("delete",object,type);
		object.delete();
	}
}
