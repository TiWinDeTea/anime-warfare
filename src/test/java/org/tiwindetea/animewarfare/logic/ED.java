package org.tiwindetea.animewarfare.logic;

import org.junit.Test;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.net.GameClientInfo;
import org.tiwindetea.animewarfare.net.networkevent.PlayerLockedFactionNetevent;

/**
 * Created by maliafo on 05/01/17.
 */
public class ED {

    @Test
    public void test() {
        EventDispatcher ed = LogicEventDispatcher.getInstance();
        EventDispatcher.registerListener(PlayerLockedFactionNetevent.class, e -> System.out.println("EventDispatcher"));
        ed.addListener(PlayerLockedFactionNetevent.class, e -> System.out.println("LogicEventDispatcher"));
        ed.fire(new PlayerLockedFactionNetevent(new GameClientInfo("auie", 0), FactionType.NO_NAME));
    }
}
