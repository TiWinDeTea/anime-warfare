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

package org.tiwindetea.animewarfare.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by benoit on 30/12/16.
 */
public class HostServerDialog extends Stage implements Initializable {
	@FXML
	private TextField serverNameTextField;

	@FXML
	private TextField numberOfPlayersTextField;

	@FXML
	private TextField tcpPortTextField;

	@FXML
	private CheckBox passwordCheckBox;

	@FXML
	private TextField passwordTextField;

	private boolean canceled = true;

	public HostServerDialog() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(HostServerDialog.class.getResource("HostServerDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.HostServerDialog"));
		loader.setController(this);

		try {
			setScene(new Scene(loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		setTitle(resourceBundle.getString("dialog.title"));

		this.serverNameTextField.setText(MainApp.getGameServer().getRoom().getGameName());
		this.numberOfPlayersTextField.setText(resourceBundle.getString("textfield.players.number.max.default"));
		this.tcpPortTextField.setText(resourceBundle.getString("textfield.port.tcp.default"));
		this.passwordCheckBox.setSelected(false);
		this.passwordTextField.disableProperty().bind(this.passwordCheckBox.selectedProperty().not());
	}

	@FXML
	private void handleCancel() {
		close();
	}

	@FXML
	private void handleStart() {
		this.canceled = false;
		close();
	}

	@FXML
	private void handleNumeralsOnlyTextChanged(KeyEvent event) {
		if (!event.getCharacter().matches("[0-9]")) {
			event.consume();
		}
	}

	public String getServerName() {
		return this.serverNameTextField.getText();
	}

	public String getPassword() {
		if (this.passwordCheckBox.isSelected()) {
			return this.passwordTextField.getText();
		}
		return null;
	}

	public int getMaxPlayers() {
		return Integer.valueOf(this.numberOfPlayersTextField.getText());
	}

	public int getTcpPort() {
		return Integer.valueOf(this.tcpPortTextField.getText());
	}

	public boolean isCanceled() {
		return this.canceled;
	}
}
