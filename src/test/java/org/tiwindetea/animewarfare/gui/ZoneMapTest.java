package org.tiwindetea.animewarfare.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.Test;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;

public class ZoneMapTest extends Application {

    @Test
    public void test() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ZonedMap zones = new ZonedMap();
        primaryStage.setScene(new Scene(zones));
        primaryStage.show();

        EventDispatcher.registerListener(ZoneClickedEvent.class, event -> {
            if (event.getMouseEvent().getButton().equals(MouseButton.PRIMARY)) {
                zones.addUnit(new GUnit(), event.getZoneID());
            } else {
                zones.removeUnit(new GUnit(), event.getZoneID());
            }
        });

    }
}
