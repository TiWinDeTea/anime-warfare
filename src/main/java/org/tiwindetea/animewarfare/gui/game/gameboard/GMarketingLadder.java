package org.tiwindetea.animewarfare.gui.game.gameboard;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.MarketingLadder;
import org.tiwindetea.animewarfare.net.networkevent.MarketingLadderUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MarketingLadderUpdatedNeteventListener;

public class GMarketingLadder extends AnchorPane implements MarketingLadderUpdatedNeteventListener {
	private final Circle circle = new Circle();
	private final Label label = new Label();

	private final IntegerProperty position;
	private final StringProperty nextCost;

	public GMarketingLadder() {
		this.position = new SimpleIntegerProperty(0);
		this.nextCost = new SimpleStringProperty(String.valueOf(MarketingLadder.COSTS[0]));

		EventDispatcher.registerListener(MarketingLadderUpdatedNetevent.class, this);

		this.circle.setRadius(20);
		this.circle.setFill(Color.TRANSPARENT);
		this.circle.setStroke(Color.BLACK);
		NumberBinding binding = Bindings.subtract(heightProperty(),
				Bindings.multiply(Bindings.divide(heightProperty(), 8), this.position));
		this.circle.centerYProperty().bind(binding);
		this.label.translateXProperty()
		          .bind(Bindings.subtract(this.circle.centerXProperty(),
				          Bindings.divide(this.label.widthProperty(), 2.)));
		this.label.translateYProperty()
		          .bind(Bindings.subtract(this.circle.centerYProperty(),
				          Bindings.divide(this.label.heightProperty(), 2.)));
		this.label.textProperty().bind(this.nextCost);

		getChildren().addAll(this.circle, this.label);
	}

	public void clean() {
		EventDispatcher.unregisterListener(MarketingLadderUpdatedNetevent.class, this);
	}


	@Override
	public void handleMarketingLadderUpdate(MarketingLadderUpdatedNetevent event) {
		this.position.set(event.getNewPosition());
		this.nextCost.setValue(String.valueOf(event.getCost()));
	}
}
