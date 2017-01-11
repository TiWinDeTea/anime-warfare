package org.tiwindetea.animewarfare;

import org.junit.Test;
import org.tiwindetea.animewarfare.logic.FactionType;
import org.tiwindetea.animewarfare.net.GameClient;
import org.tiwindetea.animewarfare.net.GameServer;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetLockFactionRequest;
import org.tiwindetea.animewarfare.net.networkrequests.client.NetSelectFactionRequest;

import java.io.IOException;

/**
 * Created by maliafo on 11/01/17.
 */
public class runServer {

    @Test
    public void server1client() throws IOException, InterruptedException {
        GameServer server = new GameServer(2);
        server.bind(9512, 9513);
        server.start();
        GameClient client = new GameClient("Jean-bon");
        client.connect(server);
        Thread.sleep(1000);
        client.send(new NetSelectFactionRequest(FactionType.F_CLASS_NO_BAKA));
        client.send(new NetLockFactionRequest(FactionType.F_CLASS_NO_BAKA));
        for (; ; ) {
            Thread.sleep(10000);
        }
    }
}
