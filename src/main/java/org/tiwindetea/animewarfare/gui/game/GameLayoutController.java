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

package org.tiwindetea.animewarfare.gui.game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.game.dialog.BattleResultDialog;
import org.tiwindetea.animewarfare.gui.game.dialog.OverlayMessageDialog;
import org.tiwindetea.animewarfare.gui.game.dialog.PlayingOrderDialog;
import org.tiwindetea.animewarfare.gui.game.dialog.SelectNextFirstPlayerDialog;
import org.tiwindetea.animewarfare.gui.game.gameboard.GContextActionMenu;
import org.tiwindetea.animewarfare.gui.game.gameboard.GFanCounter;
import org.tiwindetea.animewarfare.gui.game.gameboard.GMap;
import org.tiwindetea.animewarfare.gui.game.gameboard.GMarketingLadder;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.AskFirstPlayerSelectionNetvent;
import org.tiwindetea.animewarfare.net.networkevent.AskFirstPlayerSelectionNetventListener;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNetevent;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.SelectUnitToCaptureRequestNetevent;
import org.tiwindetea.animewarfare.net.networkevent.SelectUnitToCaptureRequestNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetBattlePhaseReadyRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetConventionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetFinishTurnRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSkipAllRequest;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Benoît CORTIER
 */
public class GameLayoutController implements Initializable, QuitApplicationEventListener,
		PhaseChangedNeteventListener, FirstPlayerSelectedNeteventListener, NextPlayerNeteventListener,
		SelectUnitToCaptureRequestNeteventListener, AskFirstPlayerSelectionNetventListener,
		BattleNeteventListener {
	private static GMap map;

	private static final List<PlayerInfoPane> playerInfoPaneList = new ArrayList<>();

	private static PlayerInfoPane localPlayerInfoPane = null;

	private ResourceBundle resourceBundle;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private HBox hBox;

	@FXML
	private HBox currentPlayerPanel;

	@FXML
	private PaperButton skipAllButton;

	@FXML
	private PaperButton finishTurnButton;

	@FXML
	private VBox mainVBox;

	@FXML
	private VBox dynamicCommandVBox;

	private VBox overlay;

	private GFanCounter gfc = new GFanCounter();

	private GMarketingLadder gMarketingLadder = new GMarketingLadder();

	private GContextActionMenu gContextActionMenu;

	private PaperButton battleReadyButton;

	private PaperButton conventionButton = new PaperButton("Make a convention.");

	public void initStart() {
		MainApp.getMainRoot().getChildren().add(this.overlay);
		GameChat chat = new GameChat();
		AnchorPane anchorPane = new AnchorPane();
		anchorPane.setPickOnBounds(false);
		((StackPane) this.overlay.getScene().getRoot()).getChildren().add(anchorPane);
		anchorPane.getChildren().add(chat);
		this.overlay.setPickOnBounds(false);

		// === dispose players info at the right place.
		List<GameClientInfo> players = MainApp.getGameClient().getRoom().getMembers();
		int j = 0;
		for (GameClientInfo player : players) {
			++j;
			if (MainApp.getGameClient().getClientInfo().equals(player)) {
				GameLayoutController.localPlayerInfoPane = new PlayerInfoPane(PlayerInfoPane.Position.BOTTOM, player);
				this.currentPlayerPanel.getChildren().add(1, this.localPlayerInfoPane);
				break;
			}
		}

		int counter = 0;
		int[][] beginstart = new int[][]{
				{j, players.size()},
				{0, j - 1}
		};
		for (int k = 0; k < 2; k++) {
			for (int i = beginstart[k][0]; i < beginstart[k][1]; i++) {
				if (players.size() == 2) {
					this.rootBorderPane.setTop(new PlayerInfoPane(PlayerInfoPane.Position.TOP, players.get(i)));
					break;
				}
				PlayerInfoPane playerInfoPane = null;
				switch (counter) {
					case 0:
						playerInfoPane = new PlayerInfoPane(PlayerInfoPane.Position.LEFT, players.get(i));
						this.rootBorderPane.setLeft(playerInfoPane);
						break;
					case 1:
						playerInfoPane = new PlayerInfoPane(PlayerInfoPane.Position.TOP, players.get(i));
						this.rootBorderPane.setTop(playerInfoPane);
						break;
					case 2:
						playerInfoPane = new PlayerInfoPane(PlayerInfoPane.Position.RIGHT, players.get(i));
						this.rootBorderPane.setRight(playerInfoPane);
						break;
					default:
						throw new IllegalStateException("It seems that there is more than 4 players");
				}
				GameLayoutController.playerInfoPaneList.add(playerInfoPane);
				++counter;
			}
		}

		for (Node node : this.rootBorderPane.getChildren()) { // center everyone!
			this.rootBorderPane.setAlignment(node, Pos.CENTER);
		}
		/// / === END: dispose players info at the right place.

		this.gContextActionMenu = new GContextActionMenu(
				GlobalChat.getClientFaction(MainApp.getGameClient().getClientInfo()),
				this.dynamicCommandVBox
		);

		this.gfc.init();
		this.hBox.getChildren().add(this.gfc);
	}

	public void clearEnd() {
		this.overlay.getChildren().clear();
		this.gfc.clear();
		MainApp.getMainRoot().getChildren().remove(this.overlay);

		this.rootBorderPane.setBottom(null);
		this.rootBorderPane.setTop(null);
		this.rootBorderPane.setRight(null);
		this.rootBorderPane.setLeft(null);

		GameLayoutController.playerInfoPaneList.clear();
		GameLayoutController.localPlayerInfoPane = null;

		this.gContextActionMenu.destroy();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resourceBundle = resources;

		EventDispatcher.registerListener(QuitApplicationEvent.class, this);
		EventDispatcher.registerListener(PhaseChangeNetevent.class, this);
		EventDispatcher.registerListener(FirstPlayerSelectedNetevent.class, this);
		EventDispatcher.registerListener(NextPlayerNetevent.class, this);
		EventDispatcher.registerListener(SelectUnitToCaptureRequestNetevent.class, this);
		EventDispatcher.registerListener(AskFirstPlayerSelectionNetvent.class, this);
		EventDispatcher.registerListener(BattleNetevent.class, this);

		ProductionMonitor.init();
		GamePhaseMonitor.init();
		PlayerTurnMonitor.init();
		UnitCountMonitor.init();
		CostModifierMonitor.init();

		this.overlay = new VBox(10);
		this.overlay.setAlignment(Pos.CENTER);
		this.overlay.setMouseTransparent(true);

		map = new GMap();
		map.autosize();
		this.hBox.getChildren().add(this.gMarketingLadder);
		this.hBox.autosize();
		this.initScroll();
		map.displayZonesGrids(true);

		this.conventionButton.setPrefWidth(150);
		this.conventionButton.setPrefHeight(30);
		this.conventionButton.setOnAction(a -> MainApp.getGameClient().send(new NetConventionRequest()));
	}

	private void initScroll() {
		ScrollPane scrollPane = new ScrollPane();

		this.gMarketingLadder.maxHeightProperty().bind(scrollPane.heightProperty());
		this.gMarketingLadder.prefHeightProperty().bind(scrollPane.heightProperty());
		this.gMarketingLadder.minHeightProperty().bind(scrollPane.heightProperty());

		scrollPane.setContent(new Group(this.map));
		this.hBox.getChildren().add(scrollPane);
		scrollPane.getStyleClass().add("MagicPane");

		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		scrollPane.setPannable(true);
		scrollPane.addEventFilter(ScrollEvent.ANY, this.map::scrollEvent);
	}

	@FXML
	private void handleFinishTurn() {
		MainApp.getGameClient().send(new NetFinishTurnRequest());
	}

	@FXML
	private void handleSkipAll() {
		MainApp.getGameClient().send(new NetSkipAllRequest());
	}

	@Override
	public void handleQuitApplication() {
		EventDispatcher.unregisterListener(QuitApplicationEvent.class, this);
		EventDispatcher.unregisterListener(PhaseChangeNetevent.class, this);
		EventDispatcher.unregisterListener(FirstPlayerSelectedNetevent.class, this);
		EventDispatcher.unregisterListener(NextPlayerNetevent.class, this);
		EventDispatcher.unregisterListener(SelectUnitToCaptureRequestNetevent.class, this);
		EventDispatcher.unregisterListener(AskFirstPlayerSelectionNetvent.class, this);
		EventDispatcher.unregisterListener(BattleNetevent.class, this);
	}

	@Override
	public void handlePhaseChanged(PhaseChangeNetevent event) {
		if (event.getPhase().equals(PhaseChangedEvent.Phase.STAFF_HIRING)) {
			return;
		}

		if (event.getPhase().equals(PhaseChangedEvent.Phase.MARKETING)) {
			Platform.runLater(() -> this.dynamicCommandVBox.getChildren().add(this.conventionButton));
		} else {
			Platform.runLater(() -> this.dynamicCommandVBox.getChildren().remove(this.conventionButton));
		}

		if (PlayerTurnMonitor.getCurrentPlayer() != null && PlayerTurnMonitor.getCurrentPlayer()
		                                                                     .equals(MainApp.getGameClient()
		                                                                                    .getClientInfo())) {
			this.finishTurnButton.setDisable(false);
			if (event.getPhase().equals(PhaseChangedEvent.Phase.MARKETING)) {
				Platform.runLater(() -> this.skipAllButton.setDisable(true));
			} else {
				Platform.runLater(() -> this.skipAllButton.setDisable(false));
			}
		} else {
			this.finishTurnButton.setDisable(true);
			this.skipAllButton.setDisable(true);
		}

		Platform.runLater(() -> {
			new OverlayMessageDialog(this.overlay, event.getPhase() + " phase started"); // TODO: externalize.
		});
	}

	@Override
	public void handlePlayerSelection(FirstPlayerSelectedNetevent firstPlayerSelectedNetevent) {
		Platform.runLater(() -> {
			GameClientInfo info = firstPlayerSelectedNetevent.getGameClientInfo();
			if (MainApp.getGameClient().getClientInfo().equals(info)) {
				new OverlayMessageDialog(this.overlay, "You are the first player."); // TODO: externalize.
				if (MainApp.getGameClient().getRoom().getMembers().size() == 2) {
					MainApp.getGameClient().send(new NetPlayingOrderChosen(true));
				} else {
					new PlayingOrderDialog(this.overlay);
				}
			} else {
				new OverlayMessageDialog(this.overlay, info.getGameClientName() + " is the first player."); // TODO: externalize.
			}
		});
	}

	@Override
	public void handleNextPlayer(NextPlayerNetevent event) {
		Platform.runLater(() -> {
			GameClientInfo info = event.getGameClientInfo();
			if (MainApp.getGameClient().getClientInfo().equals(info)) {
				new OverlayMessageDialog(this.overlay, "This is your turn."); // TODO: externalize.
				this.finishTurnButton.setDisable(false);
				this.skipAllButton.setDisable(false);
			} else {
				new OverlayMessageDialog(this.overlay, info.getGameClientName() + "'s turn."); // TODO: externalize.
				this.finishTurnButton.setDisable(true);
				this.skipAllButton.setDisable(true);
			}
		});
	}

	@Override
	public void handleFirstPlayerSelectionAsked(AskFirstPlayerSelectionNetvent event) {
		Platform.runLater(() -> {
			GameClientInfo info = event.getPlayerThatSelects();
			if (MainApp.getGameClient().getClientInfo().equals(info)) {
				new SelectNextFirstPlayerDialog(this.overlay, event.getSelectablePlayers());
			} else {
				String[] playerNames = new String[event.getSelectablePlayers().size()];
				final int[] i = {0};
				event.getSelectablePlayers().forEach(p -> {
					playerNames[i[0]] = p.getGameClientName();
					++i[0];
				});
				String selectablesPlayers = String.join(", ", playerNames);
				new OverlayMessageDialog(this.overlay, info.getGameClientName()
						+ " is deciding who plays first between: "
						+ selectablesPlayers); // TODO: externalize.
			}
		});
	}

	public static GMap getMap() {
		return map;
	}

	public static PlayerInfoPane getLocalPlayerInfoPane() {
		return GameLayoutController.localPlayerInfoPane;
	}

	@Override
	public void handleMascotSelectionRequest(SelectUnitToCaptureRequestNetevent event) {
		Platform.runLater(() -> {
			if (event.getPlayer().equals(MainApp.getGameClient().getClientInfo())) {
				new OverlayMessageDialog(this.overlay,
						"Select a " + event.getUnitLevel() + " which will be captured."); // TODO: externalize.
				this.finishTurnButton.setDisable(true);
				this.skipAllButton.setDisable(true);
				GameLayoutController.map.highlight(event.getZoneId(), Color.rgb(0, 0, 0, 0.4), Color.rgb(0, 0, 0, 0.6));
			} else {
				new OverlayMessageDialog(this.overlay, "Player " + event.getPlayer().getGameClientName() + "'s mascot is being captured\n" +
						"(in zone " + event.getZoneId() + ")"); // TODO: externalize
			}
		});
	}

	@Override
	public void handlePreBattle(BattleNetevent event) {
		Platform.runLater(() -> {
			this.map.highlight(event.getZone(), Color.rgb(255, 153, 51, 0.4), Color.rgb(255, 153, 51, 0.6));

			this.battleReadyButton = new PaperButton("Battle ready"); // TODO: externalize
			this.battleReadyButton.setPrefWidth(150);
			this.battleReadyButton.setPrefHeight(30);
			this.battleReadyButton.setOnAction(a -> MainApp.getGameClient().send(new NetBattlePhaseReadyRequest()));

			this.dynamicCommandVBox.getChildren().add(this.battleReadyButton);

			if (event.getDefender().equals(MainApp.getGameClient().getClientInfo())) {
				new OverlayMessageDialog(this.overlay, event.getAttacker().getGameClientName() + " started a battle with you.");
			} else if (!event.getAttacker().equals(MainApp.getGameClient().getClientInfo())) {
				new OverlayMessageDialog(this.overlay, event.getAttacker().getGameClientName()
						+ " started a battle with " + event.getDefender().getGameClientName());
			}
		});
	}

	@Override
	public void handleDuringBattle(BattleNetevent event) {
		Platform.runLater(() -> {
			this.battleReadyButton.setDisable(true);

			new BattleResultDialog(this.overlay, event);
		});
	}

	@Override
	public void handlePostBattle(BattleNetevent event) {
		Platform.runLater(() -> {
			this.battleReadyButton.setDisable(false);

			// todo
		});
	}

	@Override
	public void handleBattleFinished(BattleNetevent event) {
		Platform.runLater(() -> {
			this.map.unHighlightFxThread(event.getZone());

			this.dynamicCommandVBox.getChildren().remove(this.battleReadyButton);
			this.battleReadyButton = null;
		});
	}
}
