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

package org.tiwindetea.animewarfare.logic;

import org.tiwindetea.animewarfare.logic.buffs.BuffManager;
import org.tiwindetea.animewarfare.logic.buffs.HimejiAttackBuff;
import org.tiwindetea.animewarfare.logic.buffs.LelouchAttackBuff;
import org.tiwindetea.animewarfare.logic.buffs.NyarukoAttackBuff;
import org.tiwindetea.animewarfare.logic.capacity.Capacity;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.capacity.SelfActivable;
import org.tiwindetea.animewarfare.logic.events.AdvertisingCampaignRightEvent;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.events.ProductionEvent;
import org.tiwindetea.animewarfare.logic.events.StaffPointUpdatedEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCapturedEvent;
import org.tiwindetea.animewarfare.logic.units.Studio;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/*
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 */
public class Player {
	final List<SelfActivable> activables = new ArrayList<>();
	private WeakReference<Player> clockwiseNextPlayer = new WeakReference<>(null);
	private WeakReference<Player> clockwisePreviousPlayer = new WeakReference<>(null);
	private WeakReference<Player> nextPlayerInGameOrder = new WeakReference<>(null);

	private static final int BATTLE_COST = 1; // TODO: Externalize

	private final int ID;

	private int fanNumber;
	private int staffAvailable;

	private final FactionType faction;
	private final BuffManager buffManager = new BuffManager();
	private final UnitCounter unitCounter = new UnitCounter();
	private final CostModifier costModifier;
	private final Map<CapacityName, Capacity> capacities = new HashMap<>();
	private final Set<Unit> unitCaptured = new HashSet<>();
	private final List<AdvertisingCampaignRight> advertisingCampaignRights = new ArrayList<>();

	private Studio studio;

	public Player(int id, FactionType faction) {
		this.ID = id;
		this.faction = faction;
		this.costModifier = new CostModifier(this.ID);

		switch (this.faction) {
			case THE_BLACK_KNIGHTS:
				this.buffManager.addBuff(new LelouchAttackBuff());
				break;
			case HAIYORE:
				this.buffManager.addBuff(new NyarukoAttackBuff());
				break;
			case F_CLASS_NO_BAKA:
				this.buffManager.addBuff(new HimejiAttackBuff());
				break;
		}
	}

	public void setClockwiseNextPlayer(Player p) {
		this.clockwiseNextPlayer = new WeakReference<>(p);
	}

	public Player getClockwiseNextPlayer() {
		return this.clockwiseNextPlayer.get();
	}

	public void setClockwisePreviousPlayer(Player clockwisePreviousPlayer) {
		this.clockwisePreviousPlayer = new WeakReference(clockwisePreviousPlayer);
	}

	public Player getClockwisePreviousPlayer() {
		return this.clockwisePreviousPlayer.get();
	}

	public void setClockwiseness(boolean clockwiseness) {
		if (clockwiseness) {
			if (this.nextPlayerInGameOrder != this.clockwiseNextPlayer) { // checking references
				this.nextPlayerInGameOrder = this.clockwiseNextPlayer;
				this.clockwiseNextPlayer.get().setClockwiseness(clockwiseness);
			}
		} else {
			if (this.nextPlayerInGameOrder != this.clockwisePreviousPlayer) { // checking references
				this.nextPlayerInGameOrder = this.clockwisePreviousPlayer;
				this.clockwiseNextPlayer.get().setClockwiseness(clockwiseness);
			}
		}
	}

	public Player getNextPlayerInGameOrder() {
		return this.nextPlayerInGameOrder.get();
	}

	public int getID() {
		return this.ID;
	}

	public void setStaffAvailable(int staffAvailable) {
		this.staffAvailable = staffAvailable;

		LogicEventDispatcher.send(new StaffPointUpdatedEvent(this.ID, this.staffAvailable));
	}

	public boolean hasRequiredStaffPoints(float actionCost, int numberOfActions) {
		assert (actionCost > 0 && numberOfActions >= 1);

		return actionCost * numberOfActions <= this.staffAvailable;
	}

	public boolean hasRequiredStaffPoints(float actionCost) {
		return hasRequiredStaffPoints(actionCost, 1);
	}

	public void decrementStaffPoints(float actionCost, int numberOfActions) {
		assert (hasRequiredStaffPoints(actionCost, numberOfActions));

		setStaffAvailable((int) Math.ceil(this.staffAvailable - actionCost * numberOfActions));
	}

	public void decrementStaffPoints(int actionCost) {
		decrementStaffPoints(actionCost, 1);
	}

	public int getStaffAvailable() {
		return this.staffAvailable;
	}

	public int getFanNumber() {
		return this.fanNumber;
	}

	public void incrementFans(int numberOfFans) {
		if (numberOfFans < 0) {
			throw new IllegalArgumentException();
		}

		this.fanNumber += numberOfFans;
		LogicEventDispatcher.getInstance().fire(new NumberOfFansChangedEvent(this, numberOfFans, this.fanNumber));
	}

	public void decrementFans(int numberOfFans) {
		if (numberOfFans > 0) {
			throw new IllegalArgumentException();
		}

		this.fanNumber -= numberOfFans;
		LogicEventDispatcher.getInstance().fire(new NumberOfFansChangedEvent(this, -numberOfFans, this.fanNumber));
	}

	public FactionType getFaction() {
		return this.faction;
	}

	public boolean hasFaction(FactionType factionType) {
		return this.faction == factionType;
	}

	public UnitCounter getUnitCounter() {
		return this.unitCounter;
	}

	public CostModifier getCostModifier() {
		return this.costModifier;
	}

	public boolean addUnitCaptured(Unit unit, Player huntedPlayer) {
		if (this.unitCaptured.add(unit)) {
			LogicEventDispatcher.send(new UnitCapturedEvent(this, huntedPlayer, unit.getFaction(), unit.getType()));
			return true;
		}

		return false;
	}

	public Set<Unit> getUnitCaptured() {
		return this.unitCaptured;
	}

	public List<AdvertisingCampaignRight> getAdvertisingCampaignRights() {
		return Collections.unmodifiableList(this.advertisingCampaignRights);
	}

	public void addAdvertisingCampaignRights(AdvertisingCampaignRight advertisingCampaignRight) {
		if (this.advertisingCampaignRights.add(advertisingCampaignRight)) {
			LogicEventDispatcher.send(new AdvertisingCampaignRightEvent(AdvertisingCampaignRightEvent.Type.ADDED,
					this.ID,
					advertisingCampaignRight.getWeight()));
		}
	}

	public AdvertisingCampaignRight removeAdvertisingCampainRights(int weight) {
		AdvertisingCampaignRight right = getAdvertisingCampaignRight(weight);

		if (this.advertisingCampaignRights.remove(right)) {
			LogicEventDispatcher.send(new AdvertisingCampaignRightEvent(AdvertisingCampaignRightEvent.Type.REMOVED,
					this.ID, right.getWeight()));
		}

		return right;
	}

	public boolean revealAdvertisingCampainRights(int weight) { // FIXME: Don't forget this
		AdvertisingCampaignRight campaignRight = getAdvertisingCampaignRight(weight);

		if (campaignRight != null) {
			this.advertisingCampaignRights.remove(campaignRight);
			LogicEventDispatcher.send(new AdvertisingCampaignRightEvent(AdvertisingCampaignRightEvent.Type.REVEALED,
					this.ID, campaignRight.getWeight()));
			return true;
		}

		return false;
	}

	private AdvertisingCampaignRight getAdvertisingCampaignRight(int weight) {
		return this.advertisingCampaignRights.stream()
		                                     .filter(a -> a.getWeight() == weight)
		                                     .findFirst()
		                                     .orElse(null);
	}

	public void clearAdvertisingCampaignRights() { // TODO: Is this realy needed ?
		this.advertisingCampaignRights.clear();
	}

	public void activateCapacity(Capacity capacity) {
		assert (this.capacities.get(capacity.getName()) == null);

		this.capacities.put(capacity.getName(), capacity);

		LogicEventDispatcher.send(new ProductionEvent(ProductionEvent.Type.ACTIVATED, this.ID, capacity.getName()));
	}

	public void useCapacity(CapacityName capacityName) {
		this.capacities.get(capacityName).use();
		// TODO: fire event capacityUsed.
	}

	public int getNumberOfProduction() {
		return (int) this.capacities.keySet().stream().filter(k -> hasFaction(k.getFaction())).count();
	}

	public boolean hasCapacity(CapacityName type) {
		return this.capacities.containsKey(type);
	}

	public void desactivateCapactiy(CapacityName capacity) {
		assert (this.capacities.get(capacity) != null);

		this.capacities.remove(capacity);

		LogicEventDispatcher.send(new ProductionEvent(ProductionEvent.Type.ACTIVATED, this.ID, capacity));
	}

	public Set<CapacityName> getActivatedCapacities() {
		return this.capacities.keySet();
	}

	public int getBattleCost() {
		int cost = BATTLE_COST + this.costModifier.getBattleCostModifier();

		return cost < 0 ? 0 : cost;
	}

	public int getUnitCost(UnitType type) {
		float cost = this.costModifier.getUnitCostModifier(type) + type.getDefaultCost();

		return (int) (cost < 0 ? 0 : cost);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Player player = (Player) o;
		return this.ID == player.ID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.ID));
	}

	public Studio getStudio() {
		return this.studio;
	}

	public void setStudio(Studio studio) {
		Studio previousStudio = this.studio;
		this.studio = studio;

		if (studio != null) {
			LogicEventDispatcher.send(StudioEvent.addedToPlayer(studio, this.ID));
		} else {
			LogicEventDispatcher.send(StudioEvent.removedFromPlayer(previousStudio, this.ID));
		}
	}

	public BuffManager getBuffManager() {
		return this.buffManager;
	}
}
