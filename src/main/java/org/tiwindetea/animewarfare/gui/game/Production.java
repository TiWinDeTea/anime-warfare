package org.tiwindetea.animewarfare.gui.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by benoit on 02/01/17.
 */
public class Production extends Rectangle {
	public Production() {
		super(60., 60.);
		lock();
	}

	public void unlock() {
		setFill(Color.RED);
	}

	public void lock() {
		setFill(Color.DARKRED);
	}
}
