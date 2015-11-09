package de.mhus.sop.mfw.api.util;

import java.util.List;
import java.util.UUID;

import de.mhus.lib.errors.MException;
import de.mhus.sop.mfw.api.Mfw;
import de.mhus.sop.mfw.api.MfwApi;
import de.mhus.sop.mfw.api.model.DbMetadata;
import de.mhus.sop.mfw.api.model.ObjectParameter;

public class ObjectUtil {
	
	public static ObjectParameter getParameter(Class<?> type, UUID id, String key) throws MException {
		return Mfw.getApi(MfwApi.class).getParameter(type, id, key);
	}

	public static void setParameter(Class<?> type, UUID id, String key, String value) throws MException {
		Mfw.getApi(MfwApi.class).setParameter(type, id, key, value);
	}

	public static void deleteAll(Class<?> type, UUID id) throws MException {
		Mfw.getApi(MfwApi.class).deleteParameters(type, id);
	}

	public static List<ObjectParameter> getParameters(Class<?> type, String key, String value) throws MException {
		return Mfw.getApi(MfwApi.class).getParameters(type, key, value);
	}

	public static String getRecursiveValue(Class<? extends DbMetadata> clazz, UUID id, String key, String def) throws MException {
		DbMetadata obj = Mfw.getApi(MfwApi.class).getObject(clazz, id);
		return getRecursiveValue(obj, key, def);
	}
	
	public static String getRecursiveValue(DbMetadata obj, String key, String def) {
		ObjectParameter out = null;
		try {
			out = Mfw.getApi(MfwApi.class).getRecursiveParameter(obj, key);
		} catch (MException e) {
			
		}
		 if (out == null || out.getValue() == null) return def;
		 return out.getValue();
	}	

}
