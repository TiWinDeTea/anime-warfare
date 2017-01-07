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
        Scene scene = new Scene(zones);
        primaryStage.setScene(scene);
        primaryStage.show();

        EventDispatcher.registerListener(ZoneClickedEvent.class, event -> {
            if (event.getMouseEvent().getButton().equals(MouseButton.PRIMARY)) {
                zones.addUnit(new GUnit(), event.getZoneID());
                System.out.println(event.getMouseEvent().getX() + ", " + event.getMouseEvent().getY());
            } else if (event.getMouseEvent().getButton().equals(MouseButton.SECONDARY)){
                zones.removeUnit(new GUnit(), event.getZoneID());
            } else {
                zones.highlightNeighbour(event.getZoneID(), 1);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    zones.unHighlightNeigbour(event.getZoneID(), 1);
                }).start();
            }
        });

    }
}
