package org.tiwindetea.net;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Lucas on 20/11/2016.
 */
public class udpTest {
    @Test
    public void udpDiscoveryTest() throws IOException, InterruptedException {
        GameServer gs = new GameServer();
        gs.setName(new BigInteger(256, new Random()).toString(Character.MAX_RADIX));
        gs.bind(12345, 6845);
        gs.start();
        GameServer gs2 = new GameServer("素晴らしい　世界", "This is a password");
        gs2.bind(22222, 6847);
        gs2.start();
        GameClient gc = new GameClient();
        for (Room room : gc.discover(6845, 500)) {
            System.out.println("found room " + room.getGameName() + "\twith " + room.getMembers().size() + " members.\tState: " + (room.isLocked() ? "locked" : "open"));
        }
        for (Room room : gc.discover(6847, 500)) {
            System.out.println("found room " + room.getGameName() + "\twith " + room.getMembers().size() + "members.\tState: " + (room.isLocked() ? "locked" : "open"));
        }
        gs.stop();
        gs2.stop();
    }
}
