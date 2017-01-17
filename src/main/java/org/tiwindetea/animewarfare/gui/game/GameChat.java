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

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.AnimationsManager;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.PaperButton;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedNeteventListener;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GameChat extends BorderPane implements MessageReceivedNeteventListener {

    private final BorderPane topBP;

    private final Button minimize;
    private final Button stick;

    private boolean resizeBottom;
    private boolean resizeRight;
    private boolean minimized;
    private boolean containsMouse;
    private boolean sticky;
    private final FadeTransition ft;

    public GameChat() {

        this.ft = new FadeTransition(Duration.millis(2500), this);
        this.ft.setFromValue(1);
        this.ft.setToValue(0.4);
        this.ft.setCycleCount(Animation.INDEFINITE);

        setMaxHeight(160);
        setMaxWidth(200);

        this.topBP = new BorderPane();
        this.topBP.setPrefHeight(30);
        this.topBP.setStyle("-fx-background-color: peru; -fx-padding: 3"); // todo externalize

        setStyle("-fx-border-width: 1; -fx-border-color: black; -fx-background-color: peru; -fx-padding: 3"); // todo externalize

        setOpacity(0.5);
        setOnMouseExited(e -> {
            if (!this.sticky) {
                setOpacity(0.5);
            }
            this.containsMouse = false;
        });
        setOnMouseEntered(e -> {
            setOpacity(1);
            this.containsMouse = true;
            this.topBP.setStyle("-fx-background-color: peru; -fx-padding: 3"); // todo externalize
            setStyle("-fx-border-width: 1; -fx-border-color: black; -fx-background-color: peru; -fx-padding: 3"); // todo externalize
            this.ft.stop();
        });
        setTop(this.topBP);
        setCenter(GlobalChat.getChat());
        setMargin(GlobalChat.getChat(), new Insets(3, 10, 7, 0));

        this.minimize = new PaperButton("-");
        this.minimize.setEllipsisString("-");
        this.minimize.setPrefHeight(40);
        this.minimize.setPrefWidth(40);

        this.stick = new PaperButton("◇");
        this.stick.setEllipsisString("◇");
        this.stick.setPrefHeight(40);
        this.stick.setPrefWidth(40);
        this.stick.setOnMouseClicked(e -> {
            if (e.isStillSincePress()) {
                this.sticky = !this.sticky;
                if (this.sticky) {
                    this.stick.setEllipsisString("◆");
                    this.stick.setText("◆");
                } else {
                    this.stick.setEllipsisString("◇");
                    this.stick.setText("◇");
                }
            }

        });
        Tooltip t = new Tooltip("Transparency on/off");
        Tooltip.install(this.stick, t);

        makeDragable(this.topBP);
        makeDragable(this.minimize);
        makeDragable(this.stick);

        HBox b = new HBox(this.minimize, this.stick);
        b.setSpacing(15);
        b.setPadding(new Insets(3, 10, 3, 10));

        this.topBP.setLeft(b);
        this.setOnMouseClicked(mouseEvent -> toFront());
        this.minimize.setOnMouseReleased(e -> {
            if (e.isStillSincePress()) {
                minimizeClicked();
            }
        });

        makeResizable(10);
        EventDispatcher.registerListener(MessageReceivedNetevent.class, this);
    }

    private void minimizeClicked() {
        if (this.minimized) {
            setCenter(GlobalChat.getChat());
            this.minimize.setText("-");
            this.minimize.setEllipsisString("-");
            this.minimized = false;

            Scene s = getScene();
            if (getLayoutX() + getWidth() > s.getWidth()) {
                setLayoutY(s.getWidth() - getWidth());
            }
            if (getLayoutY() + getHeight() > s.getHeight()) {
                setLayoutY(s.getHeight() - getHeight());
            }
        } else {
            setCenter(null);
            this.minimize.setText("+");
            this.minimize.setEllipsisString("+");
            this.minimized = true;
        }
    }

    private void makeDragable(Node what) {
        final double deltas[] = new double[2];
        what.setOnMousePressed(mouseEvent -> {
            deltas[0] = getLayoutX() - mouseEvent.getSceneX();
            deltas[1] = getLayoutY() - mouseEvent.getSceneY();
            toFront();
        });
        what.setOnMouseDragged(mouseEvent -> {

            double newX = mouseEvent.getSceneX() + deltas[0];
            double newY = mouseEvent.getSceneY() + deltas[1];
            Scene s = getScene();
            if (newX + getWidth() > s.getWidth()) {
                newX = s.getWidth() - getWidth();
            } else if (newX < 0) {
                System.out.println("getX: " + mouseEvent.getX() + "\tgetSX:" + mouseEvent.getSceneX() + "\tgetScrX:" + mouseEvent.getScreenX());
                System.out.println("Delta: " + deltas[0] + "\n  ");
                newX = 0;
            }

            if (newY + getHeight() > s.getHeight()) {
                newY = s.getHeight() - getHeight();
            } else if (newY < 0) {
                newY = 0;
            }

            setLayoutX(newX);
            setLayoutY(newY);
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

    public void destroy() {
        //todo : call
        EventDispatcher.unregisterListener(MessageReceivedNetevent.class, this);
    }

    @Override
    public void handleMessage(MessageReceivedNetevent message) {
        Platform.runLater(() -> {
            setOpacity(1);
            this.topBP.setStyle("-fx-background-color: red; -fx-padding: 3"); // todo externalize
            setStyle("-fx-border-width: 1; -fx-border-color: black; -fx-background-color: red; -fx-padding: 3"); // todo externalize
            if (!this.containsMouse) {
                AnimationsManager.conditionalPlay(this.ft);
            }
        });
    }
}
