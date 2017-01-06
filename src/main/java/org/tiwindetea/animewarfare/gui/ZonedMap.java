package org.tiwindetea.animewarfare.gui;

import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;
import org.lomadriel.lfc.event.EventDispatcher;
import org.tiwindetea.animewarfare.gui.event.ZoneClickedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maliafo on 06/01/17.
 */
public class ZonedMap extends Pane {

    // TODO: rework this class

    private final ImageView MAP_PICT;
    private final Pane PANE = new Pane();
    private final ZoneUnitMap MAP = new ZoneUnitMap();
    private final List<Pair<Polygon, Tooltip>> ZONES = new ArrayList<>(17);

    public ZonedMap() {
        super();
        this.MAP_PICT = new ImageView(new Image(this.getClass().getClassLoader().getResource("org/tiwindetea/animewarfare/gui/game/pictures/map.png").toString()));
        initZones();
        getChildren().addAll(this.MAP_PICT, this.PANE, this.MAP);
    }

    private void initZones() {

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
                633.0, 93.0,
                558.0, 177.0,
                528.0, 166.0,
                401.0, 142.0,
                330.0, 142.0,
                286.0, 171.0,
                265.0, 244.0,
                254.0, 216.0,
                225.0, 177.0,
                172.0, 166.0,
                99.0, 169.0,
                33.0, 189.0,
                0, 218.0,
                0, 0
        ));

        polygons.add(new Polygon(               // 2
                4.0, 219.0,
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
                876.0, 639.0,
                708.0, 632.0,
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
                635.0, 95.0,
                613.0, 8.0
        ));


        for (int i = 0; i < polygons.size(); i++) {
            int magic_i = i;
            Polygon polygon = polygons.get(i);
            polygon.setFill(Color.WHITE);
            polygon.setStroke(Color.BLACK);
            polygon.setStrokeWidth(3);
            polygon.setOpacity(0);
            polygon.setPickOnBounds(false);
            polygon.setOnMouseEntered(e -> polygon.setOpacity(0.3));
            polygon.setOnMouseExited(e -> polygon.setOpacity(0));
            polygon.setOnMouseClicked(e -> EventDispatcher.send(new ZoneClickedEvent(magic_i, e)));
            Tooltip tooltip = new Tooltip(getDescription(i));
            Tooltip.install(polygon, tooltip);
            this.ZONES.add(new Pair(polygon, tooltip));
        }
        this.PANE.getChildren().addAll(polygons);
        this.MAP.setZones(polygons);
    }

    public void addUnit(GUnit unit, int zoneID) {
        this.MAP.put(zoneID, unit);
        this.ZONES.get(zoneID).getValue().setText(getDescription(zoneID));
    }

    public void removeUnit(GUnit unit, int zoneID) {
        this.MAP.remove(unit, zoneID);
        this.ZONES.get(zoneID).getValue().setText(getDescription(zoneID));
    }

    public void unHighlight(int zoneID) {
        this.ZONES.get(zoneID).getKey().setOpacity(0);
    }

    public void highlightNeighbour(int zoneID, int distance) {

    }

    public void unHighlightNeigbour(int zoneID, int distance) {

    }

    private String getDescription(int zoneID) {
        return "Number of units zone " + zoneID + ": " + this.MAP.getNumberOfUnits(zoneID);
    }

    private static final class UnitRectangle extends Parent {

        final double x, y;
        static final int WIDTH = 42;
        static final int HEIGHT = 42;

        private GUnit unit;

        public UnitRectangle(double x, double y) {
            this.x = x;
            this.y = y;/*
            Polygon p = new Polygon(x, y, x, y + WIDTH, x + WIDTH, y + WIDTH, x + WIDTH, y);
            p.setFill(Color.TRANSPARENT);
            p.setStroke(Color.RED);
            p.setMouseTransparent(true);
            getChildren().add(p);*/
        }

        public void setUnit(GUnit unit) {
            if (this.unit != null) {
                this.getChildren().remove(this.unit);
            }
            if (unit != null) {
                unit.setTranslateX(this.x + WIDTH / 2);
                unit.setTranslateY(this.y + HEIGHT / 2);
                this.getChildren().add(unit);
            }
            this.unit = unit;
        }

        public GUnit getUnit() {
            return this.unit;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof UnitRectangle) && this.equals((UnitRectangle) o);
        }

        public boolean equals(UnitRectangle r) {
            return r.x == this.x && r.y == this.y;
        }

        @Override
        public int hashCode() {
            return (int) (this.x + this.y);
        }
    }

    private static final class ZoneUnitMap extends Parent {

        private static final class Int {
            int value = 0;
        }

        private final HashMap<Integer, List<UnitRectangle>> map = new HashMap<>();
        private final HashMap<Integer, List<GUnit>> orphans = new HashMap<>();

        private final List<Int> numberOfUnitsPerZone = new ArrayList<>(17);

        public ZoneUnitMap() {
            for (int i = 0; i < 17; i++) {
                this.numberOfUnitsPerZone.add(new Int());
            }
        }

        void setZones(List<Polygon> polygons) {

            if (this.map.size() != 0) {
                throw new IllegalStateException();
            }

            int i = 0;

            for (Polygon polygon : polygons) {
                List<UnitRectangle> rectList = new ArrayList<>(20);
                Bounds bounds = polygon.getLayoutBounds();
                for (double y = bounds.getMinY() + 3; y < bounds.getMaxY(); y += UnitRectangle.HEIGHT) {
                    for (double x = bounds.getMinX() + 3; x < bounds.getMaxX(); x += UnitRectangle.WIDTH) {
                        if (polygon.contains(x - 2, y - 2)
                                && polygon.contains(x + UnitRectangle.WIDTH + 2, y - 2)
                                && polygon.contains(x - 2, y + UnitRectangle.HEIGHT + 2)
                                && polygon.contains(x + UnitRectangle.WIDTH + 2, y + UnitRectangle.HEIGHT + 2)
                                ) {

                            rectList.add(new UnitRectangle(x, y));
                        }
                    }
                }
                getChildren().addAll(rectList);

                this.map.put(new Integer(i), rectList);
                ++i;
            }
        }

        public void put(int zoneID, GUnit unit) {
            UnitRectangle unitRectangle = this.map.get(new Integer(zoneID)).parallelStream()
                    .filter(ur -> ur.unit == null)
                    .findAny().orElse(null);
            if (unitRectangle != null) {
                unitRectangle.setUnit(unit);
            } else {
                List<GUnit> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    list.add(unit);
                } else {
                    list = new ArrayList<>(5);
                    list.add(unit);
                    this.orphans.put(new Integer(zoneID), list);
                }
            }
            this.numberOfUnitsPerZone.get(zoneID).value++;
        }

        public void remove(GUnit unit, int zoneID) {
            UnitRectangle unitRectangle = this.map.get(new Integer(zoneID)).parallelStream()
                    .filter(ur -> unit.equals(ur.getUnit()))
                    .findAny()
                    .orElse(null);
            if (unitRectangle != null) {
                GUnit newUnit = null;
                List<GUnit> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    if (list.size() != 0) {
                        newUnit = list.remove(0);
                    }
                }
                unitRectangle.setUnit(newUnit);
                --this.numberOfUnitsPerZone.get(zoneID).value;
            } else {
                List<GUnit> list = this.orphans.get(new Integer(zoneID));
                if (list != null) {
                    if (list.remove(unit)) {
                        --this.numberOfUnitsPerZone.get(zoneID).value;
                    }
                }
            }
        }

        public int getNumberOfUnits(int zoneID) {
            return this.numberOfUnitsPerZone.get(zoneID).value;
        }
    }

}
