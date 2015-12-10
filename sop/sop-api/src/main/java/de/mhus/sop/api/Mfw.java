package de.mhus.sop.api;

import java.util.HashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.MJms;
import de.mhus.lib.karaf.jms.JmsUtil;

public class Mfw {

	public static final String PARAMETERS = "parameter.";

	public static final String DEFAULT_GROUP = "default";

	public static final String PARAM_OPERATION_PATH = "path";
	public static final String OPERATION_LIST = "_list";
	public static final String OPERATION_INFO = "_get";
	public static final String PARAM_OPERATION_ID = "id";

	public static final String PARAM_AAA_TICKET = "_aaa_ticket";

	public static final long MAX_MSG_BYTES = 1024 * 1024 * 100; // 100 MB

	public static final String PARAM_SUCCESSFUL = "successful";

	public static final String PARAM_MSG = "msg";

	public static final String PARAM_RC = "rc";
	
	private static HashMap<String, Container> apiCache = new HashMap<>();

	@SuppressWarnings("unchecked")
	public synchronized static <T extends MApi> T getApi(Class<? extends T> ifc) {
		Container cached = apiCache.get(ifc.getCanonicalName());
		if (cached != null) {
			if (cached.bundle.getState() != Bundle.ACTIVE || cached.modified != cached.bundle.getLastModified()) {
				apiCache.remove(cached.ifc.getCanonicalName());
				cached = null;
			}
		}
		
		if (cached == null) {
			BundleContext context = FrameworkUtil.getBundle(Mfw.class).getBundleContext();
			ServiceReference<? extends T> ref = context.getServiceReference(ifc);
			if (ref == null) throw new NotFoundException("api reference not found",ifc);
			T obj = context.getService(ref);
			if (obj == null) throw new NotFoundException("api service not found",ifc);
			cached = new Container();
			cached.bundle = ref.getBundle();
			cached.api = obj;
			cached.ifc = ifc;
			cached.modified = cached.bundle.getLastModified();
			apiCache.put(ifc.getCanonicalName(), cached);
		}
		return (T) cached.api;
	}
	
	private static class Container {

		public long modified;
		public Class<? extends MApi> ifc;
		public MApi api;
		public Bundle bundle;
		
	}
	
	public static String decodePassword(String password) {
		if (password == null) return null;
		if (password.length() < 2 || !password.startsWith("`")) return password;
		if (password.charAt(1) == 'A') {
			StringBuffer out = new StringBuffer();
			for (int i = 2; i < password.length(); i++) {
				char c = password.charAt(i);
				switch (c) {
				case '0': c = '9'; break;
				case '1': c = '0'; break;
				case '2': c = '1'; break;
				case '3': c = '2'; break;
				case '4': c = '3'; break;
				case '5': c = '4'; break;
				case '6': c = '5'; break;
				case '7': c = '6'; break;
				case '8': c = '7'; break;
				case '9': c = '8'; break;
				}
				out.append(c);
			}
			return out.toString();
		}
		return null;
	}

	public static String asEncodePassword(String alreadyEncoded) {
		return "`A" +alreadyEncoded;
	}

	public static int getPageFromSearch(String search) {
		if (MString.isEmpty(search) || !search.startsWith("page:"))
			return 0;
		search = search.substring(5);
		if (search.indexOf(',') >= 0)
			return MCast.toint(MString.beforeIndex(search, ','), 0);
		return MCast.toint(search, 0);
	}

	public static String getFilterFromSearch(String search) {
		if (MString.isEmpty(search))
			return null;
		if (search.startsWith("page:")) {
			if (search.indexOf(',') >= 0)
				return MString.afterIndex(search, ',');
			return null;
		}
		return search;
	}

	public static JmsConnection getDefaultJmsConnection() {
		return JmsUtil.getConnection(getDefaultJmsConnectionName());
	}

	public static String getDefaultJmsConnectionName() {
		return "mhus"; //TODO configurable
	}

}
