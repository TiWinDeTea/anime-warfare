package org.tiwindetea.animewarfare.settings;

import org.lomadriel.lfc.event.Event;

public class SettingsUpdatedEvent implements Event<SettingsUpdatedEventListener> {
	public enum Type {
		LANGUAGE {
			@Override
			void handle(SettingsUpdatedEventListener listener) {
				listener.onLanguageUpdated();
			}
		},
		FULLSCREEN {
			@Override
			void handle(SettingsUpdatedEventListener listener) {
				listener.onFullscreenUpdated();
			}
		};

		abstract void handle(SettingsUpdatedEventListener listener);
	}

	private final Type type;

	public SettingsUpdatedEvent(Type type) {
		this.type = type;
	}

	@Override
	public void notify(SettingsUpdatedEventListener listener) {
		this.type.handle(listener);
	}
}
