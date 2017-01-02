package org.tiwindetea.animewarfare.gui.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benoit on 02/01/17.
 */
public class PlayerInfoPane extends Group {
	public enum Position {
		BOTTOM,
		TOP,
		LEFT,
		RIGHT
	}

	private boolean isForLocalPlayer = false;

	private List<Production> productions = new ArrayList<>();

	private List<CampaignRightToken> campaignRights = new ArrayList<>();

	private StaffCounter staffCounter = new StaffCounter();

	public PlayerInfoPane(Position position) {
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
}
