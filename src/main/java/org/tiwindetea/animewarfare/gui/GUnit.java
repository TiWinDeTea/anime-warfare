package org.tiwindetea.animewarfare.gui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class GUnit extends Parent {

    private final int ID;

    public GUnit() {
        //todo
        this.ID = 0;
        Random r = new Random();
        getChildren().add(new Circle(18, Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256))));
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public int hashCode() {
        return this.ID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GUnit && this.equals((GUnit) o);
    }

    public boolean equals(GUnit u) {
        if (u != null) {
            return this.ID == u.ID;
        }
        return false;
    }
}
