package org.tiwindetea.animewarfare.gui.game;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by benoit on 02/01/17.
 */
public class CampaignRightToken extends VBox {
	private Label value = new Label("0");

	public CampaignRightToken(String label) {
		setAlignment(Pos.CENTER);

		this.value.setStyle("-fx-background-radius: 100%;" +
				"-fx-background-color: black;" +
				"-fx-padding: 11px 15px 11px 15px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: white;"); // TODO: externalize
		getChildren().add(this.value);

		getChildren().add(new Label(label));
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		getChildren().clear();
	}

	public void setValue(int value) {
		this.value.setText(String.valueOf(value));
	}

	public void increment(int value) {
		this.value.setText(String.valueOf(Integer.valueOf(this.value.getText()) + value));
	}
}
