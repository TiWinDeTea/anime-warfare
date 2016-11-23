package org.tiwindetea.animewarfare.net.networkrequests;

import java.io.Serializable;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayingOrderChosen implements Serializable {

    private final boolean isClockwise;

    public PlayingOrderChosen() {
        this.isClockwise = false;
    }

    public PlayingOrderChosen(Boolean clockWise) {
        this.isClockwise = clockWise.booleanValue();
    }

    public boolean isClockwise() {
        return this.isClockwise;
    }
}
