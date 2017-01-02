package org.tiwindetea.animewarfare.gui.game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by benoit on 02/01/17.
 */
public class GameLayoutController implements Initializable {
	private ResourceBundle resourceBundle;

	@FXML
	private BorderPane rootBorderPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resourceBundle = resources;

		this.rootBorderPane.setBottom(new PlayerInfoPane(PlayerInfoPane.Position.BOTTOM));
		this.rootBorderPane.setTop(new PlayerInfoPane(PlayerInfoPane.Position.TOP));
		this.rootBorderPane.setRight(new PlayerInfoPane(PlayerInfoPane.Position.RIGHT));
		this.rootBorderPane.setLeft(new PlayerInfoPane(PlayerInfoPane.Position.LEFT));
		for (Node node : this.rootBorderPane.getChildren()) { // center everyone!
			this.rootBorderPane.setAlignment(node, Pos.CENTER);
		}
	}
}
