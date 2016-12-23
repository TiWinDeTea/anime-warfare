package org.tiwindetea.animewarfare.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Extension of ResourceBundle.Control to support string encoding. (Default UTF-8)
 */
class EncodedControl extends ResourceBundle.Control {
	public final static String DEFAULT_ENCODING = "UTF-8";

	private final String encoding;

	public EncodedControl() {
		this(DEFAULT_ENCODING);
	}

	public EncodedControl(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public ResourceBundle newBundle(String baseName,
	                                Locale locale,
	                                String format,
	                                ClassLoader loader,
	                                boolean reload) throws IllegalAccessException, InstantiationException, IOException {
		String bundleName = toBundleName(baseName, locale);
		ResourceBundle bundle = null;
		if (format.equals("java.class")) {
			try {
				@SuppressWarnings(
						{"unchecked"})
				Class<? extends ResourceBundle> bundleClass = (Class<? extends ResourceBundle>) loader.loadClass(
						bundleName);

				// If the class isn't a ResourceBundle subclass, throw a
				// ClassCastException.
				if (ResourceBundle.class.isAssignableFrom(bundleClass)) {
					bundle = bundleClass.newInstance();
				} else {
					throw new ClassCastException(bundleClass.getName() + " cannot be cast to ResourceBundle");
				}
			} catch (ClassNotFoundException ignored) {
			}
		} else if (format.equals("java.properties")) {
			final String resourceName = toResourceName(bundleName, "properties");
			final ClassLoader classLoader = loader;
			final boolean reloadFlag = reload;
			InputStream stream;
			try {
				stream = AccessController.doPrivileged((PrivilegedExceptionAction<InputStream>) () -> {
					InputStream is = null;
					if (reloadFlag) {
						URL url = classLoader.getResource(resourceName);
						if (url != null) {
							URLConnection connection = url.openConnection();
							if (connection != null) {
								// Disable caches to get fresh data for
								// reloading.
								connection.setUseCaches(false);
								is = connection.getInputStream();
							}
						}
					} else {
						is = classLoader.getResourceAsStream(resourceName);
					}
					return is;
				});
			} catch (PrivilegedActionException e) {
				throw (IOException) e.getException();
			}
			if (stream != null) {
				try (InputStreamReader isr = new InputStreamReader(stream, this.encoding)) {
					bundle = new PropertyResourceBundle(isr);
				}
			}
		} else {
			throw new IllegalArgumentException("unknown format: " + format);
		}
		return bundle;
	}
}
