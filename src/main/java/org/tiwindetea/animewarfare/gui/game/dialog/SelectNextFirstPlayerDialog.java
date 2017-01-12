
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

package org.tiwindetea.animewarfare.gui.game.dialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.gui.PaperGridPane;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetFirstPlayerSelection;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;
import java.util.List;

/**
 * @author Benoît CORTIER
 */
public class SelectNextFirstPlayerDialog extends GameDialog {
	@FXML
	private PaperGridPane paperGridPane;

	public SelectNextFirstPlayerDialog(VBox overlay, List<GameClientInfo> selectablePlayers) {
		super(overlay);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("SelectNextFirstPlayerDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle(getClass().getName()));
		loader.setController(this);

		try {
			addElement(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				int index = i * 2 + j;
				if (index < MainApp.getGameClient().getRoom().getNumberOfExpectedPlayers()) {
					GameClientInfo clientInfo = selectablePlayers.get(index);
					PaperButton button = new PaperButton();
					if (MainApp.getGameClient().getClientInfo().equals(clientInfo)) {
						button.setText("You"); // TODO: extenalize
					} else {
						button.setText(GlobalChat.getClientFaction(clientInfo).toString());
					}
					button.setPrefHeight(30);
					button.setPrefWidth(150);
					button.setOnAction(a -> {
						MainApp.getGameClient().send(new NetFirstPlayerSelection(clientInfo.getId()));
						this.close();
					});

					GridPane.setConstraints(button, i + 1, j);
					this.paperGridPane.getChildren().add(button);
				}
			}
		}
	}
}
