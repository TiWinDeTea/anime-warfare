package org.tiwindetea.animewarfare.settings;

import java.util.EventListener;

public interface SettingsUpdatedEventListener extends EventListener {
	void onLanguageUpdated();

	void onFullscreenUpdated();
}
