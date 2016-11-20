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

package org.tiwindetea.animewarfare.gui.menu;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEvent;
import org.tiwindetea.animewarfare.gui.menu.event.SettingsMenuEvent;
import org.tiwindetea.animewarfare.gui.menu.event.SettingsMenuEventListener;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Settings menu state.
 *
 * @author Benoît CORTIER
 */
public class SettingsMenuState extends MenuState implements SettingsMenuEventListener {
	private static AnchorPane settingsMenu;
	private static SettingsMenuController settingsMenuController;

	private static FadeTransition fadeTransition;

	static {
		FXMLLoader settingsMenuLoader = new FXMLLoader();
		settingsMenuLoader.setLocation(MainMenuController.class.getResource("SettingsMenu.fxml"));
		settingsMenuLoader.setResources(ResourceBundle.getBundle("org.tiwindetea.animewarfare.gui.menu.SettingsMenuController",
				Locale.getDefault()));
		try {
			SettingsMenuState.settingsMenu = settingsMenuLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SettingsMenuState.settingsMenuController = settingsMenuLoader.getController();

		SettingsMenuState.fadeTransition = new FadeTransition(Duration.millis(400), SettingsMenuState.settingsMenu);
		SettingsMenuState.fadeTransition.setFromValue(0.0);
		SettingsMenuState.fadeTransition.setToValue(1.0);
		SettingsMenuState.fadeTransition.setCycleCount(1);
		SettingsMenuState.fadeTransition.setAutoReverse(false);
	}

	public SettingsMenuState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {
		// place the node in the root layout.
		this.rootLayout.setCenter(SettingsMenuState.settingsMenu);

		// play the fade transition
		SettingsMenuState.fadeTransition.play();

		// listen events.
		EventDispatcher.getInstance().addListener(SettingsMenuEvent.class, this);
	}

	@Override
	public void onExit() {
		// stop listening events.
		EventDispatcher.getInstance().removeListener(SettingsMenuEvent.class, this);
	}

	@Override
	public void handleSettingsMenuQuit() {
		this.nextState = new MainMenuState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}
}
