package org.tiwindetea.animewarfare.net;

import com.esotericsoftware.kryonet.Client;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Lucas on 20/11/2016.
 */
public class udpTest {
    Client t = new Client();

    @Test
    public void udpDiscoveryTest() throws IOException, InterruptedException {
        GameServer gs = new GameServer();
        gs.setGameName(new BigInteger(256, new Random()).toString(Character.MAX_RADIX));
        gs.bind(12345, 6845);
        gs.start();
        GameServer gs2 = new GameServer("素晴らしい　世界", "This is a password");
        gs2.bind(12355, 6847);
        gs2.start();
        GameClient gc = new GameClient();
        for (Room room : gc.discover(6845, 500)) {
            System.out.println("found room " + room.getGameName() + "\twith "
                    + room.getMembers().size() + " members.\tState: "
                    + (room.isLocked() ? "locked" : "open"));
        }
        Room r = null;
        for (Room room : gc.discover(6847, 500)) {
            r = room;
            System.out.println("found room " + room.getGameName() + "\twith "
                    + room.getMembers().size() + "members.\tState: "
                    + (room.isLocked() ? "locked" : "open"));
        }
        this.t.start();
        this.t.connect(500, r.getAddress(), r.getPort());
        this.t.stop();
        gs.stop();
        gs2.stop();
    }
}
