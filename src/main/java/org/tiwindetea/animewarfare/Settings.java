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

package org.tiwindetea.animewarfare;

import org.tiwindetea.animewarfare.util.PropertiesReader;

import java.util.prefs.Preferences;

/**
 * Class containing static fields about user's settings.
 *
 * @author Benoît CORTIER
 */
public class Settings {
	private static final PropertiesReader PROPERTIES_READER
			= new PropertiesReader("org.tiwindetea.animewarfare.Settings");

	private static boolean enableAnimationEffects;
	private static int autoSaveInterval;
	private static String playerName;

	static {
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);
		Settings.playerName =
				prefs.get("playerName", PROPERTIES_READER.getString("default.input.playername"));
		Settings.autoSaveInterval = Integer.valueOf(
				prefs.get("autoSaveInterval", PROPERTIES_READER.getString("default.interval.autosave"))
		);
		Settings.enableAnimationEffects = Boolean.valueOf(
				prefs.get("enableAnimationEffects", PROPERTIES_READER.getString("default.enable.animationeffects"))
		);
	}

	public static void savePreferences() {
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);
		prefs.put("playerName", Settings.playerName);
		prefs.put("autoSaveInterval", String.valueOf(Settings.autoSaveInterval));
		prefs.put("enableAnimationEffects", String.valueOf(Settings.enableAnimationEffects));
	}

	public static boolean areAnimationEffectsEnabled() {
		return enableAnimationEffects;
	}

	public static int getAutoSaveInterval() {
		return autoSaveInterval;
	}

	public static String getPlayerName() {
		return playerName;
	}

	public static void setEnableAnimationEffects(boolean enableAnimationEffects) {
		Settings.enableAnimationEffects = enableAnimationEffects;
	}

	public static void setAutoSaveInterval(int autoSaveInterval) {
		if (autoSaveInterval < 0) {
			Settings.autoSaveInterval = 0;
		} else {
			Settings.autoSaveInterval = autoSaveInterval;
		}
	}

	public static void setPlayerName(String playerName) {
		Settings.playerName = playerName;
	}
}
