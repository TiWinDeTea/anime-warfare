package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.MenuItem;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.game.GameLayoutController;
import org.tiwindetea.animewarfare.gui.game.gameboard.GUnit;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnitCaptureRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CaptureUnit extends AbstractZoneFilter {
	private Comparator<GUnit> levelComparator = Comparator.comparingInt(u2 -> u2.getType().getUnitLevel().ordinal());

	@Override
	public List<MenuItem> apply(FactionType factionType, Integer zoneID) {
		List<GUnit> units = GameLayoutController.getMap().getUnits(zoneID.intValue());

		Set<FactionType> possibleFactions = units.stream()
		                                         .map(u -> u.getFaction())
		                                         .collect(Collectors.toSet());
		if (!possibleFactions.isEmpty()) {

			GUnit hunter = units.stream().filter(u -> u.getFaction() == factionType)
			                    .max(this.levelComparator)
			                    .orElse(null);

			if (hunter != null) {
				List<MenuItem> items = new ArrayList<>();

				for (FactionType possibleFaction : possibleFactions) {
					List<GUnit> playerUnits = units.stream()
					                               .filter(u -> u.getFaction() == possibleFaction)
					                               .collect(Collectors.toList());
					if (playerUnits.stream().anyMatch(u -> u.getType().getUnitLevel() == UnitLevel.MASCOT)) {
						GUnit protector = playerUnits.stream()
						                             .max(this.levelComparator)
						                             .orElse(null);
						if (Objects.compare(hunter, protector, this.levelComparator) > 0) {
							MenuItem menuItem = new MenuItem("Capture mascot of "
									+ GlobalChat.getFactionClient(possibleFaction));
							menuItem.setOnAction(event -> MainApp.getGameClient()
							                                     .send(new NetUnitCaptureRequest(
									                                     GlobalChat.getFactionClient(possibleFaction)
									                                               .getId(),
									                                     zoneID.intValue(),
									                                     UnitLevel.MASCOT,
									                                     false)));
							items.add(menuItem);
						}
					}
				}

				return items;
			}
		}

		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void destroy() {

	}
}
