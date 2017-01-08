////////////////////////////////////////////////////////////
//
// Anime Warfare
// Copyright (C) 2016 TiWinDeTea - contact@tiwindetea.org
//
// This software is provided 'as-is', without any express or implied warranty.
// In no event will the authors be held liable for any damages arising from the use of this software.
//
// Permission is granted to anyone to use this software for any purpose,
// including commercial applications, and to alter it and redistribute it freely,
// subject to the following restrictions:
//
// 1. The origin of this software must not be misrepresented;
//    you must not claim that you wrote the original software.
//    If you use this software in a product, an acknowledgment
//    in the product documentation would be appreciated but is not required.
//
// 2. Altered source versions must be plainly marked as such,
//    and must not be misrepresented as being the original software.
//
// 3. This notice may not be removed or altered from any source distribution.
//
////////////////////////////////////////////////////////////

package org.tiwindetea.animewarfare.logic;

import org.lomadriel.lfc.event.Event;
import org.lomadriel.lfc.event.EventDispatcher;

import java.util.EventListener;

/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class LogicEventDispatcher extends EventDispatcher {

    private static class LEDHolder {
        static final LogicEventDispatcher INSTANCE = new LogicEventDispatcher();
    }

    LogicEventDispatcher() {
        super();
    }

    public static LogicEventDispatcher getInstance() {
        return LEDHolder.INSTANCE;
    }

    /**
     * Notifies each listener which listen this event.
     *
     * @param event event to fire
     * @param <T>   listener's class
     */
    public static <T extends EventListener> void send(Event<T> event) {
        getInstance().fire(event);
    }

    /**
     * Removes a listener for the given event.
     *
     * @param eventClass event's class
     * @param listener   listener to remove from the notification cycle.
     * @param <T>        listener's class, a listener must implement EventListener.
     * @return true if the event dispatcher contains the listener.
     */
    public static <T extends EventListener> boolean unregisterListener(Class<? extends Event<T>> eventClass,
                                                                       T listener) {
        return getInstance().removeListener(eventClass, listener);
    }

    /**
     * Adds a listener for the given event.
     *
     * @param eventClass event's class
     * @param listener   listener to notify when the event is fired.
     * @param <T>        listener's class, a listener must implement EventListener.
     * @return true if the listener have been added, false otherwise.
     */
    public static <T extends EventListener> boolean registerListener(Class<? extends Event<T>> eventClass, T listener) {
        return getInstance().addListener(eventClass, listener);
    }

}
