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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.menu.event.SettingsMenuEvent;
import org.tiwindetea.animewarfare.settings.Settings;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * The settings menu controller.
 *
 * @author Benoît CORTIER
 */
public class SettingsMenuController {
	private static final ResourceBundle BUNDLE
			= ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.menu.SettingsMenuController");

	@FXML
	private TextField playerNameTextField;

	@FXML
	private TextField autosaveIntervalTextField;

	@FXML
	private CheckBox enableAutosaveCheckBox;

	@FXML
	private CheckBox enableEffectsCheckBox;

	@FXML
	private ChoiceBox<String> languageChoiceBox;

	@FXML
	private Slider soundLevelSlider; // TODO

	private static final Map<String, String> LANGUAGES = new TreeMap<>(String::compareTo);

	static {
		LANGUAGES.put("en", "English");
		LANGUAGES.put("fr", "Français");
	}

	@FXML
	private void initialize() {
		this.autosaveIntervalTextField.disableProperty().bind(this.enableAutosaveCheckBox.selectedProperty().not());

		this.languageChoiceBox.getItems().addAll(LANGUAGES.values());
		resetFieldsFromSettings();
	}

	@FXML
	void handleSave() {
		String errorMessage = "";

		if (this.playerNameTextField.getText().isEmpty()) {
			errorMessage += BUNDLE.getString("alert.saveerror.playername.empty") + "\n";
		}

		if (this.enableAutosaveCheckBox.isSelected()) {
			try {
				Settings.setAutoSaveInterval(Integer.valueOf(this.autosaveIntervalTextField.getText()));
			} catch (NumberFormatException e) {
				errorMessage += BUNDLE.getString("alert.saveerror.interval.autosave.notanumber");
			}
		}

		if (!errorMessage.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(BUNDLE.getString("alert.saveerror.title"));
			alert.setHeaderText(BUNDLE.getString("alert.saveerror.header"));
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return;
		}

		if (!this.enableAutosaveCheckBox.isSelected()) {
			Settings.setAutoSaveInterval(0);
		}

		Settings.setLanguageTag(LANGUAGES.entrySet()
		                                 .stream()
		                                 .filter(e -> e.getValue()
		                                               .equals(this.languageChoiceBox.getSelectionModel()
		                                                                             .getSelectedItem()))
		                                 .map(e -> e.getKey())
		                                 .findFirst()
		                                 .orElse(LANGUAGES.get("en")));

		Settings.setPlayerName(this.playerNameTextField.getText());
		Settings.setEnableAnimationEffects(this.enableEffectsCheckBox.isSelected());
		Settings.savePreferences();
	}

	@FXML
	void handleQuit() {
		if (isThereUnsavedChanges()) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle(BUNDLE.getString("alert.unsavedchanges.title"));
			alert.setHeaderText(BUNDLE.getString("alert.unsavedchanges.header"));

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				resetFieldsFromSettings();
			} else {
				return;
			}
		}

		EventDispatcher.getInstance().fire(new SettingsMenuEvent(SettingsMenuEvent.Type.QUIT));
	}

	@FXML
	void onIntervalAutosaveTextChanged(KeyEvent event) {
		if (!event.getCharacter().matches("[0-9]")) {
			event.consume();
		}
	}

	boolean isThereUnsavedChanges() {
		return !this.playerNameTextField.getText().equals(Settings.getPlayerName())
				|| (this.enableAutosaveCheckBox.isSelected() && !this.autosaveIntervalTextField.getText().equals(String.valueOf(Settings.getAutoSaveInterval())))
				|| (!this.enableAutosaveCheckBox.isSelected() && Settings.getAutoSaveInterval() != 0)
				|| this.enableEffectsCheckBox.isSelected() != Settings.areAnimationEffectsEnabled();
	}

	void resetFieldsFromSettings() {
		this.playerNameTextField.setText(Settings.getPlayerName());
		this.enableEffectsCheckBox.setSelected(Settings.areAnimationEffectsEnabled());

		if (Settings.getAutoSaveInterval() > 0) {
			this.enableAutosaveCheckBox.setSelected(true);
			this.autosaveIntervalTextField.setText(String.valueOf(Settings.getAutoSaveInterval()));
		} else {
			this.enableAutosaveCheckBox.setSelected(false);
		}

		this.languageChoiceBox.getSelectionModel().select(LANGUAGES.get(Settings.getLanguageTag()));
	}
}
