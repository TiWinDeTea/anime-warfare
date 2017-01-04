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

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.MainApp;
import org.tiwindetea.animewarfare.gui.ConnectIpDialog;
import org.tiwindetea.animewarfare.gui.HostServerDialog;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEvent;
import org.tiwindetea.animewarfare.gui.event.QuitApplicationEventListener;
import org.tiwindetea.animewarfare.gui.menu.event.GameRoomEvent;
import org.tiwindetea.animewarfare.gui.menu.event.ServersListEvent;
import org.tiwindetea.animewarfare.net.Room;
import org.tiwindetea.animewarfare.net.ServerScanner;
import org.tiwindetea.animewarfare.net.networkevent.BadPasswordNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BadPasswordNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.ConnectedNeteventListener;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetPassword;
import org.tiwindetea.animewarfare.settings.Settings;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The servers list controller.
 *
 * @author Benoît CORTIER
 */
public class ServersListController implements Initializable, ConnectedNeteventListener,
		BadPasswordNeteventListener, QuitApplicationEventListener {
	private ResourceBundle resourceBundle;

	private static final int DEFAULT_UDP_PORT = 9513;

	@FXML
	private TableColumn<Room, String> numberOfUsersList;

	@FXML
	private TableColumn<Room, Boolean> passwordsList;

	@FXML
	private TableColumn<Room, String> IPAddressList;

	@FXML
	private TableColumn<Room, String> chatroomsNamesList;

	@FXML
	private TableView<Room> roomsTableView;

	private final ObservableList<Room> roomsList = FXCollections.observableArrayList();

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	private final ServerScanner serverScanner = new ServerScanner();

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;

		this.chatroomsNamesList.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGameName()));
		this.numberOfUsersList.setCellValueFactory(cellData ->
				new SimpleStringProperty(String.valueOf(cellData.getValue().getMembers().size())
						+ "/" + String.valueOf(cellData.getValue().getNumberOfExpectedPlayers())));
		this.IPAddressList.setCellValueFactory(cellData ->
				new SimpleStringProperty(cellData.getValue().getAddress().toString().substring(1) + ":" + cellData.getValue().getPort()));
		this.passwordsList.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isLocked()));
		this.passwordsList.setCellFactory(tc -> new CheckBoxTableCell<>());
		this.roomsTableView.setItems(this.roomsList);


		EventDispatcher.getInstance().addListener(ConnectedNetevent.class, this);
		EventDispatcher.getInstance().addListener(BadPasswordNetevent.class, this);
		EventDispatcher.getInstance().addListener(QuitApplicationEvent.class, this);

		this.serverScanner.setOnDiscovery(this::handleNewServerDiscovered);
		this.serverScanner.setOnDisappear(this::handleServerDisappeared);
		this.serverScanner.setOnUpdate(this::handleServerRoomUpdated);
	}

	public void startDiscovery() {
		if (this.serverScanner.isRunning()) {
			this.serverScanner.resume();
		} else {
			try {
				this.serverScanner.parallelDiscovery(DEFAULT_UDP_PORT);
			} catch (SocketException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText(e.getLocalizedMessage());
				alert.showAndWait();
			}
		}
	}

	public void stopDiscovery() {
		this.roomsList.clear();
		this.serverScanner.pause();
	}

	@FXML
	private void handleConnect() {
		int selectedIndex = this.roomsTableView.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			String password = null;
			if (this.roomsTableView.getItems().get(selectedIndex).isLocked()) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle(this.resourceBundle.getString("passworddialog.title"));
				dialog.setHeaderText(this.resourceBundle.getString("passworddialog.header"));
				dialog.setContentText(this.resourceBundle.getString("passworddialog.content"));

				Optional<String> result = dialog.showAndWait();
				if (!result.isPresent()) {
					return;
				}
				password = result.get();
			}

			try {
				MainApp.getGameClient().setName(Settings.getPlayerName());
				MainApp.getGameClient().connect(this.roomsTableView.getItems().get(selectedIndex), new NetPassword(password));
			} catch (IOException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		}
	}

	@FXML
	private void handleConnectIp() {
		ConnectIpDialog connectIpDialog = new ConnectIpDialog(null);
		this.roomsTableView.getScene().getRoot().setDisable(true);
		connectIpDialog.showAndWait();
		this.roomsTableView.getScene().getRoot().setDisable(false);
		if (connectIpDialog.isCanceled()) {
			return;
		}

		MainApp.getGameClient().setName(Settings.getPlayerName());
		try {
			MainApp.getGameClient().connectAt(
					new InetSocketAddress(connectIpDialog.getServerIp(), connectIpDialog.getTcpPort()),
					new NetPassword(connectIpDialog.getPassword())
			);
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(e.getLocalizedMessage());
			alert.showAndWait();
		}
	}

	@FXML
	private void handleHost() {
		try {
			HostServerDialog hostServerDialog = new HostServerDialog(null);
			this.roomsTableView.getScene().getRoot().setDisable(true);
			hostServerDialog.showAndWait();
			this.roomsTableView.getScene().getRoot().setDisable(false);
			if (hostServerDialog.isCanceled()) {
				return;
			}

			MainApp.getGameServer().setNumberOfExpectedPlayer(hostServerDialog.getMaxPlayers());
			MainApp.getGameServer().setGameName(hostServerDialog.getServerName());
			MainApp.getGameServer().setGamePassword(hostServerDialog.getPassword());
			MainApp.getGameServer().bind(hostServerDialog.getTcpPort(), DEFAULT_UDP_PORT);
			MainApp.getGameServer().start();
			MainApp.getGameClient().setName(Settings.getPlayerName());
			MainApp.getGameClient().connect(MainApp.getGameServer());
		} catch (IOException | IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(e.getLocalizedMessage());
			alert.showAndWait();
		}
	}

	@FXML
	private void handleRefresh() {
		this.serverScanner.forceUpdate();
	}

	private void handleNewServerDiscovered(Room room) {
		this.roomsList.add(room);
	}

	private void handleServerDisappeared(Room room) {
		this.roomsList.remove(room);
	}

	private void handleServerRoomUpdated(Room room) {
		this.roomsList.remove(room);
		this.roomsList.add(room);
	}

	@FXML
	private void handleQuit() {
		EventDispatcher.getInstance().fire(new ServersListEvent(ServersListEvent.Type.QUIT));
	}

	@Override
	public void handleBadPassword() {
		Platform.runLater(() -> {
			EventDispatcher.getInstance().fire(new GameRoomEvent(GameRoomEvent.Type.QUIT));
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText(this.resourceBundle.getString("alert.badpassword"));
			alert.showAndWait();
		});
	}

	@Override
	public void handleSelfConnection(ConnectedNetevent event) {
		Platform.runLater(() -> EventDispatcher.getInstance().fire(new ServersListEvent(ServersListEvent.Type.JOIN)));
	}

	@Override
	public void handleQuitApplication() {
		this.serverScanner.shutdown();
	}
}
