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

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by benoit on 02/01/17.
 */
public class GameLayoutController implements Initializable, QuitApplicationEventListener {
	private ResourceBundle resourceBundle;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private HBox hBox;

	private GMap map;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		EventDispatcher.registerListener(QuitApplicationEvent.class, this);

		this.resourceBundle = resources;
		this.map = new GMap();
		this.map.autosize();
		this.hBox.autosize();
		this.initScroll();
		this.map.displayZonesGrids(true);
		this.map.displayComponentssGrids(true);

		this.rootBorderPane.setBottom(new PlayerInfoPane(PlayerInfoPane.Position.BOTTOM));
		this.rootBorderPane.setTop(new PlayerInfoPane(PlayerInfoPane.Position.TOP));
		this.rootBorderPane.setRight(new PlayerInfoPane(PlayerInfoPane.Position.RIGHT));
		this.rootBorderPane.setLeft(new PlayerInfoPane(PlayerInfoPane.Position.LEFT));
		for (Node node : this.rootBorderPane.getChildren()) { // center everyone!
			this.rootBorderPane.setAlignment(node, Pos.CENTER);
		}
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
	}
}
