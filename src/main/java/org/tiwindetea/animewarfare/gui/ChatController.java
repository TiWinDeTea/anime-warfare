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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.tiwindetea.animewarfare.Settings;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

/**
 * The chat controller.
 *
 * @author Benoît CORTIER
 */
public class ChatController {
	private static final ResourceBundle BUNDLE
			= ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.ChatController");

	@FXML
	private VBox chatMessages;

	@FXML
	private ScrollPane chatMessagesScroll;

	private boolean needScrollBarUpdate = false;

	@FXML
	private TextArea answerTextArea;

	private Deque<Label> chatContent = new ArrayDeque<>();

	private static final int MAX_MESSAGES = Integer.valueOf(BUNDLE.getString("messages.max"));

	public void clear() {
		this.chatMessages.getChildren().clear();
		this.chatContent.clear();
	}

	@FXML
	private void initialize() {
		this.chatMessagesScroll.vvalueProperty().addListener((obs, oldval, newval) -> {
			if (oldval.doubleValue() == 1.0 && this.needScrollBarUpdate) {
				this.chatMessagesScroll.setVvalue(1.0);
				this.needScrollBarUpdate = false;
			}
		});

		/* TODO: add a shortcut to send messages by pressing CTRL+ENTER.
		this.mainApp.getPrimaryStage().getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.SHIFT, KeyCombination.SHORTCUT_DOWN), () -> handleSend());
		button.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));*/
	}

	@FXML
	private void handleSend(ActionEvent event) {
		if (!this.answerTextArea.getText().isEmpty()) {
			// TODO: send message over network.
			addMessage(Settings.getPlayerName() + ": " + this.answerTextArea.getText(), Color.DARKBLUE);
			this.answerTextArea.setText("");
		}
	}

	private void addMessage(String message, Color color) {
		Label newMessage = new Label(message);
		newMessage.setTextFill(color);

		this.chatContent.addLast(newMessage);
		this.chatMessages.getChildren().add(newMessage);

		if (this.chatContent.size() > MAX_MESSAGES) {
			this.chatMessages.getChildren().remove(this.chatContent.pop());
		}

		this.needScrollBarUpdate = true;
	}
}

