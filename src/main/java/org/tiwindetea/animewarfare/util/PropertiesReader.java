////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

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

