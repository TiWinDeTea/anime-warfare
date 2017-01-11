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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.game.dialog.OverlayMessageDialog;
import org.tiwindetea.animewarfare.gui.game.dialog.PlayingOrderDialog;
import org.tiwindetea.animewarfare.logic.GFanCounter;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNetevent;
import org.tiwindetea.animewarfare.net.networkevent.NextPlayerNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangedNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetFinishTurnRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSkipAllRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Benoît CORTIER
 */
public class GameLayoutController implements Initializable, QuitApplicationEventListener,
		PhaseChangedNeteventListener, FirstPlayerSelectedNeteventListener, NextPlayerNeteventListener {
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

	private static GMap map;

	private VBox overlay;

	public void initStart(GContextActionMenu gContextActionMenu) {
		MainApp.getMainRoot().getChildren().add(this.overlay);

		// === dispose players info at the right place.
		List<GameClientInfo> players = MainApp.getGameClient().getRoom().getMembers();
		int j = 0;
		for (GameClientInfo player : players) {
			++j;
			if (MainApp.getGameClient().getClientInfo().equals(player)) {
				this.currentPlayerPanel.getChildren().add(1, new PlayerInfoPane(PlayerInfoPane.Position.BOTTOM, player));
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
				switch (counter) {
					case 0:
						this.rootBorderPane.setLeft(new PlayerInfoPane(PlayerInfoPane.Position.LEFT, players.get(i)));
						break;
					case 1:
						this.rootBorderPane.setTop(new PlayerInfoPane(PlayerInfoPane.Position.TOP, players.get(i)));
						break;
					case 2:
						this.rootBorderPane.setRight(new PlayerInfoPane(PlayerInfoPane.Position.RIGHT, players.get(i)));
						break;
					default:
						throw new IllegalStateException("It seems that there is more than 4 players");
				}
				++counter;
			}
		}
		/// / === END: dispose players info at the right place.

		gContextActionMenu.setOwnerNode(this.mainVBox);
		for (Node node : this.rootBorderPane.getChildren()) { // center everyone!
			this.rootBorderPane.setAlignment(node, Pos.CENTER);
		}

		GFanCounter gfc = new GFanCounter();
		gfc.init();
		this.hBox.getChildren().add(gfc);
	}

	public void clearEnd() {
		this.overlay.getChildren().clear();
		MainApp.getMainRoot().getChildren().remove(this.overlay);

		this.rootBorderPane.setBottom(null);
		this.rootBorderPane.setTop(null);
		this.rootBorderPane.setRight(null);
		this.rootBorderPane.setLeft(null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resourceBundle = resources;

		EventDispatcher.registerListener(QuitApplicationEvent.class, this);
		EventDispatcher.registerListener(PhaseChangeNetevent.class, this);
		EventDispatcher.registerListener(FirstPlayerSelectedNetevent.class, this);
		EventDispatcher.registerListener(NextPlayerNetevent.class, this);

		GamePhaseMonitor.init();
		PlayerTurnMonitor.init();

		this.overlay = new VBox(10);
		this.overlay.setAlignment(Pos.CENTER);
		this.overlay.setMouseTransparent(true);

		map = new GMap();
		map.autosize();
		this.hBox.autosize();
		this.initScroll();
		map.displayZonesGrids(true);
	}

	private void initScroll() {
		ScrollPane scrollPane = new ScrollPane();

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
	}

	@Override
	public void handlePhaseChanged(PhaseChangeNetevent event) {
		if (event.getPhase().equals(PhaseChangedEvent.Phase.STAFF_HIRING)) {
			return;
		}

		if (event.getPhase().equals(PhaseChangedEvent.Phase.MARKETING)) {
			this.finishTurnButton.setDisable(false);
		} else if (PlayerTurnMonitor.getCurrentPlayer() != null && PlayerTurnMonitor.getCurrentPlayer().equals(MainApp.getGameClient().getClientInfo())) {
			this.finishTurnButton.setDisable(false);
			this.skipAllButton.setDisable(false);
		} else {
			this.finishTurnButton.setDisable(true);
			this.skipAllButton.setDisable(true);
		}

		Platform.runLater(() -> {
			new OverlayMessageDialog(this.overlay, event.getPhase().name() + " phase started"); // TODO: externalize.
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
	public static GMap getMap() {
		return map;
	}
}
