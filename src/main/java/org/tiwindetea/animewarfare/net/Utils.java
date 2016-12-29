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
import org.tiwindetea.animewarfare.logic.event.GameEndConditionsReachedEvent;
import org.tiwindetea.animewarfare.logic.event.MarketingLadderUpdatedEvent;
import org.tiwindetea.animewarfare.logic.event.NumberOfFansChangedEvent;
import org.tiwindetea.animewarfare.logic.event.StudioAddedEvent;
import org.tiwindetea.animewarfare.logic.event.UnitEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskFirstPlayerEvent;
import org.tiwindetea.animewarfare.logic.states.events.AskMascotToCaptureEvent;
import org.tiwindetea.animewarfare.logic.states.events.BattleStartedEvent;
import org.tiwindetea.animewarfare.logic.states.events.FirstPlayerSelectedEvent;
import org.tiwindetea.animewarfare.logic.states.events.GameEndedEvent;
import org.tiwindetea.animewarfare.logic.states.events.PhaseChangedEvent;
import org.tiwindetea.animewarfare.net.networkrequests.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;
import org.tiwindetea.animewarfare.net.networkrequests.NetSelectFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.NetUnitEvent;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetBattleStarted;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFanNumberUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelected;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetFirstPlayerSelectionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEndConditionsReached;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetGameEnded;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetHandlePlayerDisconnection;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMarketingLadderUpdated;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetMessage;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetNewStudio;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetPhaseChange;
import org.tiwindetea.animewarfare.net.networkrequests.server.NetSelectMascotToCapture;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
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
		Kryo kryo = endPoint.getKryo();

		// general
		kryo.register(GameClientInfo.class);
		kryo.register(Room.class);
		kryo.register(String.class);
		kryo.register(LinkedList.class);
		kryo.register(ArrayList.class);

		// network requests
		kryo.register(NetBattleStarted.class);
		kryo.register(NetFanNumberUpdated.class);
		kryo.register(NetFirstPlayerSelected.class);
		kryo.register(NetFirstPlayerSelectionRequest.class);
		kryo.register(NetGameEnded.class);
		kryo.register(NetGameEndConditionsReached.class);
		kryo.register(NetHandlePlayerDisconnection.class);
		kryo.register(NetLockFactionRequest.class);
		kryo.register(NetMarketingLadderUpdated.class);
		kryo.register(NetMessage.class);
		kryo.register(NetNewStudio.class);
		kryo.register(NetPhaseChange.class);
		kryo.register(NetPlayingOrderChosen.class);
		kryo.register(NetSelectFactionRequest.class);
		kryo.register(NetSelectMascotToCapture.class);
		kryo.register(NetUnitEvent.class);
	}

	public static void registerAsLogicListener(GameServer.LogicListener logicListener) {

		EventDispatcher ed = EventDispatcher.getInstance();

		//logic.states.events
		ed.addListener(AskFirstPlayerEvent.class, logicListener);
		ed.addListener(AskMascotToCaptureEvent.class, logicListener);
		ed.addListener(BattleStartedEvent.class, logicListener);
		ed.addListener(FirstPlayerSelectedEvent.class, logicListener);
		ed.addListener(GameEndedEvent.class, logicListener);
		ed.addListener(PhaseChangedEvent.class, logicListener);

		//logic.event
		ed.addListener(GameEndConditionsReachedEvent.class, logicListener);
		ed.addListener(MarketingLadderUpdatedEvent.class, logicListener);
		ed.addListener(NumberOfFansChangedEvent.class, logicListener);
		ed.addListener(StudioAddedEvent.class, logicListener);
		ed.addListener(UnitEvent.class, logicListener);
	}

	public static void deregisterLogicListener(GameServer.LogicListener logicListener) {

		EventDispatcher ed = EventDispatcher.getInstance();

		//logic.states.events
		ed.removeListener(AskFirstPlayerEvent.class, logicListener);
		ed.removeListener(AskMascotToCaptureEvent.class, logicListener);
		ed.removeListener(BattleStartedEvent.class, logicListener);
		ed.removeListener(FirstPlayerSelectedEvent.class, logicListener);
		ed.removeListener(GameEndedEvent.class, logicListener);
		ed.removeListener(PhaseChangedEvent.class, logicListener);

		//logic.event
		ed.removeListener(GameEndConditionsReachedEvent.class, logicListener);
		ed.removeListener(MarketingLadderUpdatedEvent.class, logicListener);
		ed.removeListener(NumberOfFansChangedEvent.class, logicListener);
		ed.removeListener(StudioAddedEvent.class, logicListener);
		ed.removeListener(UnitEvent.class, logicListener);
	}
}
