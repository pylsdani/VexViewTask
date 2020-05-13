package ink.ptms.cronus.internal.bukkit;

import ink.ptms.cronus.util.Utils;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-24 14:09
 */
public class Location {

    private Mode mode;
    private org.bukkit.Location[] area;
    private org.bukkit.Location[] points;
    private int range;

    public Location(Mode mode, org.bukkit.Location[] area, org.bukkit.Location[] points) {
        this.mode = mode;
        this.area = area;
        this.points = points;
    }

    public Location(Mode mode, org.bukkit.Location[] points, int range) {
        this.mode = mode;
        this.points = points;
        this.range = range;
    }

    public org.bukkit.Location toBukkit() {
        return points != null && points.length > 0 ? points[0] : new org.bukkit.Location(Bukkit.getWorlds().get(0), 0, 0, 0);
    }

    public boolean isBukkit() {
        try {
            return toBukkit() != null;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public boolean isSelect(org.bukkit.Location locationA, org.bukkit.Location locationB) {
        return locationA.getWorld().equals(locationB.getWorld()) && locationA.getX() == locationB.getX() && locationA.getY() == locationB.getY() && locationA.getZ() == locationB.getZ();
    }

    public boolean inSelect(org.bukkit.Location location) {
        if (!isSelectWorld(location)) {
            return false;
        }
        switch (mode) {
            case AREA:
                return area != null && location.toVector().isInAABB(area[0].toVector(), area[1].toVector());
            case POINT:
                return points != null && Arrays.stream(points).anyMatch(p -> isSelect(p, location));
            case RANGE:
                return points != null && toBukkit().distance(location) <= range;
            default:
                return false;
        }
    }

    public boolean isSelectWorld(org.bukkit.Location location) {
        switch (mode) {
            case AREA:
                return area != null && location.getWorld().equals(area[0].getWorld());
            default:
                return points != null && Arrays.stream(points).anyMatch(p -> p.getWorld().equals(location.getWorld()));
        }
    }

    public String asString() {
        switch (mode) {
            case AREA:
                return area == null ? "-" : Utils.fromLocation(area[0]) + "~" + Utils.fromLocation(area[1]);
            case POINT:
                return points == null ? "-" : Arrays.stream(points).map(Utils::fromLocation).collect(Collectors.joining(";"));
            case RANGE:
                return points == null || points.length == 0 ? "-" : Utils.fromLocation(points[0]) + " r:" + range;
            default:
                return "-";
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "mode=" + mode +
                ", area=" + Arrays.toString(area) +
                ", points=" + Arrays.toString(points) +
                ", range=" + range +
                '}';
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public Mode getMode() {
        return mode;
    }

    public org.bukkit.Location[] getArea() {
        return area;
    }

    public org.bukkit.Location[] getPoints() {
        return points;
    }

    public enum Mode {

        AREA, POINT, RANGE
    }
}
