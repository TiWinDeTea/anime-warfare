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
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperGridPane;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * @author Benoît CORTIER
 */
public class BattleResultDialog extends GameDialog {
	@FXML
	private PaperGridPane paperGridPane;

	public BattleResultDialog(VBox overlay, BattleNetevent battleNetevent) {
		super(overlay);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("BattleResultDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle(getClass().getName()));
		loader.setController(this);

		try {
			addElement(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}

		Label attackerSide = new Label(GlobalChat.getClientFaction(battleNetevent.getAttacker()).toString());
		GridPane.setConstraints(attackerSide, 1, 0);
		this.paperGridPane.getChildren().add(attackerSide);

		Label defenderSide = new Label(GlobalChat.getClientFaction(battleNetevent.getDefender()).toString());
		GridPane.setConstraints(defenderSide, 2, 0);
		this.paperGridPane.getChildren().add(defenderSide);

		Label attackerAttack = new Label(battleNetevent.getAttack().get(battleNetevent.getAttacker()).toString());
		GridPane.setConstraints(attackerAttack, 1, 1);
		this.paperGridPane.getChildren().add(attackerAttack);

		Label defenderAttack = new Label(battleNetevent.getAttack().get(battleNetevent.getDefender()).toString());
		GridPane.setConstraints(defenderAttack, 2, 1);
		this.paperGridPane.getChildren().add(defenderAttack);

		Label attackerDeads = new Label(battleNetevent.getNumberOfDeads().get(battleNetevent.getAttacker()).toString());
		GridPane.setConstraints(attackerDeads, 1, 2);
		this.paperGridPane.getChildren().add(attackerDeads);

		Label defenderDeads = new Label(battleNetevent.getNumberOfDeads().get(battleNetevent.getDefender()).toString());
		GridPane.setConstraints(defenderDeads, 2, 2);
		this.paperGridPane.getChildren().add(defenderDeads);

		Label attackerWoundeds = new Label(battleNetevent.getNumberOfWoundeds().get(battleNetevent.getAttacker()).toString());
		GridPane.setConstraints(attackerWoundeds, 1, 3);
		this.paperGridPane.getChildren().add(attackerWoundeds);

		Label defenderWoundeds = new Label(battleNetevent.getNumberOfWoundeds().get(battleNetevent.getDefender()).toString());
		GridPane.setConstraints(defenderWoundeds, 2, 3);
		this.paperGridPane.getChildren().add(defenderWoundeds);
	}

	@FXML
	private void handleClose() {
		close();
	}
}
