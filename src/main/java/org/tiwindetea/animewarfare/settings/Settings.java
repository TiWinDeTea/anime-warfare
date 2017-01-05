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

package org.tiwindetea.animewarfare.settings;

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Class containing static fields about user's settings.
 *
 * @author Benoît CORTIER
 */
public class Settings {
	private static final ResourceBundle BUNDLE
			= ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.settings.Settings");

	private static boolean enableAnimationEffects;
	private static boolean enableFullscreen;
	private static int autoSaveInterval;
	private static String playerName;
	private static String languageTag;
	private static boolean languageChanged;
	private static boolean fullscreenChanged;

	static {
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);

		Settings.playerName =
				prefs.get("playerName", BUNDLE.getString("default.input.playername"));

		Settings.autoSaveInterval = Integer.valueOf(
				prefs.get("autoSaveInterval", BUNDLE.getString("default.interval.autosave"))
		).intValue();

		Settings.enableAnimationEffects = Boolean.valueOf(
				prefs.get("enableAnimationEffects", BUNDLE.getString("default.enable.animationeffects"))
		).booleanValue();

		Settings.enableFullscreen = Boolean.valueOf(
				prefs.get("enableFullscreen", BUNDLE.getString("default.enable.fullscreen"))
		).booleanValue();

		Settings.languageTag = prefs.get("languageTag", BUNDLE.getString("default.language.tag"));
	}

	public static void savePreferences() {
		Preferences prefs = Preferences.userNodeForPackage(Settings.class);
		prefs.put("playerName", Settings.playerName);
		prefs.put("autoSaveInterval", String.valueOf(Settings.autoSaveInterval));
		prefs.put("enableAnimationEffects", String.valueOf(Settings.enableAnimationEffects));
		prefs.put("enableFullscreen", String.valueOf(Settings.enableFullscreen));
		prefs.put("languageTag", Settings.languageTag);

		if (Settings.languageChanged) {
			EventDispatcher.getInstance().fire(new SettingsUpdatedEvent(SettingsUpdatedEvent.Type.LANGUAGE));
			Settings.languageChanged = false;
		}

		if (Settings.fullscreenChanged) {
			EventDispatcher.getInstance().fire(new SettingsUpdatedEvent(SettingsUpdatedEvent.Type.FULLSCREEN));
			Settings.fullscreenChanged = false;
		}
	}

	public static boolean areAnimationEffectsEnabled() {
		return enableAnimationEffects;
	}

	public static boolean isFullscreenEnabled() {
		return enableFullscreen;
	}

	public static int getAutoSaveInterval() {
		return autoSaveInterval;
	}

	public static String getPlayerName() {
		return playerName;
	}

	public static String getLanguageTag() {
		return languageTag;
	}

	public static void setEnableAnimationEffects(boolean enableAnimationEffects) {
		Settings.enableAnimationEffects = enableAnimationEffects;
	}

	public static void setEnableFullscreen(boolean enableFullscreen) {
		Settings.enableFullscreen = enableFullscreen;
		Settings.fullscreenChanged = true;
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

	public static void setLanguageTag(String languageTag) {
		if (Locale.forLanguageTag(languageTag) != null && !Settings.languageTag.equals(languageTag)) {
			Settings.languageTag = languageTag;
			Settings.languageChanged = true;
		}
	}

	public static void initStaticFields() {
		// Init static fields
	}
}
