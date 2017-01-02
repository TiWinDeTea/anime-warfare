package org.tiwindetea.animewarfare.gui.game;

import javafx.scene.control.Label;

/**
 * Created by benoit on 02/01/17.
 */
public class StaffCounter extends Label {
	public StaffCounter() {
		super("0");
		setStyle("-fx-background-color: black;" +
				"-fx-padding: 20px 15px 20px 15px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: white;"); // TODO: externalize
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		getChildren().clear();
	}

	public void setValue(int value) {
		setText(String.valueOf(value));
	}

	public void increment(int value) {
		setText(String.valueOf(Integer.valueOf(getText()) + value));
	}

	public void decrement(int value) {
		setText(String.valueOf(Integer.valueOf(getText()) - value));
	}
}
