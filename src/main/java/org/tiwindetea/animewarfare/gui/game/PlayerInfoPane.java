package org.tiwindetea.animewarfare.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.StaffPointUpdatedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StaffPointUpdatedNeteventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benoit on 02/01/17.
 */
public class PlayerInfoPane extends Group implements StaffPointUpdatedNeteventListener {
	public enum Position {
		BOTTOM,
		TOP,
		LEFT,
		RIGHT
	}

	private boolean isForLocalPlayer = false;

	private final GameClientInfo playerInfo;

	private List<Production> productions = new ArrayList<>();

	private List<CampaignRightToken> campaignRights = new ArrayList<>();

	private StaffCounter staffCounter = new StaffCounter();

	public PlayerInfoPane(Position position, GameClientInfo playerInfo) {
		EventDispatcher.registerListener(StaffPointUpdatedNetevent.class, this);

		this.playerInfo = playerInfo;

		this.productions.add(new Production());
		this.productions.add(new Production());
		this.productions.add(new Production());
		this.productions.add(new Production());
		this.productions.add(new Production());
		this.productions.add(new Production());

		// layouts
		if (position == Position.LEFT || position == Position.RIGHT) {
			HBox rootHBox = new HBox();
			getChildren().add(rootHBox);

			VBox infoHBox = new VBox();

			VBox productionsVBox = new VBox();
			productionsVBox.setSpacing(10.);

			if (position == Position.RIGHT) {
				rootHBox.getChildren().add(infoHBox);
				rootHBox.getChildren().add(productionsVBox);

				productionsVBox.setPadding(new Insets(10., 0., 10., 10.));
				productionsVBox.setAlignment(Pos.CENTER_RIGHT);

			} else {
				rootHBox.getChildren().add(productionsVBox);
				rootHBox.getChildren().add(infoHBox);

				productionsVBox.setPadding(new Insets(10., 10., 10., 0.));
				productionsVBox.setAlignment(Pos.CENTER_LEFT);

			}

			VBox campaignRightsVBox = new VBox();
			campaignRightsVBox.setPadding(new Insets(10., 10., 10., 10.));
			campaignRightsVBox.setAlignment(Pos.CENTER);
			campaignRightsVBox.setSpacing(10.);
			infoHBox.getChildren().add(campaignRightsVBox);
			infoHBox.setVgrow(campaignRightsVBox, Priority.ALWAYS);

			VBox otherVBox = new VBox();
			otherVBox.setPadding(new Insets(10., 10., 10., 10.));
			otherVBox.setAlignment(Pos.CENTER);
			otherVBox.setSpacing(10.);
			infoHBox.getChildren().add(otherVBox);
			infoHBox.setVgrow(otherVBox, Priority.ALWAYS);

			this.campaignRights.add(new CampaignRightToken("?K"));
			campaignRightsVBox.getChildren().addAll(this.campaignRights);
			otherVBox.getChildren().addAll(this.staffCounter);
			productionsVBox.getChildren().addAll(this.productions);

			productionsVBox.getChildren().add(3, getPlayerLogo());
		} else {
			VBox rootVBox = new VBox();
			getChildren().add(rootVBox);

			HBox infoHBox = new HBox();

			HBox productionsHBox = new HBox();
			productionsHBox.setSpacing(10.);

			if (position == Position.BOTTOM) {
				rootVBox.getChildren().add(infoHBox);
				rootVBox.getChildren().add(productionsHBox);

				productionsHBox.setPadding(new Insets(10., 10., 0., 10.));
				productionsHBox.setAlignment(Pos.BOTTOM_CENTER);

				this.campaignRights.add(new CampaignRightToken("1K"));
				this.campaignRights.add(new CampaignRightToken("2K"));
				this.campaignRights.add(new CampaignRightToken("3K"));
				this.isForLocalPlayer = true;
			} else {
				rootVBox.getChildren().add(productionsHBox);
				rootVBox.getChildren().add(infoHBox);

				productionsHBox.setPadding(new Insets(0., 10., 10., 10.));
				productionsHBox.setAlignment(Pos.TOP_CENTER);

				for (Production production : this.productions) {
					production.setRotate(180);
				}

				this.campaignRights.add(new CampaignRightToken("?K"));
			}

			HBox campaignRightsHBox = new HBox();
			campaignRightsHBox.setPadding(new Insets(10., 10., 10., 10.));
			campaignRightsHBox.setAlignment(Pos.CENTER);
			campaignRightsHBox.setSpacing(10.);
			infoHBox.getChildren().add(campaignRightsHBox);
			infoHBox.setHgrow(campaignRightsHBox, Priority.ALWAYS);

			HBox otherHBox = new HBox();
			otherHBox.setPadding(new Insets(10., 10., 10., 10.));
			otherHBox.setAlignment(Pos.CENTER);
			otherHBox.setSpacing(10.);
			infoHBox.getChildren().add(otherHBox);
			infoHBox.setHgrow(otherHBox, Priority.ALWAYS);

			campaignRightsHBox.getChildren().addAll(this.campaignRights);
			otherHBox.getChildren().addAll(this.staffCounter);
			productionsHBox.getChildren().addAll(this.productions);

			productionsHBox.getChildren().add(3, getPlayerLogo());
		}
	}

	public Production getProductionByIndex(int index) {
		return this.productions.get(index);
	}

	public StaffCounter getStaffCounter() {
		return this.staffCounter;
	}

	public CampaignRightToken getCampaignRightTokenByWeight(int weight) {
		if (this.isForLocalPlayer) {
			return this.campaignRights.get(weight);
		}
		return this.campaignRights.get(0);
	}

	public void destroy() {
		// TODO: call on game quit.
		EventDispatcher.unregisterListener(StaffPointUpdatedNetevent.class, this);

		this.staffCounter = null;
		this.campaignRights.clear();
		this.productions.clear();
		getChildren().clear();
	}

	@Override
	public void handleStaffPointUpdated(StaffPointUpdatedNetevent event) {
		this.staffCounter.setValue(event.getNewValue());
	}

	// helper
	private VBox getPlayerLogo() {
		VBox logo = new VBox(10);
		logo.setAlignment(Pos.CENTER);
		Label playerName = new Label(this.playerInfo.getGameClientName());
		playerName.setTextFill(GlobalChat.getClientColor(this.playerInfo));
		Label playerFaction = new Label(GlobalChat.getClientFaction(this.playerInfo).name());
		playerFaction.setTextFill(GlobalChat.getClientColor(this.playerInfo));
		logo.getChildren().addAll(playerName, playerFaction);
		return logo;
	}
}
