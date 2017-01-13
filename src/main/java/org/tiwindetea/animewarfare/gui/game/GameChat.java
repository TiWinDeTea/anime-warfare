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

package org.tiwindetea.animewarfare.gui.game;

import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperButton;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameChat extends BorderPane {

    private final BorderPane topBP;

    private final Button minimize;

    private boolean resizeBottom;
    private boolean resizeRight;
    private boolean minimized;

    public GameChat() {

        setMaxHeight(160);
        setMaxWidth(200);

        this.topBP = new BorderPane();
        this.topBP.setPrefHeight(30);
        this.topBP.setStyle("-fx-background-color: burlywood; -fx-padding: 3");

        setStyle("-fx-border-width: 1; -fx-border-color: black; -fx-background-color: burlywood; -fx-padding: 3");

        setTop(this.topBP);
        setCenter(GlobalChat.getChat());

        this.minimize = new PaperButton("-");
        this.minimize.setEllipsisString("-");
        this.minimize.setPrefHeight(50);
        this.minimize.setPrefWidth(50);

        makeDragable(this.topBP);
        this.topBP.setRight(this.minimize);
        this.setOnMouseClicked(mouseEvent -> toFront());
        this.minimize.setOnAction(this::minimizeClicked);

        makeResizable(50);
    }

    private void minimizeClicked(ActionEvent actionEvent) {
        if (this.minimized) {
            setCenter(GlobalChat.getChat());
            this.minimize.setText("+");
            this.minimize.setEllipsisString("+");
            this.minimized = false;
        } else {
            setCenter(null);
            this.minimize.setText("-");
            this.minimize.setEllipsisString("-");
            this.minimized = true;
        }
    }

    private void makeDragable(Node what) {
        final double deltas[] = new double[2];
        what.setOnMousePressed(mouseEvent -> {
            deltas[0] = getLayoutX() - mouseEvent.getScreenX();
            deltas[1] = getLayoutY() - mouseEvent.getScreenY();
            toFront();
        });
        what.setOnMouseDragged(mouseEvent -> {
            this.setLayoutX(mouseEvent.getScreenX() + deltas[0]);
            this.setLayoutY(mouseEvent.getScreenY() + deltas[1]);
        });
    }

    private void makeResizable(double mouseBorderWidth) {
        this.setOnMouseMoved(mouseEvent -> {
            //local window's coordiantes
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            //window size
            double width = this.boundsInLocalProperty().get().getWidth();
            double height = this.boundsInLocalProperty().get().getHeight();
            //if we on the edge, change state and cursor
            if (Math.abs(mouseX - width) < mouseBorderWidth) {
                if (Math.abs(mouseY - height) < mouseBorderWidth) {
                    this.resizeRight = true;
                    this.resizeBottom = true;
                    this.setCursor(Cursor.SE_RESIZE);
                } else {
                    this.resizeRight = true;
                    this.resizeBottom = false;
                    this.setCursor(Cursor.E_RESIZE);
                }
            } else if (Math.abs(mouseY - height) < mouseBorderWidth) {
                this.resizeBottom = true;
                this.resizeRight = false;
                this.setCursor(Cursor.S_RESIZE);
            } else {
                this.resizeBottom = false;
                this.resizeRight = false;
                this.setCursor(Cursor.DEFAULT);
            }

        });
        this.setOnMouseDragged(mouseEvent -> {
            if (this.resizeBottom && this.resizeRight) {
                setMaxSize(mouseEvent.getX(), mouseEvent.getY());
            } else if (this.resizeRight) {
                setMaxWidth(mouseEvent.getX());
            } else if (this.resizeBottom) {
                setMaxHeight(mouseEvent.getY());
            }
        });
    }
}
