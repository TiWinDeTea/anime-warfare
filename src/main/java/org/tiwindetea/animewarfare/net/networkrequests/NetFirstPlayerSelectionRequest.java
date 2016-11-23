package org.tiwindetea.animewarfare.net.networkrequests;

import org.tiwindetea.animewarfare.net.GameClientInfo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lucas on 23/11/2016.
 */
public class NetFirstPlayerSelectionRequest implements Serializable {

    private final GameClientInfo selector;
    private final List<GameClientInfo> selectables;

    public NetFirstPlayerSelectionRequest() {
        this.selector = null;
        this.selectables = null;
    }

    public NetFirstPlayerSelectionRequest(GameClientInfo selector, List<GameClientInfo> selectable) {
        this.selector = selector;
        this.selectables = selectable;
    }

    public GameClientInfo getSelector() {
        return this.selector;
    }

    public List<GameClientInfo> getSelectables() {
        return Collections.unmodifiableList(this.selectables);
    }
}
