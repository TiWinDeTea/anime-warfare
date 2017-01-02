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
import org.tiwindetea.animewarfare.gui.AnimationsManager;
import org.tiwindetea.animewarfare.gui.GUIState;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.menu.event.MainMenuEvent;
import org.tiwindetea.animewarfare.gui.menu.event.MainMenuEventListener;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * Servers list menu state.
 *
 * @author Benoît CORTIER
 */
public class MainMenuState extends GUIState implements MainMenuEventListener {
	private static AnchorPane mainMenu;
	private static MainMenuController mainMenuController;

	private static FadeTransition fadeTransition;

	static {
		FXMLLoader mainMenuLoader = new FXMLLoader();
		mainMenuLoader.setLocation(MainMenuController.class.getResource("MainMenu.fxml"));
		mainMenuLoader.setResources(ResourceBundleHelper.getBundle(
				"org.tiwindetea.animewarfare.gui.menu.MainMenuController"));
		try {
			MainMenuState.mainMenu = mainMenuLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainMenuState.mainMenuController = mainMenuLoader.getController();

		MainMenuState.fadeTransition = new FadeTransition(Duration.millis(400), MainMenuState.mainMenu);
		MainMenuState.fadeTransition.setFromValue(0.0);
		MainMenuState.fadeTransition.setToValue(1.0);
		MainMenuState.fadeTransition.setCycleCount(1);
		MainMenuState.fadeTransition.setAutoReverse(false);
	}

	public MainMenuState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {
		// place the node in the root layout.
		this.rootLayout.setCenter(MainMenuState.mainMenu);

		// play the fade transition
		AnimationsManager.conditionalPlay(MainMenuState.fadeTransition);

		// listen events.
		EventDispatcher.getInstance().addListener(MainMenuEvent.class, this);
	}

	@Override
	public void onExit() {
		// stop listening events.
		EventDispatcher.getInstance().removeListener(MainMenuEvent.class, this);
	}

	@Override
	public void handlePlay() {
		this.nextState = new ServersListState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}

	@Override
	public void handleGoSettings() {
		this.nextState = new SettingsMenuState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}

	@Override
	public void handleQuitMainMenu() {
		EventDispatcher.getInstance().fire(new QuitApplicationEvent());
	}
}
