package org.tiwindetea.animewarfare.net.networkevent;

import org.lomadriel.lfc.event.Event;
import org.tiwindetea.animewarfare.net.GameClientInfo;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class FirstPlayerSelectedNetevent implements Event<FirstPlayerSelectedNeteventListener> {

    private final GameClientInfo gameClientInfo;

    public FirstPlayerSelectedNetevent() {
        this.gameClientInfo = null;
    }

    public FirstPlayerSelectedNetevent(GameClientInfo gameClientInfo) {
        this.gameClientInfo = gameClientInfo;
    }

    @Override
    public void notify(FirstPlayerSelectedNeteventListener listener) {
        listener.handlePlayerSelection(this);
    }

    public GameClientInfo getGameClientInfo() {
        return this.gameClientInfo;
    }
}
