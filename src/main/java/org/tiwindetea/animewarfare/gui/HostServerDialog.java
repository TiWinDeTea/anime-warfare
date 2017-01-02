package org.tiwindetea.animewarfare.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

	public HostServerDialog(Parent parent) {
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
