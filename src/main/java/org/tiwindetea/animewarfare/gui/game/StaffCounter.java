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

import javafx.scene.control.Label;

/**
 * Created by benoit on 02/01/17.
 */
public class StaffCounter extends Label {
	public StaffCounter() {
		super("0");
		setStyle("-fx-background-color: black;" +
				"-fx-padding: 20px 15px 20px 15px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: white;"); // TODO: externalize
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		getChildren().clear();
	}

	public void setValue(int value) {
		setText(String.valueOf(value));
	}

	public int getValue() {
		return Integer.valueOf(getText());
	}

	public void increment(int value) {
		setText(String.valueOf(Integer.valueOf(getText()) + value));
	}

	public void decrement(int value) {
		setText(String.valueOf(Integer.valueOf(getText()) - value));
	}
}
