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
import org.tiwindetea.animewarfare.gui.menu.GameRoomState;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The global chat is contained here.
 *
 * @author Benoît CORTIER
 */
public class GlobalChat {
	private static AnchorPane chat;
	private static ChatController chatController;

	static {
		FXMLLoader chatLoader = new FXMLLoader();
		chatLoader.setLocation(ChatController.class.getResource("Chat.fxml"));
		chatLoader.setResources(ResourceBundle.getBundle("org.tiwindetea.animewarfare.gui.ChatController",
				Locale.getDefault()));
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
}
