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

package org.tiwindetea.animewarfare.gui.game.gameboard.ItemFilters;

import javafx.scene.control.Button;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Benoît CORTIER
 * @since 0.1.0
 */
public abstract class AbstractFilter {
	public static Function<String, Button> buttonAdder;
	public static Consumer<Button> buttonRemover;

	protected enum GCAMState {
		NOTHING,
		MOVING_UNITS,
		BATTLE,
	}

	protected static GCAMState actionMenuState = GCAMState.NOTHING;

	public abstract String getName();

	public abstract void destroy();

	protected final Button addButton(String text) {
		return AbstractFilter.buttonAdder.apply(text);
	}

	protected final void remove(Button... buttons) {
		for (Button button : buttons) {
			AbstractFilter.buttonRemover.accept(button);
		}
	}
}
