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

package org.tiwindetea.animewarfare.gui.game;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.tiwindetea.animewarfare.gui.AnimationsManager;
import org.tiwindetea.animewarfare.gui.GUIState;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.io.IOException;

/**
 * Game state.
 *
 * @author Benoît CORTIER
 */
public class GameState extends GUIState {
	private static AnchorPane gameLayout;
	private static GameLayoutController gameLayoutController;
	private static FadeTransition fadeTransition;

	static {
		FXMLLoader gameLayoutLoader = new FXMLLoader();
		gameLayoutLoader.setLocation(GameState.class.getResource("GameLayout.fxml"));

		try {
			GameState.gameLayout = gameLayoutLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GameState.gameLayoutController = gameLayoutLoader.getController();

		GameState.fadeTransition = new FadeTransition(Duration.millis(400), GameState.gameLayout);
		GameState.fadeTransition.setFromValue(0.0);
		GameState.fadeTransition.setToValue(1.0);
		GameState.fadeTransition.setCycleCount(1);
		GameState.fadeTransition.setAutoReverse(false);
	}

	public static void initStaticFields() {
	}

	public GameState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {

		// place the node in the root layout.
		this.rootLayout.setCenter(GameState.gameLayout);

		// play the fade transition
		AnimationsManager.conditionalPlay(GameState.fadeTransition);

		GameState.gameLayoutController.initStart();

		// listen events.
	}

	@Override
	public void onExit() {
		GameState.gameLayoutController.clearEnd();

		// stop listening events.
	}
}
