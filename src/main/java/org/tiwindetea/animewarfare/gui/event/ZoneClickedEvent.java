package org.tiwindetea.animewarfare.gui.event;

import javafx.scene.input.MouseEvent;
import org.lomadriel.lfc.event.Event;

/**
 * Created by maliafo on 06/01/17.
 */
public class ZoneClickedEvent implements Event<ZoneClickedEventListener> {

    private final int ZONE_ID;
    private final MouseEvent MOUSE_EVENT;

    public ZoneClickedEvent(int zoneID, MouseEvent mouseEvent) {
        this.ZONE_ID = zoneID;
        this.MOUSE_EVENT = mouseEvent;
    }

    @Override
    public void notify(ZoneClickedEventListener listener) {
        listener.handleClick(this);
    }

    public int getZoneID() {
        return this.ZONE_ID;
    }

    public MouseEvent getMouseEvent() {
        return this.MOUSE_EVENT;
    }
}
