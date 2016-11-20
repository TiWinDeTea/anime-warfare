package org.tiwindetea.net.animewarfare;

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
        org.tiwindetea.net.animewarfare.GameServer gs = new org.tiwindetea.net.animewarfare.GameServer();
        gs.setName(new BigInteger(256, new Random()).toString(Character.MAX_RADIX));
        gs.bind(12345, 6845);
        gs.start();
        org.tiwindetea.net.animewarfare.GameServer gs2 = new org.tiwindetea.net.animewarfare.GameServer("素晴らしい　世界", "This is a password");
        gs2.bind(12355, 6847);
        gs2.start();
        org.tiwindetea.net.animewarfare.GameClient gc = new org.tiwindetea.net.animewarfare.GameClient();
        for (org.tiwindetea.net.animewarfare.Room room : gc.discover(6845, 500)) {
            System.out.println("found room " + room.getGameName() + "\twith " + room.getMembers().size() + " members.\tState: " + (room.isLocked() ? "locked" : "open"));
        }
        org.tiwindetea.net.animewarfare.Room r = null;
        for (org.tiwindetea.net.animewarfare.Room room : gc.discover(6847, 500)) {
            r = room;
            System.out.println("found room " + room.getGameName() + "\twith " + room.getMembers().size() + "members.\tState: " + (room.isLocked() ? "locked" : "open"));
        }
        this.t.start();
        this.t.connect(500, r.getAddress(), r.getPort());
        this.t.stop();
        gs.stop();
        gs2.stop();
    }
}
