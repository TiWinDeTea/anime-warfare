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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.menu.event.GameRoomEvent;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.Room;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnlockedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnlockedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnselectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FactionUnselectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.GameStartedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameStartedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PlayerConnectionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerConnectionNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PlayerDisconnectionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerDisconnectionNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PlayerLockedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerLockedFactionNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PlayerSelectedFactionNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PlayerSelectedFactionNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnlockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnselectFactionRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * The game room controller.
 *
 * @author Benoît CORTIER
 */
public class GameRoomController
		implements ConnectedNeteventListener, PlayerConnectionNeteventListener,
		PlayerDisconnectionNeteventListener, PlayerSelectedFactionNeteventListener,
		PlayerLockedFactionNeteventListener, FactionUnselectedNeteventListener,
		FactionUnlockedNeteventListener, GameStartedNeteventListener {
	private static final ColorAdjust EFFECT_MONOCHROME = new ColorAdjust();
	private static final ColorAdjust GREENIFY;

	static {
		EFFECT_MONOCHROME.setSaturation(-1.);

		// greenify effect
		ColorAdjust greenify = new ColorAdjust();
		Color targetColor = Color.LIGHTGREEN;
		double hue = map((targetColor.getHue() + 180) % 360, 0, 360, -1, 1);
		greenify.setHue(hue);

		double saturation = targetColor.getSaturation();
		greenify.setSaturation(saturation);

		double brightness = map(targetColor.getBrightness(), 0, 1, -1, 0);
		greenify.setBrightness(brightness);
		GREENIFY = greenify;
	}

	@FXML
	private VBox usersList;

	@FXML
	private BorderPane borderPane;

	@FXML
	private ImageView fClassNoBakaImageView;

	@FXML
	private ImageView noNameImageView;

	@FXML
	private ImageView haiyoreImageView;

	@FXML
	private ImageView theBlackKnightsImageView;

	@FXML
	private Button lockFactionButton;

	private Map<Integer, Label> userNamesLabels = new HashMap<>();

	private FactionType selectedFaction = null;

	private boolean factionLocked = false;

	public BorderPane getBorderPane() {
		return this.borderPane;
	}

	@FXML
	private void initialize() {
		EventDispatcher.registerListener(ConnectedNetevent.class, this);
		EventDispatcher.registerListener(PlayerConnectionNetevent.class, this);
		EventDispatcher.registerListener(PlayerDisconnectionNetevent.class, this);
		EventDispatcher.registerListener(PlayerSelectedFactionNetevent.class, this);
		EventDispatcher.registerListener(PlayerLockedFactionNetevent.class, this);
		EventDispatcher.registerListener(FactionUnselectedNetevent.class, this);
		EventDispatcher.registerListener(FactionUnlockedNetevent.class, this);
		EventDispatcher.registerListener(GameStartedNetevent.class, this);
	}

	@FXML
	private void handleQuit() {
		// stop network things.
		MainApp.getGameClient().disconnect();
		if (MainApp.getGameServer().isRunning()) {
			MainApp.getGameServer().stop();
			MainApp.initANewGameServer();
		}

		// re-initialize states.
		this.selectedFaction = null;
		this.factionLocked = false;

		this.userNamesLabels.clear();
		this.usersList.getChildren().clear();
		this.fClassNoBakaImageView.setEffect(null);
		this.haiyoreImageView.setEffect(null);
		this.noNameImageView.setEffect(null);
		this.theBlackKnightsImageView.setEffect(null);
		this.handleUnlockFaction(new ActionEvent());

		EventDispatcher.getInstance().fire(new GameRoomEvent(GameRoomEvent.Type.QUIT));
	}

	private void handleFactionClicked(FactionType faction){
		if (faction.equals(this.selectedFaction)) {
			MainApp.getGameClient().send(new NetUnselectFactionRequest());
		} else {
			MainApp.getGameClient().send(new NetSelectFactionRequest(faction));
		}
	}

	@FXML
	private void handleTheBlackKnightsClicked() {
		this.handleFactionClicked(FactionType.THE_BLACK_KNIGHTS);
	}

	@FXML
	private void handleFClassNoBakaClicked() {
		this.handleFactionClicked(FactionType.F_CLASS_NO_BAKA);
	}

	@FXML
	private void handleHaiyoreClicked() {
		this.handleFactionClicked(FactionType.HAIYORE);
	}

	@FXML
	private void handleNoNameClicked() {
		this.handleFactionClicked(FactionType.NO_NAME);
	}

	@FXML
	private void handleLockFaction() {
		if (this.selectedFaction != null) {
			MainApp.getGameClient().send(new NetLockFactionRequest(this.selectedFaction));
			this.lockFactionButton.setText("Unlock faction"); // TODO externalize
			this.lockFactionButton.setOnAction(this::handleUnlockFaction);
		}
	}

	private void handleUnlockFaction(ActionEvent actionEvent) {
		MainApp.getGameClient().send(new NetUnlockFactionRequest());
		this.lockFactionButton.setText("Lock faction"); // TODO externalize
		this.lockFactionButton.setOnAction(this::handleLockFaction);
	}

	private void handleLockFaction(ActionEvent actionEvent) {
		handleLockFaction();
	}

	@Override
	public void handlePlayerConnection(PlayerConnectionNetevent event) {
		Platform.runLater(() -> {
			addPlayer(event.getClient());
			GlobalChat.getChatController().addMessage(event.getClient().getGameClientName() + " connected.", Color.GRAY); // TODO: externalize.
		});
	}

	@Override
	public void handleSelfConnection(ConnectedNetevent event) {
		Platform.runLater(() -> {
			event.getRoom().getMembers().forEach(this::addPlayer);

			Room room = MainApp.getGameClient().getRoom();
			room.getSelections().forEach((gc, faction) -> this.handleFactionChoice(new PlayerSelectedFactionNetevent(gc, faction)));
			room.getLocks().forEach((gc, faction) -> this.handleFactionLock(new PlayerLockedFactionNetevent(gc, faction)));
		});
	}

	@Override
	public void handlePlayerDisconnection(PlayerDisconnectionNetevent event) {
		Platform.runLater(() -> {
			if (event.getPlayer().equals(MainApp.getGameClient().getClientInfo())) {
				handleQuit();
			} else {
				this.usersList.getChildren().remove(this.userNamesLabels.get(event.getPlayer().getId()));
				this.userNamesLabels.remove(event.getPlayer().getId());
				GlobalChat.getChatController().addMessage(event.getPlayer().getGameClientName() + " disconnected.", Color.GRAY); // TODO: externalize.
			}
		});
	}

	@Override
	public void handleFactionChoice(PlayerSelectedFactionNetevent playerSelectedFactionEvent) {
		getImageViewByFactionType(playerSelectedFactionEvent.getFactionType()).setEffect(GREENIFY);
		if (playerSelectedFactionEvent.getPlayerInfo().equals(MainApp.getGameClient().getClientInfo())) {
			this.selectedFaction = playerSelectedFactionEvent.getFactionType();
		}
	}

	@Override
	public void handleFactionUnselected(FactionUnselectedNetevent factionUnselectedNetevent) {
		if (!MainApp.getGameClient().getRoom().getSelections().containsValue(factionUnselectedNetevent.getFaction())) {
			getImageViewByFactionType(factionUnselectedNetevent.getFaction()).setEffect(null);
		}

		if (MainApp.getGameClient().getClientInfo().equals(factionUnselectedNetevent.getClient())) {
			this.selectedFaction = null;
		}
	}

	@Override
	public void handleFactionLock(PlayerLockedFactionNetevent playerLockedFactionNetevent) {
		Platform.runLater(() -> {
			GameClientInfo info = playerLockedFactionNetevent.getPlayerInfo();
			getImageViewByFactionType(playerLockedFactionNetevent.getFaction()).setEffect(EFFECT_MONOCHROME);
			GlobalChat.registerClientColor(info, getColorByFaction(playerLockedFactionNetevent.getFaction()));
			this.userNamesLabels.get(info.getId()).setTextFill(GlobalChat.getClientColor(info));
		});
	}

	@Override
	public void handleFactionUnlocked(FactionUnlockedNetevent factionUnlockedNetevent) {
		Platform.runLater(() -> {
			GameClientInfo info = factionUnlockedNetevent.getClient();
			getImageViewByFactionType(factionUnlockedNetevent.getFaction())
					.setEffect(
							MainApp.getGameClient().getRoom().getSelections().containsValue(factionUnlockedNetevent.getFaction())
									? GREENIFY
									: null
					);
			GlobalChat.unregisterClientColor(info);
			this.userNamesLabels.get(info.getId()).setTextFill(GlobalChat.getClientColor(info));
		});
	}

	@Override
	public void handleGameStart() {
		Platform.runLater(() -> EventDispatcher.getInstance().fire(new GameRoomEvent(GameRoomEvent.Type.GAME_START)));
	}

	// helper method
	private ImageView getImageViewByFactionType(FactionType factionType) {
		switch (factionType) {
			case NO_NAME:
				return this.noNameImageView;
			case THE_BLACK_KNIGHTS:
				return this.theBlackKnightsImageView;
			case HAIYORE:
				return this.haiyoreImageView;
			case F_CLASS_NO_BAKA:
				return this.fClassNoBakaImageView;
			default:
				throw new IllegalStateException("There is a missing case.");
		}
	}

	// helper
	private Color getColorByFaction(FactionType factionType) {
		switch (factionType) {
			case NO_NAME:
				return Color.DARKGREEN;
			case THE_BLACK_KNIGHTS:
				return Color.DARKBLUE;
			case HAIYORE:
				return Color.DARKRED;
			case F_CLASS_NO_BAKA:
				return Color.DARKORANGE;
			default:
				throw new IllegalStateException("There is a missing case.");
		}
	}

	// helper
	private void addPlayer(GameClientInfo gameClientInfo) {
		Label playerName = new Label(gameClientInfo.getGameClientName());
		playerName.setTextFill(GlobalChat.getClientColor(gameClientInfo));
		this.userNamesLabels.put(gameClientInfo.getId(), playerName);
		this.usersList.getChildren().add(playerName);
	}

	// static helper
	private static double map(double value, double start, double stop, double targetStart, double targetStop) {
		return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
	}
}

