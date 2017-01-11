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

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.game.dialog.PlayingOrderDialog;
import org.tiwindetea.animewarfare.logic.GFanCounter;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FirstPlayerSelectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangeNetevent;
import org.tiwindetea.animewarfare.net.networkevent.PhaseChangedNeteventListener;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Benoît CORTIER
 */
public class GameLayoutController implements Initializable, QuitApplicationEventListener,
		PhaseChangedNeteventListener, FirstPlayerSelectedNeteventListener {
	private ResourceBundle resourceBundle;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private HBox hBox;

	private GMap map;

	private VBox overlay;

	public void initStart() {
		MainApp.getMainRoot().getChildren().add(this.overlay);

		// === dispose players info at the right place.
		List<GameClientInfo> players = MainApp.getGameClient().getRoom().getMembers();
		int j = 0;
		for (GameClientInfo player : players) {
			++j;
			if (MainApp.getGameClient().getClientInfo().equals(player)) {
				this.rootBorderPane.setBottom(new PlayerInfoPane(PlayerInfoPane.Position.BOTTOM, player));
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
				}
				++counter;
			}
		}
		// === END: dispose players info at the right place.

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

		GamePhaseMonitor.init();

		this.overlay = new VBox(10);
		this.overlay.setAlignment(Pos.CENTER);
		this.overlay.setMouseTransparent(true);

		this.map = new GMap();
		this.map.autosize();
		this.hBox.autosize();
		this.initScroll();
		this.map.displayZonesGrids(true);
		this.map.displayComponentssGrids(true);
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

	@Override
	public void handleQuitApplication() {
		EventDispatcher.unregisterListener(QuitApplicationEvent.class, this);
		EventDispatcher.unregisterListener(PhaseChangeNetevent.class, this);
		EventDispatcher.unregisterListener(FirstPlayerSelectedNetevent.class, this);
	}

	@Override
	public void handlePhaseChanged(PhaseChangeNetevent event) {
		if (event.getPhase().equals(PhaseChangedEvent.Phase.STAFF_HIRING)) {
			return;
		}

		Platform.runLater(() -> {
			addOverlayMessage(event.getPhase().name() + " phase started"); // TODO: externalize.
		});
	}

	@Override
	public void handlePlayerSelection(FirstPlayerSelectedNetevent firstPlayerSelectedNetevent) {
		Platform.runLater(() -> {
			GameClientInfo info = firstPlayerSelectedNetevent.getGameClientInfo();
			if (MainApp.getGameClient().getClientInfo().equals(info)) {
				new PlayingOrderDialog(this.overlay);
			} else {
				addOverlayMessage(info.getGameClientName() + " is the first player."); // TODO: externalize.
			}
		});
	}

	// helper
	private void addOverlayMessage(String message) {
		Label label = new Label(message);
		label.setStyle("-fx-background-color: black;" +
				"-fx-text-fill: white;"); // TODO: externalize.

		this.overlay.getChildren().add(label);

		PauseTransition pauseTransition = new PauseTransition(Duration.seconds(4));
		pauseTransition.setOnFinished(e -> this.overlay.getChildren().remove(label));
		pauseTransition.play();
	}
}
