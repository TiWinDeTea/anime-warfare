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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.menu.event.GameRoomEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The game room controller.
 *
 * @author Benoît CORTIER
 */
public class GameRoomController {
	@FXML
	private VBox usersList;

	@FXML
	private BorderPane borderPane;

	private List<Label> userNamesLabels = new ArrayList<>();

	public BorderPane getBorderPane() {
		return this.borderPane;
	}

	@FXML
	void handleQuit(ActionEvent event) {
		// TODO: disconnect from network
		EventDispatcher.getInstance().fire(new GameRoomEvent(GameRoomEvent.Type.QUIT));
	}
}

