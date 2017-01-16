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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNeteventListener;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

/**
 * The chat controller.
 *
 * @author Benoît CORTIER
 */
public class ChatController implements MessageReceivedNeteventListener {
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
		EventDispatcher.getInstance().addListener(MessageReceivedNetevent.class, this);

		this.chatMessagesScroll.vvalueProperty().addListener((obs, oldval, newval) -> {
			if (oldval.doubleValue() == 1.0 && this.needScrollBarUpdate) {
				this.chatMessagesScroll.setVvalue(1.0);
				this.needScrollBarUpdate = false;
			}
		});
	}

	public void initShortcuts(Stage primaryStage) {
		primaryStage.getScene().getAccelerators().put(
				new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_ANY), () -> handleSend()
		);
	}

	@FXML
	private void handleSend() {
		if (!this.answerTextArea.getText().isEmpty()) {
			MainApp.getGameClient().send(this.answerTextArea.getText());
			addMessage(this.answerTextArea.getText(), fromattedGCName(MainApp.getGameClient().getClientInfo()),
					GlobalChat.getClientColor(MainApp.getGameClient().getClientInfo()));
			this.answerTextArea.setText("");
		}
	}

	public void addMessage(String text, String senderName, Color color) {
		Text name = new Text(senderName);
		name.setStyle("-fx-font-weight: bold");
		name.setFill(color);

		TextFlow textFlow = new TextFlow(name);

		while (text.contains("**")) {
			if (!text.startsWith("**")) {
				textFlow.getChildren().add(new Text(text.substring(0, text.indexOf("**"))));
			}
			text = text.substring(text.indexOf("**") + 2);
			if (text.contains("**")) {
				Text GText = new Text(text.substring(0, text.indexOf("**")));
				GText.setStyle("-fx-font-weight: bold");
				text = text.substring(text.indexOf("**") + 2);
				textFlow.getChildren().addAll(GText);
			}
		}
		textFlow.getChildren().add(new Text(text));
		this.chatMessages.getChildren().addAll(textFlow);
		if (this.chatContent.size() > MAX_MESSAGES) {
			this.chatMessages.getChildren().remove(this.chatContent.pop());
		}
	}

	public void addSystemMessage(String message, Color color) {
		Label newMessage = new Label(message);
		newMessage.setTextFill(color);

		this.chatContent.addLast(newMessage);
		this.chatMessages.getChildren().add(newMessage);

		if (this.chatContent.size() > MAX_MESSAGES) {
			this.chatMessages.getChildren().remove(this.chatContent.pop());
		}

		this.needScrollBarUpdate = true;
	}

	@Override
	public void handleMessage(MessageReceivedNetevent message) {
		Platform.runLater(() -> addMessage(
				message.getMessage(), fromattedGCName(message.getSenderInfos()),
				GlobalChat.getClientColor(message.getSenderInfos())
		));
	}

	// helper
	private String fromattedGCName(GameClientInfo info) {
		FactionType faction = GlobalChat.getClientFaction(info);
		if (faction != null) {
			return info.getGameClientName() + " (" + faction + "): ";
		}
		return info.getGameClientName() + ": ";
	}
}

