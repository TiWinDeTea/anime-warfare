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

package org.tiwindetea.animewarfare.gui.menu;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GUIState;
import org.tiwindetea.animewarfare.gui.event.AskMenuStateUpdateEvent;
import org.tiwindetea.animewarfare.gui.menu.event.SettingsMenuEvent;
import org.tiwindetea.animewarfare.gui.menu.event.SettingsMenuEventListener;
import org.tiwindetea.animewarfare.settings.Settings;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;

/**
 * Settings menu state.
 *
 * @author Benoît CORTIER
 */
public class SettingsMenuState extends GUIState implements SettingsMenuEventListener {
	private static AnchorPane settingsMenu;
	private static SettingsMenuController settingsMenuController;

	private static final Duration ANIMATION_DURATION = Duration.millis(350);
	private static ParallelTransition transition;
	private static FadeTransition fadeTransition;
	private static Timeline scaleTimeline;
	private static Timeline positionTimeline;

	private static BlurTransition bt;

	static {
		FXMLLoader settingsMenuLoader = new FXMLLoader();
		settingsMenuLoader.setLocation(MainMenuController.class.getResource("SettingsMenu.fxml"));
		settingsMenuLoader.setResources(ResourceBundleHelper.getBundle(
				"org.tiwindetea.animewarfare.gui.menu.SettingsMenuController"));

		try {
			SettingsMenuState.settingsMenu = settingsMenuLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SettingsMenuState.settingsMenuController = settingsMenuLoader.getController();

		fadeTransition = new FadeTransition(ANIMATION_DURATION, SettingsMenuState.settingsMenu);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.setCycleCount(1);
		fadeTransition.setAutoReverse(false);

		scaleTimeline = new Timeline(new KeyFrame(ANIMATION_DURATION,
				new KeyValue(SettingsMenuState.settingsMenu.scaleXProperty(), 1)));
		scaleTimeline.setCycleCount(1);
		scaleTimeline.setAutoReverse(false);

		positionTimeline = new Timeline();
		positionTimeline.setCycleCount(1);
		positionTimeline.setAutoReverse(false);

		transition = new ParallelTransition(fadeTransition, scaleTimeline, positionTimeline);
		transition.setAutoReverse(false);
		transition.setCycleCount(1);


		bt = new BlurTransition(settingsMenu);
		bt.setBegin(10, 10);
		bt.setEnd(0, 0);
		bt.setDuration(ANIMATION_DURATION);
	}

	public SettingsMenuState(BorderPane rootLayout) {
		super(rootLayout);
	}

	@Override
	public void onEnter() {

		int selectedTransition = 1;

		if (Settings.areAnimationEffectsEnabled()) {
			switch (selectedTransition) {
				case 0:
					SettingsMenuState.settingsMenu.setTranslateX(-2 * this.rootLayout.getWidth() / 5. + 15);
					SettingsMenuState.settingsMenu.setScaleX(0.1);
					positionTimeline.getKeyFrames()
							.setAll(new KeyFrame(ANIMATION_DURATION,
									new KeyValue(SettingsMenuState.settingsMenu.translateXProperty(), 0)));
					transition.play();
					break;
				case 1:
				default:
					bt.play();
			}
		}

		// place the node in the root layout.
		this.rootLayout.setCenter(SettingsMenuState.settingsMenu);


		// listen events.
		EventDispatcher.getInstance().addListener(SettingsMenuEvent.class, this);
	}

	@Override
	public void onExit() {
		// stop listening events.
		EventDispatcher.getInstance().removeListener(SettingsMenuEvent.class, this);
	}

	@Override
	public void handleSettingsMenuQuit() {
		this.nextState = new MainMenuState(this.rootLayout);
		EventDispatcher.getInstance().fire(new AskMenuStateUpdateEvent());
	}

	public static class BlurTransition extends Transition {

		private final Node node;
		private BoxBlur blur = new BoxBlur();
		private double wb, hb, we, he;

		public BlurTransition(Node node, double widthBeg, double heightBeg, double widthEnd, double heightEnd) {
			setCycleDuration(Duration.millis(1000));
			this.node = node;
			this.wb = widthBeg;
			this.hb = heightBeg;
			this.we = widthEnd;
			this.he = heightEnd;
		}

		public BlurTransition(Node node) {
			this.node = node;
		}

		@Override
		public void play() {
			this.blur.setWidth(this.wb);
			this.blur.setHeight(this.wb);
			this.node.setEffect(this.blur);
			super.play();
		}

		@Override
		protected void interpolate(double v) {
			this.blur.setHeight(this.hb + (this.he - this.hb) * v);
			this.blur.setWidth(this.wb + (this.we - this.wb) * v);
		}

		public void setBegin(int width, int height) {
			this.wb = width;
			this.hb = height;
		}

		public void setDuration(Duration duration) {
			setCycleDuration(duration);
		}

		public void setEnd(int width, int height) {
			this.we = width;
			this.he = height;
		}
	}
}
