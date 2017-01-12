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

package org.tiwindetea.animewarfare.gui.game.gameboard;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.GlobalChat;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;
import org.tiwindetea.animewarfare.logic.GameMap;
import org.tiwindetea.animewarfare.logic.events.StudioEvent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNetevent;
import org.tiwindetea.animewarfare.net.networkevent.BattleNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.GameEndedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.StudioControllerChangedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioControllerChangedNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.StudioNetevent;
import org.tiwindetea.animewarfare.net.networkevent.StudioNeteventListener;
import org.tiwindetea.animewarfare.net.networkevent.UnitMovedNetevent;
import org.tiwindetea.animewarfare.net.networkevent.UnitMovedNeteventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Lucas Lazare
 * @since 0.1.0
 */
public class GMap extends Pane implements UnitMovedNeteventListener, StudioNeteventListener,
        BattleNeteventListener, StudioControllerChangedNeteventListener, GameEndedNeteventListener {

    // TODO: rework this class

    private final ImageView MAP_PICT;
    private final Group POLYGONES = new Group();
    private final ZoneComponentsMap MAP = new ZoneComponentsMap();
    private final List<Pair<Polygon, Tooltip>> ZONES = new ArrayList<>(17);
    private final Map<Integer, Line> LINKS = new HashMap<>();
    private final Map<Integer, Line> STUDIOLINKS = new HashMap<>();
    private boolean isDisplayingZonesGrids = false;
    private boolean isDisplayinGComponentsGrids = false;

    private static int id = 0;

    public GMap() {
        super();
        this.MAP_PICT = new ImageView(new Image(GMap.class.getResourceAsStream("pictures/map.png")));
        initZones();
        getChildren().addAll(this.MAP_PICT, this.POLYGONES, this.MAP);
        this.MAP_PICT.autosize();
        this.POLYGONES.autosize();
        this.MAP.autosize();

        this.setOnScroll(e -> {
            setScaleX(getScaleX() + getScaleX() * e.getDeltaY() / 1000.0); // TODO externalize & settings
            setScaleY(getScaleY() + getScaleY() * e.getDeltaY() / 1000.0); // TODO externalize & settings
        });

        EventDispatcher.registerListener(UnitMovedNetevent.class, this);
        EventDispatcher.registerListener(StudioNetevent.class, this);
        EventDispatcher.registerListener(BattleNetevent.class, this);
        EventDispatcher.registerListener(StudioControllerChangedNetevent.class, this);
        EventDispatcher.registerListener(GameEndedNetevent.class, this);
    }

    public void clear() {
        EventDispatcher.unregisterListener(UnitMovedNetevent.class, this);
        EventDispatcher.unregisterListener(StudioNetevent.class, this);
        EventDispatcher.unregisterListener(BattleNetevent.class, this);
        EventDispatcher.unregisterListener(StudioControllerChangedNetevent.class, this);
        EventDispatcher.unregisterListener(GameEndedNetevent.class, this);
        this.POLYGONES.getChildren().clear();
        this.MAP.clear();
        this.ZONES.clear();
        this.LINKS.clear();
        this.STUDIOLINKS.clear();
        getChildren().clear();
    }

    /**
     * Adds a GComponent to the map, using Javafx's thread.
     */
    public void addGComponentFxThread(GComponent gComponent, int zoneID) {
        Platform.runLater(() -> addGComponent(gComponent, zoneID));
    }

    /**
     * Adds a GComponent to the map.
     */
    public void addGComponent(GComponent gComponent, int zoneID) {
        this.MAP.put(gComponent, zoneID);
        this.ZONES.get(zoneID).getValue().setText(getDescription(zoneID));
        autosize();
    }

    /**
     * Removes a GComponent from the map, using Javafx's thread.
     */
    public void removeGComponentFxThread(GComponent gComponent, int zoneID) {
        Platform.runLater(() -> removeGComponent(gComponent, zoneID));
    }

    /**
     * Removes a GComponent from the map.
     */
    public void removeGComponent(GComponent gComponent, int zoneID) {
        this.MAP.remove(gComponent, zoneID);
        this.ZONES.get(zoneID).getValue().setText(getDescription(zoneID));
        autosize();
    }

    /**
     * Moves a GComponent from a zone to another, using JavaFX's thread.
     */
    public void moveGComponentFxThread(GComponent gComponent, int source, int destination) {
        Platform.runLater(() -> moveGComponent(gComponent, source, destination));
    }

    /**
     * Moves a GComponent from a zone to another
     */
    public void moveGComponent(GComponent gComponent, int source, int destination) {
        this.MAP.move(gComponent, source, destination);
        autosize();
    }

    /**
     * Removes any highlightig of a given zone, unsing JavaFX's thread
     */
    public void unHighlightFxThread(int zoneID) {
        Platform.runLater(() -> unHighlight(zoneID));
    }

    /**
     * Removes any highlightig of a given zone
     */
    public void unHighlight(int zoneID) {
        Polygon p = this.ZONES.get(zoneID).getKey();
        p.setEffect(null);
        setStyle(p);
    }

    /**
     * Highlights a given zone in a given color, using JavaFX's thread
     */
    public void highLightFxThread(int zone, Color defaultColor, Color selectColor) {
        Platform.runLater(() -> highlight(zone, defaultColor, selectColor));
    }

    /**
     * Highlights a given zone in a given color
     */
    public void highlight(int zone, Color defaultColor, Color selectColor) {
        Polygon poly = this.ZONES.get(zone).getKey();
        poly.setOnMouseEntered(e -> poly.setFill(selectColor));
        poly.setOnMouseExited(e -> poly.setFill(defaultColor));
        poly.setOpacity(1);
        poly.setFill(defaultColor);
    }

    /**
     * Highlights any zone at at most {@code distance} distance from a given zone, appart from the zone itself
     */
    public void highlightNeighbour(int zoneID, int distance, Color defaultColor, Color selectColor) {
        for (Integer integer : GameMap.getZonesAtAtMostExcept(zoneID, distance)) {
            highlight(integer.intValue(), defaultColor, selectColor);
        }
    }

    /**
     * Remove the highlighting of any zone within {@code distance} zones of a given zone
     */
    public void unHighlightNeigbour(int zoneID, int distance) {
        for (Integer integer : GameMap.getZonesAtAtMostExcept(zoneID, distance)) {
            unHighlight(integer.intValue());
        }
    }

    /**
     * Displays or Hides GComponents squares (debug)
     */
    public void displayComponentsGrids(boolean b) {
        if (b) {
            if (!this.isDisplayinGComponentsGrids) {
                this.isDisplayinGComponentsGrids = true;
                this.MAP.getRectangles().stream().forEach(rectangle -> rectangle.showGrid());
            }
        } else {
            if (this.isDisplayinGComponentsGrids) {
                this.isDisplayinGComponentsGrids = false;
                this.MAP.getRectangles().stream().forEach(rectangle -> rectangle.hideGrid());
            }
        }
    }

    /**
     * Displays or Hides zones borders
     */
    public void displayZonesGrids(boolean b) {
        if (b) {
            if (!this.isDisplayingZonesGrids) {
                this.isDisplayingZonesGrids = true;
                this.ZONES.stream().map(Pair::getKey).forEach(this::setStyle);
            }
        } else {
            if (this.isDisplayingZonesGrids) {
                this.isDisplayingZonesGrids = false;
                this.ZONES.stream().map(Pair::getKey).forEach(this::setStyle);
            }
        }
    }

    public void switchComponentsGridsDisplay() {
        this.displayComponentsGrids(!this.isDisplayinGComponentsGrids);
    }

    public void switchZonesGridsDisplay() {
        this.displayZonesGrids(!this.isDisplayingZonesGrids);
    }

    /**
     * Used to disable the ScrollPane wheel scroll without removing the zooming ability
     */
    public void scrollEvent(ScrollEvent e) {
        this.getOnScroll().handle(e);
    }

    public List<GComponent> getComponents(int zoneId) {
        return this.MAP.getRectanglesOf(zoneId).stream()
                .map(r -> r.getGComponent())
                .filter(c -> c != null)
                .collect(Collectors.toList());
    }

    public List<GUnit> getUnits(int zonedID) {
        return this.MAP.getRectanglesOf(zonedID)
                       .stream()
                       .map(r -> r.getGComponent())
                       .filter(c -> c != null && c.isUnit()).map(c -> (GUnit) c)
                       .collect(Collectors.toList());
    }

    /**
     * Creates a line from the component to the given coordinates
     *
     * @return an ID for the link, to be used with "removeLinks"
     */
    public int linkTo(GComponent component, double x, double y) {

        Coordinates c = centerOf(component);

        Line line = new Line(
                c.x,
                c.y,
                x,
                y
        );
        line.setStrokeWidth(2.);
        line.setFill(Color.BLACK);
        line.getStrokeDashArray().addAll(4.);
        line.setMouseTransparent(true);
        getChildren().add(line);
        line.toFront();
        this.LINKS.put(new Integer(id), line);
        return id++;
    }

    public void removeLinks() {
        getChildren().removeAll(this.LINKS.values());
        this.LINKS.clear();
    }

    public void removeLink(Integer id) {
        getChildren().remove(this.LINKS.remove(id));
    }

    private class GComponentRectangle extends Parent {

        final double x, y;
        static final int WIDTH = 42;
        static final int HEIGHT = 42;
        final Polygon grid;

        private GComponent gComponent;

        public GComponentRectangle(double x, double y) {
            this.x = x;
            this.y = y;

            this.grid = new Polygon(x, y, x, y + WIDTH, x + WIDTH, y + WIDTH, x + WIDTH, y);
            this.grid.setFill(Color.TRANSPARENT);
            this.grid.setStroke(Color.YELLOW);
            this.grid.setMouseTransparent(true);
        }

        public void setGComponent(GComponent gComponent) {
            if (this.gComponent != null) {
                this.getChildren().remove(this.gComponent);
            }
            if (gComponent != null) {
                gComponent.setTranslateX(this.x + WIDTH / 2);
                gComponent.setTranslateY(this.y + HEIGHT / 2);
                this.getChildren().add(gComponent);
            }
            this.gComponent = gComponent;
        }

        public GComponent getGComponent() {
            return this.gComponent;
        }

        public void showGrid() {
            getChildren().add(this.grid);
        }

        public void hideGrid() {
            getChildren().remove(this.grid);
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof GComponentRectangle) && this.equals((GComponentRectangle) o);
        }

        public boolean equals(GComponentRectangle r) {
            return r.x == this.x && r.y == this.y;
        }

        @Override
        public int hashCode() {
            return (int) (this.x + this.y);
        }

        public void clear() {
            getChildren().clear();
            this.gComponent.getChildren().clear();
            this.gComponent = null;
        }
    }

    private class ZoneComponentsMap extends Parent {

        private class Int {
            int value = 0;
        }

        private final Map<Integer, List<GComponentRectangle>> map = new HashMap<>();
        private final Map<Integer, List<GComponent>> orphans = new HashMap<>();

        private final List<Int> numberOfgComponentsPerZone = new ArrayList<>(17);

        public ZoneComponentsMap() {
            for (int i = 0; i < 17; i++) {
                this.numberOfgComponentsPerZone.add(new Int());
            }
        }

        void setZones(List<Polygon> polygons) {

            if (this.map.size() != 0) {
                throw new IllegalStateException();
            }

            int i = 0;

            for (Polygon polygon : polygons) {
                List<GComponentRectangle> rectList = new ArrayList<>(20);
                Bounds bounds = polygon.getLayoutBounds();

                int xdirection = 1;
                boolean pouredOne = false;

                for (double y = bounds.getMinY() + 3; y < bounds.getMaxY(); y += GComponentRectangle.HEIGHT) {
                    double x = xdirection > 0 ? bounds.getMinX() + 3 : bounds.getMaxX() - 3;
                    while (x > bounds.getMinX() && x < bounds.getMaxX()) {

                        do {
                            if (polygon.contains(x - 2, y - 2)
                                    && polygon.contains(x + GComponentRectangle.WIDTH + 2, y - 2)
                                    && polygon.contains(x - 2, y + GComponentRectangle.HEIGHT + 2)
                                    && polygon.contains(x + GComponentRectangle.WIDTH + 2, y + GComponentRectangle.HEIGHT + 2)
                                    ) {
                                rectList.add(new GComponentRectangle(x, y));
                                pouredOne = true;
                            }
                            x += xdirection;
                        } while (!pouredOne && x < bounds.getMaxX() && x > bounds.getMinX());
                        x += (GComponentRectangle.WIDTH - 1) * xdirection;
                    }
                    pouredOne = false;
                    xdirection = -xdirection;
                }
                getChildren().addAll(rectList);

                this.map.put(new Integer(i), rectList);
                ++i;
            }
        }

        public GComponentRectangle put(GComponent gComponent, int zoneID) {
            GComponentRectangle gcomponentRectangle = this.map.get(new Integer(zoneID)).parallelStream()
                    .filter(ur -> ur.gComponent == null)
                    .findAny().orElse(null);
            if (gcomponentRectangle != null) {
                gcomponentRectangle.setGComponent(gComponent);
            } else {
                List<GComponent> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    list.add(gComponent);
                } else {
                    list = new ArrayList<>(5);
                    list.add(gComponent);
                    this.orphans.put(new Integer(zoneID), list);
                }
            }
            this.numberOfgComponentsPerZone.get(zoneID).value++;
            updateDescription(zoneID);

            gComponent.setZone(zoneID);
            return gcomponentRectangle;
        }

        public GComponentRectangle remove(GComponent gComponent, int zoneID) {
            GComponentRectangle gcomponentRectangle = this.map.get(new Integer(zoneID)).parallelStream()
                    .filter(ur -> gComponent.equals(ur.getGComponent()))
                    .findAny()
                    .orElse(null);
            if (gcomponentRectangle != null) {
                GComponent newgComponent = null;
                List<GComponent> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    if (list.size() != 0) {
                        newgComponent = list.remove(0);
                    }
                }
                gcomponentRectangle.setGComponent(newgComponent);
                --this.numberOfgComponentsPerZone.get(zoneID).value;
            } else {
                List<GComponent> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    if (list.remove(gComponent)) {
                        --this.numberOfgComponentsPerZone.get(zoneID).value;
                    }
                }
            }
            updateDescription(zoneID);

            gComponent.setZone(-1);
            return gcomponentRectangle;
        }

        public void move(GComponent gComponent, int source, int destination) {
            remove(gComponent, source);
            put(gComponent, destination);
        }

        public int getNumberOfComponents(int zoneID) {
            return this.numberOfgComponentsPerZone.get(zoneID).value;
        }

        public List<GComponentRectangle> getRectanglesOf(int zoneId) {
            return this.map.get(new Integer(zoneId));
        }

        public List<GComponentRectangle> getRectangles() {
            List<GComponentRectangle> ans = new ArrayList<>(200);
            for (List<GComponentRectangle> gcomponentRectangles : this.map.values()) {
                ans.addAll(gcomponentRectangles);
            }
            return ans;
        }

        public GComponentRectangle getRectangleOf(GComponent component) {
            return this.map.get(new Integer(component.getZone())).stream().filter(r -> component.equals(r.getGComponent())).findAny().orElse(null);
        }

        public void clear() {
            this.map.values().forEach(r -> r.clear());
            this.map.clear();
            this.orphans.clear();
            getChildren().clear();
        }
    }

    private static class Coordinates {
        public double x, y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Coordinates() {

        }
    }

    private String getDescription(int zoneID) {
        return "Number of things in zone " + zoneID + ": " + this.MAP.getNumberOfComponents(zoneID);
    }

    private void setStyle(Polygon polygon) {

        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(3);
        polygon.setPickOnBounds(false);

        if (this.isDisplayingZonesGrids) {
            polygon.setFill(Color.TRANSPARENT);
            polygon.setOpacity(1);
            polygon.setOnMouseEntered(e -> polygon.setFill(Color.rgb(255, 255, 255, 0.3)));
            polygon.setOnMouseExited(e -> polygon.setFill(Color.TRANSPARENT));
        } else {
            polygon.setFill(Color.WHITE);
            polygon.setOpacity(0);
            polygon.setOnMouseEntered(e -> polygon.setOpacity(0.3));
            polygon.setOnMouseExited(e -> polygon.setOpacity(0));
        }
    }

    private void updateDescription(int zoneID) {
        this.ZONES.get(zoneID).getValue().setText(getDescription(zoneID));
    }

    private void initZones() {

        List<Polygon> polygons = createPolyZone();
        for (int i = 0; i < polygons.size(); i++) {
            int magic_i = i;
            Polygon polygon = polygons.get(i);
            setStyle(polygon);
            polygon.setOnMouseClicked(e -> EventDispatcher.send(new ZoneClickedEvent(magic_i, e)));
            Tooltip tooltip = new Tooltip(getDescription(i));
            Tooltip.install(polygon, tooltip);
            this.ZONES.add(new Pair(polygon, tooltip));
        }
        this.POLYGONES.getChildren().addAll(polygons);
        this.MAP.setZones(polygons);
    }

    private static List<Polygon> createPolyZone() {

        List<Polygon> polygons = new ArrayList<>(17);

        polygons.add(new Polygon(               // 0
                949.0, 0,
                1366.0, 0,
                1366.0, 220.0,
                1299.0, 194.0,
                1237.0, 125.0,
                1243.0, 115.0,
                1147.0, 65.0,
                993.0, 29.0,
                950.0, 3.0

        ));
        polygons.add(new Polygon(               // 1
                0, 0,
                608.0, 0,
                411.0, 126.0,
                422.0, 146.0,
                401.0, 142.0,
                330.0, 142.0,
                286.0, 171.0,
                265.0, 244.0,
                254.0, 216.0,
                225.0, 177.0,
                172.0, 166.0,
                99.0, 169.0,
                35.0, 181.0,
                0, 218.0,
                0, 0
        ));

        polygons.add(new Polygon(               // 2
                0.0, 218.0,
                35.0, 181.0,
                100.0, 166.0,
                173.0, 164.0,
                225.0, 180.0,
                253.0, 216.0,
                266.0, 240.0,
                265, 250,
                268.0, 344.0,
                280.0, 525.0,
                0, 562.0,
                0, 214.0
        ));
        polygons.add(new Polygon(               // 3
                5.0, 562.0,
                283.0, 522.0,
                283.0, 551.0,
                303.0, 576.0,
                413.0, 589.0,
                438.0, 612.0,
                438.0, 633.0,
                368.0, 673.0,
                354.0, 700.0,
                374.0, 732.0,
                445.0, 768.0,
                0, 768.0,
                0, 562.0
        ));
        polygons.add(new Polygon(               // 4
                266.0, 246.0,
                285.0, 169.0,
                332.0, 140.0,
                404.0, 144.0,
                530.0, 168.0,
                555.0, 177.0,
                540.0, 223.0,
                558.0, 283.0,
                640.0, 377.0,
                616.0, 401.0,
                575.0, 395.0,
                508.0, 372.0,
                386.0, 353.0,
                269.0, 350.0,
                266.0, 240.0
        ));
        polygons.add(new Polygon(               //  5
                414.0, 588.0,
                607.0, 572.0,
                638.0, 588.0,
                679.0, 626.0,
                706.0, 629.0,
                640.0, 768.0,
                445.0, 768.0,
                377.0, 731.0,
                357.0, 700.0,
                368.0, 677.0,
                440.0, 635.0,
                438.0, 614.0,
                416.0, 588.0

        ));
        polygons.add(new Polygon(               // 6
                812.0, 357.0,
                880.0, 387.0,
                950.0, 397.0,
                1011.0, 363.0,
                1040.0, 395.0,
                1106.0, 471.0,
                1119.0, 518.0,
                1098.0, 558.0,
                933.0, 641.0,
                909.0, 663.0,
                874.0, 640.0,
                892.0, 605.0,
                894.0, 531.0,
                879.0, 511.0,
                864.0, 444.0,
                831.0, 391.0,
                807.0, 376.0
        ));
        polygons.add(new Polygon(               // 7
                1202.0, 519.0,
                1368.0, 515.0,
                1368.0, 768.0,
                1173.0, 768.0,
                1186.0, 658.0,
                1183.0, 581.0
        ));
        polygons.add(new Polygon(               // 8
                1208.0, 160.0,
                1238.0, 127.0,
                1300.0, 193.0,
                1366.0, 219.0,
                1366.0, 515.0,
                1200.0, 519.0,
                1208.0, 163.0

        ));
        polygons.add(new Polygon(               // 9
                270.0, 349.0,
                391.0, 350.0,
                508.0, 370.0,
                577.0, 398.0,
                619.0, 402.0,
                636.0, 376.0,
                679.0, 425.0,
                700.0, 497.0,
                704.0, 507.0,
                636.0, 586.0,
                608.0, 572.0,
                416.0, 588.0,
                305.0, 579.0,
                283.0, 553.0
        ));
        polygons.add(new Polygon(               // 10
                555.0, 179.0,
                627.0, 194.0,
                785.0, 301.0,
                815.0, 360.0,
                807.0, 375.0,
                830.0, 391.0,
                860.0, 441.0,
                881.0, 515.0,
                894.0, 529.0,
                893.0, 605.0,
                893.0, 603.0,
                847.0, 554.0,
                762.0, 575.0,
                706.0, 629.0,
                681.0, 625.0,
                638.0, 584.0,
                706.0, 506.0,
                678.0, 428.0,
                637.0, 378.0,
                555.0, 275.0,
                538.0, 222.0
        ));
        polygons.add(new Polygon(               // 11
                706.0, 629.0,
                762.0, 575.0,
                847.0, 554.0,
                893.0, 603.0,
                875.0, 637.0,
                877.0, 641.0,
                920.0, 669.0,
                944.0, 696.0,
                967.0, 744.0,
                964.0, 768.0,
                642.0, 768.0
        ));
        polygons.add(new Polygon(               // 12
                912.0, 658.0,
                930.0, 640.0,
                1098, 558.0,
                1118.0, 521.0,
                1202.0, 519.0,
                1183.0, 581.0,
                1185.0, 660.0,
                1173.0, 768.0,
                963.0, 768.0,
                967.0, 748.0,
                944.0, 696.0,
                922.0, 670.0,
                909.0, 660.0
        ));
        polygons.add(new Polygon(               // 13
                1006.0, 239.0,
                1053.0, 223.0,
                1202.0, 287.0,
                1199.0, 518.0,
                1117.0, 520.0,
                1104.0, 473.0,
                1041.0, 395.0,
                1011.0, 363.0,
                1032.0, 285.0,
                1012.0, 243.0
        ));
        polygons.add(new Polygon(               // 14
                770.0, 161.0,
                949.0, 80.0,
                988.0, 26.0,
                1146.0, 64.0,
                1246.0, 116.0,
                1210.0, 156.0,
                1203.0, 286.0,
                1053.0, 227.0,
                1009.0, 242.0,
                975.0, 216.0,
                955.0, 207.0,
                916.0, 201.0,
                829.0, 210.0,
                775.0, 188.0
        ));
        polygons.add(new Polygon(               // 15
                559.0, 174.0,
                636.0, 94.0,
                676.0, 117.0,
                742.0, 125.0,
                770.0, 154.0,
                775.0, 188.0,
                826.0, 210.0,
                915.0, 203.0,
                953.0, 207.0,
                975.0, 214.0,
                1002.0, 237.0,
                1012.0, 243.0,
                1033.0, 283.0,
                1011.0, 363.0,
                950.0, 397.0,
                879.0, 387.0,
                812.0, 356.0,
                785.0, 301.0,
                625.0, 194.0,
                557.0, 177.0
        ));
        polygons.add(new Polygon(               // 16
                608.0, 0,
                947.0, 0,
                987.0, 25.0,
                947.0, 82.0,
                772.0, 165.0,
                768.0, 152.0,
                744.0, 125.0,
                682.0, 119.0,
                635.0, 93.0,
                555.0, 176.0,
                422.0, 146.0,
                411.0, 126.0,
                606.0, 0.0
        ));
        return polygons;
    }

    private static Coordinates centerOf(Node n) {
        Bounds b = n.getBoundsInParent();
        return new Coordinates((b.getMaxX() + b.getMinX()) / 2, (b.getMaxY() + b.getMinY()) / 2);
    }


    @Override
    public void handleUnitMovedNetevent(UnitMovedNetevent event) {
        if (event.getDestination() != Integer.MIN_VALUE) {
            if (event.getSource() != Integer.MIN_VALUE) {
                moveGComponentFxThread(GUnit.get(event.getUnitID()), event.getSource(), event.getDestination());
            } else {
                addGComponentFxThread(GUnit.get(event.getUnitID()), event.getDestination());
            }
        } else if (event.getSource() != Integer.MIN_VALUE) {
            removeGComponentFxThread(GUnit.get(event.getUnitID()), event.getSource());
        } else {
            Log.warn("Received " + UnitMovedNetevent.class.getName().toString() + " with both destination and target and 0.");
        }
    }

    @Override
    public void handleStudioBuiltOrDestroyed(StudioNetevent studioNetevent) {
        // nothing to do
    }

    @Override
    public void handleStudioPlayered(StudioNetevent studioNetevent) {
        // nothing to do
    }

    @Override
    public void handleStudioMapped(StudioNetevent studioNetevent) {
        if (studioNetevent.getType() == StudioEvent.Type.ADDED_TO_MAP) {
            this.addGComponentFxThread(GStudio.get(studioNetevent.getZoneID()), studioNetevent.getZoneID());
        } else if (studioNetevent.getType() == StudioEvent.Type.REMOVED_FROM_MAP) {
            this.removeGComponentFxThread(GStudio.get(studioNetevent.getZoneID()), studioNetevent.getZoneID());
        }
    }

    @Override
    public void handlePreBattle(BattleNetevent event) {
        // todo
    }

    @Override
    public void handleDuringBattle(BattleNetevent event) {
        // todo
    }

    @Override
    public void handlePostBattle(BattleNetevent event) {
        // todo
    }

    @Override
    public void handleBattleFinished(BattleNetevent event) {
        this.unHighlightFxThread(event.getZone());
    }

    @Override
    public void handleStudioControllerChanged(StudioControllerChangedNetevent event) {
        Platform.runLater(() -> {
            if (event.getControllerFaction() == null) {
                Line l = this.STUDIOLINKS.remove(new Integer(event.getZoneID()));
                getChildren().remove(l);
            } else {
                Coordinates centerOfStudio = centerOf(GStudio.get(event.getZoneID()));
                Coordinates centerOfUnit = centerOf(GUnit.get(event.getControllerID()));

                Line line = new Line(
                        centerOfStudio.x,
                        centerOfStudio.y,
                        centerOfUnit.x,
                        centerOfUnit.y
                );
                line.setStrokeWidth(2.);
                line.setOpacity(0.4);
                line.setStroke(GlobalChat.getClientColor(GlobalChat.getFactionClient(event.getControllerFaction())));
                line.getStrokeDashArray().addAll(4.);
                line.setMouseTransparent(true);
                getChildren().add(line);
                line.toFront();
                this.STUDIOLINKS.put(new Integer(event.getZoneID()), line);
            }
        });
    }

    @Override
    public void handleGameEnd(GameEndedNetevent gameEndedEvent) {
        Platform.runLater(this::clear);
    }
}
