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

package org.tiwindetea.animewarfare.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.util.ResourceBundleHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The global chat is contained here.
 *
 * @author Benoît CORTIER
 */
public class GlobalChat {
	private static AnchorPane chat;
	private static ChatController chatController;

	private static final Map<GameClientInfo, Color> colorsByClient = new HashMap<>();
	private static final Map<GameClientInfo, FactionType> factionsByClient = new HashMap<>();
	private static final Map<FactionType, GameClientInfo> clientsByFactions = new HashMap<>();

	private static final Color DEFAULT_COLOR = Color.BLACK;

	static {
		FXMLLoader chatLoader = new FXMLLoader();
		chatLoader.setLocation(ChatController.class.getResource("Chat.fxml"));
		chatLoader.setResources(ResourceBundleHelper.getBundle("org.tiwindetea.animewarfare.gui.ChatController"));

		try {
			GlobalChat.chat = chatLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GlobalChat.chatController = chatLoader.getController();
	}

	public static AnchorPane getChat() {
		return GlobalChat.chat;
	}

	public static ChatController getChatController() {
		return GlobalChat.chatController;
	}

	public static void registerClientColor(GameClientInfo gameClientInfo, Color color) {
		GlobalChat.colorsByClient.put(gameClientInfo, color);
	}

	public static void unregisterClientColor(GameClientInfo gameClientInfo) {
		GlobalChat.colorsByClient.remove(gameClientInfo);
	}

	public static Color getClientColor(GameClientInfo gameClientInfo) {
		return GlobalChat.colorsByClient.getOrDefault(gameClientInfo, GlobalChat.DEFAULT_COLOR);
	}

	public static void registerClientFaction(GameClientInfo gameClientInfo, FactionType factionType) {
		GlobalChat.factionsByClient.put(gameClientInfo, factionType);
		GlobalChat.clientsByFactions.put(factionType, gameClientInfo);
	}

	public static void unregisterClientFaction(GameClientInfo gameClientInfo) {
		GlobalChat.factionsByClient.remove(gameClientInfo);
		GlobalChat.clientsByFactions.entrySet().stream()
				.filter(entrySet -> entrySet.getValue() == gameClientInfo)
				.forEach(entrySet -> GlobalChat.clientsByFactions.remove(entrySet.getKey()));
	}

	public static FactionType getClientFaction(GameClientInfo gameClientInfo) {
		return GlobalChat.factionsByClient.getOrDefault(gameClientInfo, null);
	}

	public static GameClientInfo getFactionClient(FactionType factionType) {
		return GlobalChat.clientsByFactions.get(factionType);
	}

	public static Color getDefaultColor() {
		return DEFAULT_COLOR;
	}
}
