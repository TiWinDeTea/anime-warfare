package org.tiwindetea.animewarfare.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Properties reader using resources bundle.
 *
 * @author Benoît CORTIER
 */
public class PropertiesReader {
	private final ResourceBundle resourceBundle;

	/**
	 * Contruct a properties reader.
	 *
	 * @param bundleName the name of the bundle.
	 * @param locale the local to use.
	 */
	public PropertiesReader(String bundleName, Locale locale) {
		this.resourceBundle = ResourceBundle.getBundle(bundleName, locale);
	}

	/**
	 * Contruct a properties reader using the default locale.
	 *
	 * @param bundleName the name of the bundle.
	 */
	public PropertiesReader(String bundleName) {
		this(bundleName, Locale.getDefault());
	}

	/**
	 * Retrieve the string matching the given key.
	 *
	 * @param key the key
	 * @return the string
	 */
	public String getString(String key) {
		return this.resourceBundle.getString(key);
	}
}

