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
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;

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
		scrollPane.setStyle("-fx-background-color:transparent;"); // TODO externalize
		this.hBox.getChildren().add(scrollPane);

		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		scrollPane.setPannable(true);
		scrollPane.addEventFilter(ScrollEvent.ANY, e -> this.map.scrollEvent(e));
	}

	@Override
	public void handleQuitApplication() {
		EventDispatcher.unregisterListener(QuitApplicationEvent.class, this);
	}
}
