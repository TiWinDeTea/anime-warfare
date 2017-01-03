package org.tiwindetea.animewarfare.net;

import org.junit.Test;

import java.net.SocketException;

/**
 * Created by maliafo on 03/01/17.
 */
public class ScanTest {

    @Test
    public void test() {

        ServerScanner ss = new ServerScanner();
        ss.setOnDiscovery(room -> System.out.println("Room found: " + room));
        ss.setOnDisappear(room -> System.out.println("Room disapeared: " + room));
        try {
            ss.parallelDiscovery(9513);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        for (; ; ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
