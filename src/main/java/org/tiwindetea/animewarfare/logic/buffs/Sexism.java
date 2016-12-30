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

import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.Player;
import org.tiwindetea.animewarfare.logic.states.events.BattleStartedEvent;
import org.tiwindetea.animewarfare.logic.states.events.BattleStartedEventListener;
import org.tiwindetea.animewarfare.logic.units.Unit;
import org.tiwindetea.animewarfare.logic.units.UnitBasicCharacteristics;

abstract class Sexism extends Buff implements BattleStartedEventListener {
	private final UnitBasicCharacteristics.Gender gender;

	protected int nbrOfGenderAttacker;
	protected Player attacker;
	protected int nbrOfGenderDefensor;
	protected Player defensor;

	protected Sexism(UnitBasicCharacteristics.Gender gender) {
		super(2);

		this.gender = gender;
		EventDispatcher.registerListener(BattleStartedEvent.class, this);
	}

	@Override
	public void handleBattleStartedEvent(BattleStartedEvent event) {
		this.attacker = event.getAttacker();
		this.defensor = event.getDefensor();
		updateGender(event);

		applyEffect();
	}

	protected abstract void applyEffect();

	private void updateGender(BattleStartedEvent event) {
		for (Unit unit : event.getZone().getUnits()) {
			if (unit.getFaction() == event.getAttacker().getFaction() && unit.getType()
			                                                                 .getUnitBasicCharacteristics()
			                                                                 .getGender() == this.gender) {
				++this.nbrOfGenderAttacker;
			} else if (unit.getFaction() == event.getDefensor().getFaction() && unit.getType()
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
		EventDispatcher.unregisterListener(BattleStartedEvent.class, this);
	}
}
