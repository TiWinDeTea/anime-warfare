package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lucas on 23/11/2016.
 */
public class NetGameEnded implements Serializable {

    private final List<GameClientInfo> winners;

    public NetGameEnded() {
        this.winners = null;
    }

    public NetGameEnded(List<GameClientInfo> winners) {
        this.winners = winners;
    }

    public List<GameClientInfo> getWinners() {
        return Collections.unmodifiableList(this.winners);
    }
}
