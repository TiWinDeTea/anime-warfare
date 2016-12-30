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

package org.tiwindetea.animewarfare.gui.menu;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.menu.event.ServersListEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The servers list controller.
 *
 * @author Benoît CORTIER
 */
public class ServersListController {
	private class Room { // FIXME: temporary class for later...
		public String getName() {
			return "temp";
		}

		public boolean isLocked() {
			return false;
		}

		public List getMembers() {
			return new ArrayList();
		}
	}

	@FXML
	private TableColumn<Room, String> numberOfUsersList;

	@FXML
	private TableColumn<Room, Boolean> passwordsList;

	@FXML
	private TableColumn<Room, String> chatroomsNamesList;

	@FXML
	private TableView<Room> roomsTableView;

	private ObservableList<Room> roomsList = FXCollections.observableArrayList();

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		this.chatroomsNamesList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		this.passwordsList.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isLocked()));
		this.numberOfUsersList.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getMembers().size())));

		this.roomsTableView.setItems(this.roomsList);
	}

	@FXML
	private void handleRefresh(ActionEvent event) {
		handleRefresh();
	}

	@FXML
	private void handleConnect(ActionEvent event) {
		int selectedIndex = this.roomsTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			// TODO: try connection to the selected room.
			// which is roomsTableView.getItems().get(selectedIndex)
			EventDispatcher.getInstance().fire(new ServersListEvent(ServersListEvent.Type.JOIN));
		}
	}

	@FXML
	private void handleHost(ActionEvent event) {
		// TODO: start a server, connect the local client to it...
		EventDispatcher.getInstance().fire(new ServersListEvent(ServersListEvent.Type.HOST));
	}

	public void handleRefresh() {
		// TODO: look for LAN servers.
		List<Room> rooms = new ArrayList();
		this.roomsList.clear();
		this.roomsList.addAll(rooms);
	}

	@FXML
	private void handleQuit(ActionEvent event) {
		EventDispatcher.getInstance().fire(new ServersListEvent(ServersListEvent.Type.QUIT));
	}
}
