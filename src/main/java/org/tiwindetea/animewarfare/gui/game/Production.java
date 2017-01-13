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

package org.tiwindetea.animewarfare.gui.game;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.game.event.CapacityClickedEvent;
import org.tiwindetea.animewarfare.logic.capacity.CapacityName;

/**
 * @author Benoit Cortier
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class Production extends Rectangle {

	private final Tooltip tooltip = new Tooltip();

	public Production() {
		super(60., 60.);
		lock();
		Tooltip.install(this, this.tooltip);
	}

	public void unlock() {
		setFill(Color.RED);
	}

	public void lock() {
		setFill(Color.DARKRED);
	}

	public void setCapacity(CapacityName capacity) {
		setOnMouseClicked(e -> EventDispatcher.send(new CapacityClickedEvent(e, capacity, this)));
		this.tooltip.setText(capacity.toString());
	}
}
