package org.tiwindetea.animewarfare.logic;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.FanNumberUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.FanNumberUpdatedNeteventListener;

import java.util.HashMap;
import java.util.Map;

public class GFanCounter extends AnchorPane implements FanNumberUpdatedNeteventListener {
	private final Map<Integer, IntegerProperty> playerPosition = new HashMap<>();

	public GFanCounter() {
	}

	public void init() {
		double angle = 0.0;
		for (GameClientInfo gameClientInfo : MainApp.getGameClient().getRoom().getMembers()) {
			IntegerProperty position = new SimpleIntegerProperty(0);
			this.playerPosition.put(new Integer(gameClientInfo.getId()), position);
			NumberBinding binding = Bindings.subtract(heightProperty(),
					Bindings.multiply(Bindings.divide(heightProperty(), 60.0), position));

			Rectangle rectangle = new Rectangle(20, 5);
			rectangle.setStyle("-fx-rotate: " + angle + ";");
			angle += 20;
			rectangle.translateYProperty().bind(binding);
			rectangle.setFill(GlobalChat.getClientColor(gameClientInfo));
			getChildren().add(rectangle);
		}
	}

	private void updatePosition(int playerID, int position) {
		this.playerPosition.get(new Integer(playerID)).setValue(new Integer(position));
	}

	@Override
	public void handleFanNumberUpdate(FanNumberUpdatedNetevent event) {
		updatePosition(event.getPlayer().getId(), event.getNewValue());
	}
}
