package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.networkrequests.NetPlayingOrderChosen;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class PlayOrderChosenNetevent implements Event<PlayOrderChosenNeteventListener> {

    private final boolean isClockwise;

    public PlayOrderChosenNetevent(boolean isClockwise) {
        this.isClockwise = isClockwise;
    }

    public PlayOrderChosenNetevent(NetPlayingOrderChosen netPlayingOrderChosen) {
        this.isClockwise = netPlayingOrderChosen.isClockwise();
    }

    @Override
    public void notify(PlayOrderChosenNeteventListener listener) {
        listener.handlePlayOrderChoice(this);
    }

    public boolean isClockwise() {
        return this.isClockwise;
    }
}
