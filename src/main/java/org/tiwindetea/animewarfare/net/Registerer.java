package org.tiwindetea.animewarfare.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import org.tiwindetea.animewarfare.net.networkevent.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
class Registerer {
    static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(GameClientInfo.class);
        kryo.register(Room.class);
        kryo.register(String.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(MessageReceivedEvent.class);
        //Todo
    }
}
