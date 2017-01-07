package org.tiwindetea.animewarfare.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.lomadriel.lfc.statemachine.DefaultStateMachine;
import org.tiwindetea.animewarfare.gui.game.GameState;

public class ZoneMapTest extends Application {

    @Test
    public void test() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        new DefaultStateMachine(new GameState(root));
    }
}
