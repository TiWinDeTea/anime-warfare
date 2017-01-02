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
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by benoit on 01/01/17.
 */
public class ConnectIpDialog extends Stage implements Initializable {
	@FXML
	private TextField serverIpTextField;

	@FXML
	private TextField tcpPortTextField;

	@FXML
	private CheckBox passwordCheckBox;

	@FXML
	private TextField passwordTextField;

	private boolean canceled = true;

	public ConnectIpDialog(Parent parent) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ConnectIpDialog.class.getResource("ConnectIpDialog.fxml"));
		loader.setResources(ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.ConnectIpDialog"));
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

	public String getServerIp() {
		return this.serverIpTextField.getText();
	}

	public String getPassword() {
		if (this.passwordCheckBox.isSelected()) {
			return this.passwordTextField.getText();
		}
		return null;
	}

	public int getTcpPort() {
		return Integer.valueOf(this.tcpPortTextField.getText());
	}

	public boolean isCanceled() {
		return this.canceled;
	}
}
