package org.tiwindetea.animewarfare.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleHelper {
	public static final String DEFAULT_ENCODING = "UTF-8";

	public static ResourceBundle getBundle(String bundleName, Locale locale, String encoding) {
		try (URLClassLoader loader = new URLClassLoader(new URL[]{new File(".").toURI().toURL()})) {
			return ResourceBundle.getBundle(bundleName,
					locale,
					loader,
					new EncodedControl(encoding));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ResourceBundle getBundle(String bundleName, Locale locale) {
		return getBundle(bundleName, locale, DEFAULT_ENCODING);
	}

	public static ResourceBundle getBundle(String bundleName, String encoding) {
		return getBundle(bundleName, Locale.getDefault(), encoding);
	}

	public static ResourceBundle getBundle(String bundleName) {
		return getBundle(bundleName, Locale.getDefault(), DEFAULT_ENCODING);
	}
}
