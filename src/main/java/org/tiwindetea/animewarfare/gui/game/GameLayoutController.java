package org.tiwindetea.animewarfare.gui.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GMap;
import org.tiwindetea.animewarfare.gui.GUnit;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;

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

	private Timeline scrollLeft;
	private Timeline scrollRight;
	private Timeline scrollUp;
	private Timeline scrollDown;

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

		testing();
	}

	// todo : remove
	private void testing() {
		EventDispatcher.registerListener(ZoneClickedEvent.class, event -> {
			if (event.getMouseEvent().getButton().equals(MouseButton.PRIMARY)) {
				this.map.addUnit(new GUnit(), event.getZoneID());
			} else if (event.getMouseEvent().getButton().equals(MouseButton.SECONDARY)) {
				this.map.removeUnit(new GUnit(), event.getZoneID());
			} else {
				this.map.highlightNeighbour(event.getZoneID(), 1);
				new Thread(() -> {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.map.unHighlightNeigbour(event.getZoneID(), 1);
				}).start();
			}
		});
	}

	private void initScroll() {

		final Duration SCROLL_RATE = Duration.millis(20);
		final double SCROLL_SPEED = 0.01;
		final double SCROLL_SENSITIVITY = 50.0;


		ScrollPane scrollPane = new ScrollPane(this.map);
		this.hBox.getChildren().add(scrollPane);

		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		this.scrollLeft = new Timeline(new KeyFrame(SCROLL_RATE, event -> scrollPane.setHvalue(scrollPane.getHvalue() - SCROLL_SPEED)));
		this.scrollRight = new Timeline(new KeyFrame(SCROLL_RATE, event -> scrollPane.setHvalue(scrollPane.getHvalue() + SCROLL_SPEED)));
		this.scrollUp = new Timeline(new KeyFrame(SCROLL_RATE, event -> scrollPane.setVvalue(scrollPane.getVvalue() - SCROLL_SPEED)));
		this.scrollDown = new Timeline(new KeyFrame(SCROLL_RATE, event -> scrollPane.setVvalue(scrollPane.getVvalue() + SCROLL_SPEED)));
		this.scrollLeft.setCycleCount(Timeline.INDEFINITE);
		this.scrollRight.setCycleCount(Timeline.INDEFINITE);
		this.scrollUp.setCycleCount(Timeline.INDEFINITE);
		this.scrollDown.setCycleCount(Timeline.INDEFINITE);

		scrollPane.setOnMouseMoved(e -> {
			if (e.getX() < SCROLL_SENSITIVITY) {
				this.scrollLeft.play();
			} else if (e.getX() - scrollPane.getWidth() > -SCROLL_SENSITIVITY) {
				this.scrollRight.play();
			} else {
				this.scrollLeft.stop();
				this.scrollRight.stop();
			}

			if (e.getY() < SCROLL_SENSITIVITY) {
				this.scrollUp.play();
			} else if (e.getY() - scrollPane.getHeight() > -SCROLL_SENSITIVITY) {
				this.scrollDown.play();
			} else {
				this.scrollUp.stop();
				this.scrollDown.stop();
			}
		});

		scrollPane.setOnMouseExited(e -> {
			this.scrollLeft.stop();
			this.scrollRight.stop();
			this.scrollUp.stop();
			this.scrollDown.stop();
		});
	}

	@Override
	public void handleQuitApplication() {
		this.scrollLeft.stop();
		this.scrollRight.stop();
		this.scrollUp.stop();
		this.scrollDown.stop();
		EventDispatcher.unregisterListener(QuitApplicationEvent.class, this);
	}
}
