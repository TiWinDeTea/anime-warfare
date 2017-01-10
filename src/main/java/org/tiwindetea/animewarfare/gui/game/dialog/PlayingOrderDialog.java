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
import javafx.scene.layout.VBox;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * @author Benoît CORTIER
 */
public class PlayingOrderDialog extends GameDialog {
	public PlayingOrderDialog(VBox overlay) {
		super(overlay);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("PlayingOrderDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle(getClass().getName()));
		loader.setController(this);

		try {
			addElement(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleLeftClicked() {
		MainApp.getGameClient().send(new NetPlayingOrderChosen(true));
		close();
	}

	@FXML
	private void handleRightClicked() {
		MainApp.getGameClient().send(new NetPlayingOrderChosen(false));
		close();
	}
}
