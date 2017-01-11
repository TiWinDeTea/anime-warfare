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

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * @author Benoît CORTIER
 */
public class OverlayMessageDialog extends GameDialog {
	@FXML
	private Label messageLabel;

	public OverlayMessageDialog(VBox overlay, String message) {
		super(overlay);

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("OverlayMessageDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle(getClass().getName()));
		loader.setController(this);

		try {
			addElement(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.messageLabel.setText(message);

		PauseTransition pauseTransition = new PauseTransition(Duration.seconds(10));
		pauseTransition.setOnFinished(e -> close());
		pauseTransition.play();
	}

	@FXML
	private void handleClose() {
		close();
	}
}
