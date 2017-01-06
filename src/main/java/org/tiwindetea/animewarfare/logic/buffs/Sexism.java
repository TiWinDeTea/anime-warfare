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

package org.tiwindetea.animewarfare.logic.buffs;

import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitBasicCharacteristics;

abstract class Sexism extends Buff implements BattleEventListener {
	private final UnitBasicCharacteristics.Gender gender;

	protected int nbrOfGenderAttacker;
	protected Player attacker;
	protected int nbrOfGenderDefensor;
	protected Player defender;

	protected Sexism(UnitBasicCharacteristics.Gender gender) {
		super(2);

		this.gender = gender;
		LogicEventDispatcher.registerListener(BattleEvent.class, this);
	}

	@Override
	public void handlePreBattle(BattleEvent event) {
		// nothing to do.
	}

	@Override
	public void handleDuringBattle(BattleEvent event) {
		this.attacker = event.getBattleContext().getAttacker().getPlayer();
		this.defender = event.getBattleContext().getDefender().getPlayer();
		updateGender(event);

		applyEffect();
	}

	@Override
	public void handleBattleFinished(BattleEvent event) {
		// nothing to do.
	}

	@Override
	public void handlePostBattle(BattleEvent event) {
		// nothing to do.
	}

	protected abstract void applyEffect();

	private void updateGender(BattleEvent event) {
		for (Unit unit : event.getBattleContext().getZone().getUnits()) {
			if (unit.hasFaction(event.getBattleContext().getAttacker().getPlayer().getFaction()) && unit.getType()
			                                                                                            .getUnitBasicCharacteristics()
			                                                                                            .getGender() == this.gender) {
				++this.nbrOfGenderAttacker;
			} else if (unit.hasFaction(event.getBattleContext()
			                                .getDefender()
			                                .getPlayer()
			                                .getFaction()) && unit.getType()
			                                                      .getUnitBasicCharacteristics()
			                                                      .getGender() == this.gender) {
				++this.nbrOfGenderDefensor;
			}
		}
	}

	@Override
	boolean isActionBuff() {
		return false;
	}

	@Override
	void destroy() {
		LogicEventDispatcher.unregisterListener(BattleEvent.class, this);
	}
}
