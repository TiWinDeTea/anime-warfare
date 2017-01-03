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
import org.tiwindetea.animewarfare.gui.menu.event.ServersListEvent;
import org.tiwindetea.animewarfare.gui.menu.event.ServersListEventListener;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * Servers list menu state.
 *
 * @author Benoît CORTIER
 */
public class ServersListState extends GUIState implements ServersListEventListener {
	private static AnchorPane serversList;
	private static ServersListController serversListController;
	private static FadeTransition fadeTransition;

	static {
		FXMLLoader serversListLoader = new FXMLLoader();
		serversListLoader.setLocation(ServersListController.class.getResource("ServersList.fxml"));
		serversListLoader.setResources(
				ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.menu.ServersListController")
		);

		try {
			ServersListState.serversList = serversListLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ServersListState.serversListController = serversListLoader.getController();

		ServersListState.fadeTransition = new FadeTransition(Duration.millis(400), ServersListState.serversList);
		ServersListState.fadeTransition.setFromValue(0.0);
		ServersListState.fadeTransition.setToValue(1.0);
		ServersListState.fadeTransition.setCycleCount(1);
		ServersListState.fadeTransition.setAutoReverse(false);
	}

	public static void initStaticFields() {
	}

	public ServersListState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {
		// place the node in the root layout.
		ServersListState.serversListController.handleRefresh();
		this.rootLayout.setCenter(ServersListState.serversList);

		// play the fade transition
		AnimationsManager.conditionalPlay(ServersListState.fadeTransition);

		// listen events.
		EventDispatcher.getInstance().addListener(ServersListEvent.class, this);
	}

	@Override
	public void onExit() {
		// stop listening events.
		EventDispatcher.getInstance().removeListener(ServersListEvent.class, this);
	}

	@Override
	public void handleJoinServer() {
		this.nextState = new GameRoomState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}

	@Override
	public void handleServersListQuit() {
		this.nextState = new MainMenuState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}
}
