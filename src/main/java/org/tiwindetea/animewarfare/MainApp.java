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

import com.esotericsoftware.minlog.Log;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.lomadriel.lfc.event.EventDispatcher;
import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEvent;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEventListener;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.game.GComponent;
import org.tiwindetea.animewarfare.gui.game.GameState;
import org.tiwindetea.animewarfare.gui.menu.GameRoomState;
import org.tiwindetea.animewarfare.gui.menu.MainMenuState;
import org.tiwindetea.animewarfare.gui.menu.ServersListState;
import org.tiwindetea.animewarfare.gui.menu.SettingsMenuState;
import org.tiwindetea.animewarfare.net.GameClient;
import org.tiwindetea.animewarfare.net.GameServer;
import org.tiwindetea.animewarfare.settings.Settings;
import org.tiwindetea.animewarfare.settings.SettingsUpdatedEvent;
import org.tiwindetea.animewarfare.settings.SettingsUpdatedEventListener;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main application class.
 * This class contains the main function to start the game.
 *
 * @author Benoît CORTIER
 */
public class MainApp extends Application implements AskMenuStateUpdateEventListener,
		QuitApplicationEventListener, SettingsUpdatedEventListener {
	private static final ResourceBundle BUNDLE
			= ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.MainApp");

	private static GameClient gameClient = new GameClient();
	private static GameServer gameServer = new GameServer();

	private Stage primaryStage;

	@FXML
	private BorderPane rootLayout;

	private DefaultStateMachine guiStateMachine;

	@Override
	public void start(Stage stage) {
		EventDispatcher.getInstance().addListener(AskMenuStateUpdateEvent.class, this);
		EventDispatcher.getInstance().addListener(QuitApplicationEvent.class, this);
		EventDispatcher.getInstance().addListener(SettingsUpdatedEvent.class, this);

		Font.loadFont(MainApp.class.getResource("gui/fonts/LadylikeBB.ttf").toExternalForm(), 13);

		Settings.initStaticFields();
		Locale.setDefault(Locale.forLanguageTag(Settings.getLanguageTag()));
		stage.setFullScreen(Settings.isFullscreenEnabled());

		this.primaryStage = stage;
		this.primaryStage.setTitle(BUNDLE.getString("title"));

		initRootLayout();
		GlobalChat.getChatController().initShortcuts(this.primaryStage);
		MainMenuState.initStaticFields();
		SettingsMenuState.initStaticFields();
		ServersListState.initStaticFields();
		GameRoomState.initStaticFields();
		GameState.initStaticFields();
		GComponent.initSubFactories();

		this.guiStateMachine = new DefaultStateMachine(new MainMenuState(this.rootLayout));

		this.primaryStage.setOnCloseRequest((event) -> EventDispatcher.send(new QuitApplicationEvent()));
	}

    private void initRootLayout() {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("RootLayout.fxml"));
		loader.setController(this);
		try {
			// Show the scene containing the root layout.
			Scene scene = new Scene(loader.load());
			scene.getRoot().getStylesheets().add(getClass().getResource("gui/css/menu.css").toExternalForm());
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private void onQuit() {
	    EventDispatcher.getInstance().removeListener(AskMenuStateUpdateEvent.class, this);
		EventDispatcher.getInstance().removeListener(QuitApplicationEvent.class, this);
		EventDispatcher.getInstance().removeListener(SettingsUpdatedEvent.class, this);

		// stop network things.
		MainApp.getGameClient().disconnect();
		if (MainApp.getGameServer().isRunning()) {
			MainApp.getGameServer().stop();
		}
	}

	@Override
	public void handleAskMenuStateUpdate() {
		this.guiStateMachine.update();
	}

	public static void initANewGameServer() {
		MainApp.gameServer = new GameServer();
	}

	public static GameServer getGameServer() {
		return MainApp.gameServer;
	}

	public static GameClient getGameClient() {
		return MainApp.gameClient;
	}

	public static void main(String[] args) {
		Log.set(Log.LEVEL_DEBUG);

		launch(args);
	}

	@Override
	public void handleQuitApplication() {
		this.primaryStage.close();
		onQuit();
	}

	@Override
	public void onLanguageUpdated() {
		Locale.setDefault(Locale.forLanguageTag(Settings.getLanguageTag()));
		// TODO: reload some FXML.
	}

	@Override
	public void onFullscreenUpdated() {
		this.primaryStage.setFullScreen(Settings.isFullscreenEnabled());
	}
}
