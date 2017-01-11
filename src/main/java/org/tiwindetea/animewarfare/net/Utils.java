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

package org.tiwindetea.animewarfare.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.logic.LogicEventDispatcher;
import org.tiwindetea.animewarfare.logic.battle.event.BattleEvent;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;
import org.tiwindetea.animewarfare.logic.capacity.CapacityType;
import org.tiwindetea.animewarfare.logic.events.GameEndConditionsReachedEvent;
import org.tiwindetea.animewarfare.logic.events.MarketingLadderUpdatedEvent;
import org.tiwindetea.animewarfare.logic.events.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.events.StaffPointUpdatedEvent;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.logic.events.UnitCounterEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskMascotToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.logic.units.UnitType;
import org.tiwindetea.animewarfare.logic.units.events.StudioControllerChangedEvent;
import org.tiwindetea.animewarfare.logic.units.events.UnitMovedEvent;
import org.tiwindetea.animewarfare.net.logicevent.MoveUnitsEvent;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetBattlePhaseReadyRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetCapturedMascotSelection;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetConventionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetFirstPlayerSelection;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetInvokeUnitRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetMascotCaptureRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetMoveUnitsRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetOpenStudioRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPassword;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectUnitsRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectWoundedUnitsRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSkipAllRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetStartBattleRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnlockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUnselectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetUseCapacityRequest;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBadPassword;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattle;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionLocked;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionUnlocked;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFactionUnselected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFanNumberUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEndConditionsReached;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameStarted;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMarketingLadderUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetPhaseChanged;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectMascotToCapture;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStaffPointsUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudio;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetStudioControllerChanged;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetUnitCountChange;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetUnitMoveEvent;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class regroups some methods used in the net package
 *
 * @author Lucas Lazare
 * @since 0.1.0
 */
class Utils {

	static final String VERSION_HEADER = "Anime-Wafare v0.1.0";
	static final Charset CHARSET = Charset.defaultCharset();

	static List<InetAddress> findBroadcastAddr() {

		LinkedList<InetAddress> broadcastAddresses = new LinkedList<>();
		Enumeration<NetworkInterface> en = Collections.emptyEnumeration();

		try {
			en = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			Log.warn(Utils.class.getName(), "Failed to find broacast addresses", e);
		}

		while (en.hasMoreElements()) {
			List<InterfaceAddress> list = en.nextElement().getInterfaceAddresses();
			broadcastAddresses.addAll(list.stream()
			                              .filter(ia -> ia.getBroadcast() != null)
			                              .map(InterfaceAddress::getBroadcast)
			                              .collect(Collectors.toList()));
		}
		return broadcastAddresses;
	}

	/**
	 * Register classes sent by network.
	 */
	static void registerClasses(EndPoint endPoint) {
		//***************************************************************
		//*                                                             *
		//*                    ALPHABETICAL ORDER                       *
		//*                                                             *
		//***************************************************************

		Kryo kryo = endPoint.getKryo();

		// general
		kryo.register(ArrayList.class);
		kryo.register(GameClientInfo.class);
		kryo.register(HashMap.class);
		kryo.register(LinkedList.class);
		kryo.register(NetPlayingOrderChosen.class);
		kryo.register(Room.class);
		kryo.register(String.class);

		// client, alphabetical order
		kryo.register(NetBattlePhaseReadyRequest.class);
		kryo.register(NetCapturedMascotSelection.class);
		kryo.register(NetConventionRequest.class);
		kryo.register(NetFirstPlayerSelection.class);
		kryo.register(NetInvokeUnitRequest.class);
		kryo.register(NetLockFactionRequest.class);
		kryo.register(NetMascotCaptureRequest.class);
		kryo.register(NetMoveUnitsRequest.class);
		kryo.register(NetOpenStudioRequest.class);
		kryo.register(NetPassword.class);
		kryo.register(NetSkipAllRequest.class);
		kryo.register(NetSelectFactionRequest.class);
		kryo.register(NetSelectUnitsRequest.class);
		kryo.register(NetSelectWoundedUnitsRequest.class);
		kryo.register(NetStartBattleRequest.class);
		kryo.register(NetUnselectFactionRequest.class);
		kryo.register(NetUnlockFactionRequest.class);
		kryo.register(NetUseCapacityRequest.class);

		// server, alphabetical order
		kryo.register(NetBadPassword.class);
		kryo.register(NetBattle.class);
		kryo.register(NetFactionLocked.class);
		kryo.register(NetFactionSelected.class);
		kryo.register(NetFactionUnlocked.class);
		kryo.register(NetFactionUnselected.class);
		kryo.register(NetFanNumberUpdated.class);
		kryo.register(NetFirstPlayerSelected.class);
		kryo.register(NetFirstPlayerSelectionRequest.class);
		kryo.register(NetGameEndConditionsReached.class);
		kryo.register(NetGameEnded.class);
		kryo.register(NetGameStarted.class);
		kryo.register(NetHandlePlayerDisconnection.class);
		kryo.register(NetMarketingLadderUpdated.class);
		kryo.register(NetMessage.class);
		kryo.register(NetStaffPointsUpdated.class);
		kryo.register(NetStudio.class);
		kryo.register(NetStudioControllerChanged.class);
		kryo.register(NetPhaseChanged.class);
		kryo.register(NetSelectMascotToCapture.class);
		kryo.register(NetUnitCountChange.class);
		kryo.register(NetUnitMoveEvent.class);

		// inner
		kryo.register(CapacityName.class);
		kryo.register(CapacityType.class);
		kryo.register(FactionType.class);
		kryo.register(UnitCounterEvent.Type.class);
		kryo.register(PhaseChangedEvent.Phase.class);
		kryo.register(MoveUnitsEvent.Movement.class);
		kryo.register(UnitType.class);
		kryo.register(StudioEvent.Type.class);
	}

	static void registerAsLogicListener(GameServer.LogicListener logicListener) {

		//***************************************************************
		//*                                                             *
		//*                    ALPHABETICAL ORDER                       *
		//*                                                             *
		//***************************************************************

		EventDispatcher ed = LogicEventDispatcher.getInstance();

		//logic.states.events, alphabetical order
		ed.addListener(AskFirstPlayerEvent.class, logicListener);
		ed.addListener(AskMascotToCaptureEvent.class, logicListener);
		ed.addListener(BattleEvent.class, logicListener);
		ed.addListener(FirstPlayerSelectedEvent.class, logicListener);
		ed.addListener(GameEndedEvent.class, logicListener);
		ed.addListener(PhaseChangedEvent.class, logicListener);

		//logic.event
		ed.addListener(GameEndConditionsReachedEvent.class, logicListener);
		ed.addListener(MarketingLadderUpdatedEvent.class, logicListener);
		ed.addListener(NumberOfFansChangedEvent.class, logicListener);
		ed.addListener(StaffPointUpdatedEvent.class, logicListener);
		ed.addListener(StudioControllerChangedEvent.class, logicListener);
		ed.addListener(StudioEvent.class, logicListener);
		ed.addListener(UnitCounterEvent.class, logicListener);
		ed.addListener(UnitMovedEvent.class, logicListener);
	}

	static void deregisterLogicListener(GameServer.LogicListener logicListener) {

		//***************************************************************
		//*                                                             *
		//*                    ALPHABETICAL ORDER                       *
		//*                                                             *
		//***************************************************************

		EventDispatcher ed = LogicEventDispatcher.getInstance();

		//logic.states.events, alphabetical order
		ed.removeListener(AskFirstPlayerEvent.class, logicListener);
		ed.removeListener(AskMascotToCaptureEvent.class, logicListener);
		ed.removeListener(BattleEvent.class, logicListener);
		ed.removeListener(FirstPlayerSelectedEvent.class, logicListener);
		ed.removeListener(GameEndedEvent.class, logicListener);
		ed.removeListener(PhaseChangedEvent.class, logicListener);

		//logic.event
		ed.removeListener(GameEndConditionsReachedEvent.class, logicListener);
		ed.removeListener(MarketingLadderUpdatedEvent.class, logicListener);
		ed.removeListener(NumberOfFansChangedEvent.class, logicListener);
		ed.removeListener(StaffPointUpdatedEvent.class, logicListener);
		ed.removeListener(StudioControllerChangedEvent.class, logicListener);
		ed.removeListener(StudioEvent.class, logicListener);
		ed.removeListener(UnitCounterEvent.class, logicListener);
		ed.removeListener(UnitMovedEvent.class, logicListener);
	}
}
