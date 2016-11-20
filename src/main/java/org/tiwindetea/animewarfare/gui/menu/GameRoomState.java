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
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEvent;
import org.tiwindetea.animewarfare.gui.menu.event.GameRoomEvent;
import org.tiwindetea.animewarfare.gui.menu.event.GameRoomEventListener;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Game room menu state.
 *
 * @author Benoît CORTIER
 */
public class GameRoomState extends MenuState implements GameRoomEventListener {
	private static AnchorPane gameRoom;
	private static GameRoomController gameRoomController;
	private static FadeTransition fadeTransition;

	static {
		FXMLLoader gameRoomLoader = new FXMLLoader();
		gameRoomLoader.setLocation(GameRoomController.class.getResource("GameRoom.fxml"));
		gameRoomLoader.setResources(ResourceBundle.getBundle("org.tiwindetea.animewarfare.gui.menu.GameRoomController",
				Locale.getDefault()));
		try {
			GameRoomState.gameRoom = gameRoomLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameRoomState.gameRoomController = gameRoomLoader.getController();
		GameRoomState.gameRoomController.getBorderPane().setCenter(GlobalChat.getChat());

		GameRoomState.fadeTransition = new FadeTransition(Duration.millis(400), GameRoomState.gameRoom);
		GameRoomState.fadeTransition.setFromValue(0.0);
		GameRoomState.fadeTransition.setToValue(1.0);
		GameRoomState.fadeTransition.setCycleCount(1);
		GameRoomState.fadeTransition.setAutoReverse(false);
	}

	public GameRoomState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {
		// place the node in the root layout.
		this.rootLayout.setCenter(GameRoomState.gameRoom);
		GlobalChat.getChatController().clear();

		// listen events.
		EventDispatcher.getInstance().addListener(GameRoomEvent.class, this);
	}

	@Override
	public void onExit() {
		// stop listening events.
		EventDispatcher.getInstance().removeListener(GameRoomEvent.class, this);
	}

	@Override
	public void handleGameRoomQuit() {
		this.nextState = new ServersListState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}
}
