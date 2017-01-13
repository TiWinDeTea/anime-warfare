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

package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattle;

import java.util.Map;

/**
 * @author Lucas Lazare
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public class BattleNetevent implements Event<BattleNeteventListener> {
	public enum Type {
		PRE_BATTLE {
			@Override
			void handle(BattleNetevent event, BattleNeteventListener battleEventListener) {
				battleEventListener.handlePreBattle(event);
			}
		},
		DURING_BATTLE {
			@Override
			void handle(BattleNetevent event, BattleNeteventListener battleEventListener) {
				battleEventListener.handleDuringBattle(event);
			}
		},
		POST_BATTLE {
			@Override
			void handle(BattleNetevent event, BattleNeteventListener battleEventListener) {
				battleEventListener.handlePostBattle(event);
			}
		},
		BATTLE_FINISHED {
			@Override
			void handle(BattleNetevent event, BattleNeteventListener battleEventListener) {
				battleEventListener.handleBattleFinished(event);
			}
		};

		abstract void handle(BattleNetevent event, BattleNeteventListener battleEventListener);
	}

	private final GameClientInfo attacker;
	private final GameClientInfo defender;
	private final int zone;
	private final Type type;

	private final Map<GameClientInfo, Integer> attack;
	private final Map<GameClientInfo, Integer> numberOfWoundeds;
	private final Map<GameClientInfo, Integer> numberOfDeads;

	public BattleNetevent(NetBattle netBattle) {
		this.attacker = netBattle.getAttacker();
		this.defender = netBattle.getDefender();
		this.zone = netBattle.getZone();
		this.type = netBattle.getType();
		this.attack = netBattle.getAttack();
		this.numberOfWoundeds = netBattle.getNumberOfWoundeds();
		this.numberOfDeads = netBattle.getNumberOfDeads();
	}

	@Override
	public void notify(BattleNeteventListener listener) {
		this.type.handle(this, listener);
	}

	public GameClientInfo getAttacker() {
		return this.attacker;
	}

	public GameClientInfo getDefender() {
		return this.defender;
	}

	public int getZone() {
		return this.zone;
	}

	public Map<GameClientInfo, Integer> getAttack() {
		return this.attack;
	}

	public Map<GameClientInfo, Integer> getNumberOfWoundeds() {
		return this.numberOfWoundeds;
	}

	public Map<GameClientInfo, Integer> getNumberOfDeads() {
		return this.numberOfDeads;
	}
}

