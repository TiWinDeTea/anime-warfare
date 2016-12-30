package org.tiwindetea.animewarfare.gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

public class PaperGridPane extends GridPane {
	private final ImageView mask;
	private final ImageView background = new ImageView();
	private boolean skipNextEvent;

	public PaperGridPane() {
		this.mask = new ImageView(new Image(getClass().getResourceAsStream("pictures/ui/gridPane_mask.png")));
		backgroundProperty().addListener((observable, oldValue, newValue) -> {
			if (this.skipNextEvent) {
				this.skipNextEvent = false;
				return;
			}

			if (!newValue.getImages().isEmpty()) {
				this.mask.setScaleX(getPrefWidth() / this.mask.getImage().getWidth());
				this.mask.setScaleY(getPrefHeight() / this.mask.getImage().getHeight());
				BackgroundImage old = newValue.getImages().get(0);
				this.background.setImage(old.getImage());
				this.background.setClip(this.mask);
				new Scene(new Group(this.background));

				SnapshotParameters sp = new SnapshotParameters();
				sp.setFill(Paint.valueOf("transparent"));
				WritableImage im = this.background.snapshot(sp, null);
				BackgroundImage bi = new BackgroundImage(im,
						old.getRepeatX(),
						old.getRepeatY(),
						old.getPosition(),
						old.getSize());

				this.skipNextEvent = true;
				backgroundProperty().setValue(new Background(bi));
			}
		});
	}
}
