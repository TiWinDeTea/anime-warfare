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

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GUnit extends Parent {

    private final int ID;

    public GUnit() {
        //todo
        this.ID = 0;
        Random r = new Random();
        getChildren().add(new Circle(15, Color.rgb(r.nextInt(256), r.nextInt(256), r.nextInt(256))));
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public int hashCode() {
        return this.ID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GUnit && this.equals((GUnit) o);
    }

    public boolean equals(GUnit u) {
        if (u != null) {
            return this.ID == u.ID;
        }
        return false;
    }
}
