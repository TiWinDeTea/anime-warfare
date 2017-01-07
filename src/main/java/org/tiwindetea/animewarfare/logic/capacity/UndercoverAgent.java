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

package org.tiwindetea.animewarfare.logic.capacity;

import org.tiwindetea.animewarfare.logic.GameBoard;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.Zone;
import org.tiwindetea.animewarfare.logic.capacity.events.AskMascotToSwapEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEventListener;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitLevel;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEventListener;
import org.tiwindetea.animewarfare.net.logicevent.MascotToSwapEvent;
import org.tiwindetea.animewarfare.net.logicevent.MascotToSwapEventListener;
import org.tiwindetea.animewarfare.net.logicevent.UndercoverAgentCapacityEvent;
import org.tiwindetea.animewarfare.net.logicevent.UndercoverAgentCapacityEventListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UndercoverAgent extends PlayerCapacity implements UndercoverAgentCapacityEventListener, MascotToSwapEventListener {
	public static class UndercoverAgentActivable extends PlayerActivable implements StudioEventListener, StudioControllerChangedEventListener {
		private final Set<Studio> studioBuiltByOtherPlayers = new HashSet<>();
		private final GameBoard gameBoard;

		public UndercoverAgentActivable(Player player, GameBoard gameBoard) {
			super(player);
			this.gameBoard = gameBoard;

			LogicEventDispatcher.registerListener(StudioEvent.class, this);
		}

		@Override
		public void destroy() {
			LogicEventDispatcher.unregisterListener(StudioEvent.class, this);
		}

		@Override
		public void handleStudioAddedEvent(StudioEvent event) {
			if (event.getPlayerID() != getPlayer().getID()) {
				this.studioBuiltByOtherPlayers.add(this.gameBoard.getMap().getZone(event.getZoneID()).getStudio());
			}
		}

		@Override
		public void handleStudioRemovedEvent(StudioEvent event) {
			if (event.getPlayerID() != getPlayer().getID()) {
				this.studioBuiltByOtherPlayers.remove(this.gameBoard.getMap().getZone(event.getZoneID()).getStudio());
			}
		}

		@Override
		public void handleStudioController(StudioControllerChangedEvent event) {
			if (getPlayer().hasFaction(event.getControllerFaction())) {
				activateAndDestroy(new UndercoverAgent(getPlayer(), this.gameBoard));
			}
		}
	}

	private final GameBoard gameBoard;
	private Zone targetZone;
	private int targetPlayer = -1;

	public UndercoverAgent(Player player, GameBoard gameBoard) {
		super(player);
		this.gameBoard = gameBoard;
	}

	@Override
	public void use() {
		if (this.targetZone != null) {
			LogicEventDispatcher.registerListener(MascotToSwapEvent.class, this);
			LogicEventDispatcher.send(new AskMascotToSwapEvent(this.targetPlayer, this.targetZone.getID()));
		}
	}

	@Override
	public void handleUndercoverAgentZoneChoice(UndercoverAgentCapacityEvent event) {
		this.targetZone = this.gameBoard.getMap().getZone(event.getZoneID());
		this.targetPlayer = event.getTargetPlayerID();
	}

	@Override
	public void handleMascotToSwapEvent(MascotToSwapEvent event) {
		Unit mascot = this.targetZone.getUnit(event.getUnitID());
		if (mascot != null && mascot.isLevel(UnitLevel.MASCOT) && this.gameBoard.getPlayer(event.getPlayerID())
		                                                                        .hasFaction(mascot.getFaction())) {
			boolean isController = this.targetZone.getStudio().getController().equals(mascot);
			mascot.removeFromMap();

			this.gameBoard.getPlayer(event.getPlayerID()).getUnitCounter().removeUnit(mascot.getType());

			UnitType casterMascotType = Arrays.stream(UnitType.values())
			                                  .filter(t -> t.isLevel(UnitLevel.MASCOT) && getPlayer().hasFaction(t.getDefaultFaction()))
			                                  .findFirst()
			                                  .get(); // Can't be null

			if (getPlayer().getUnitCounter().getNumberOfUnits(UnitLevel.MASCOT) == casterMascotType.getMaxNumber()) {
				Unit unit = new Unit(casterMascotType);
				unit.addInZone(this.targetZone);
				if (isController) {
					this.targetZone.getStudio().setController(unit);
				}
			}
		}
	}

	@Override
	public CapacityName getName() {
		return CapacityName.UNDERCOVER_AGENT;
	}
}
