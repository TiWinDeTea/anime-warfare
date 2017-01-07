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
