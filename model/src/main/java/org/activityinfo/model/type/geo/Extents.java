/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.model.type.geo;

import org.activityinfo.json.JsonValue;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSetter;

import java.io.Serializable;

import static org.activityinfo.json.Json.createObject;

/*
 * Bounding box for a map. 
 * 
 * This cannot be mapped 1:1 to a rectangle, since a lat/long combination is a coordinate on
 * a sphere as opposed to a coordinate on a 2D plane.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Extents implements Serializable {

    private static final int LAT_MAX = 90;
    private static final int LNG_MAX = 180;
    private static final int LAT_MIN = -LAT_MAX;
    private static final int LNG_MIN = -180;

    private double minLat;
    private double maxLat;
    private double minLon;
    private double maxLon;

    private Extents() {

    }

    public Extents(double minLat, double maxLat, double minLon, double maxLon) {
        super();
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
    }

    public Extents(Extents toCopy) {
        super();
        this.minLat = toCopy.minLat;
        this.maxLat = toCopy.maxLat;
        this.minLon = toCopy.minLon;
        this.maxLon = toCopy.maxLon;
    }

    /**
     * @return maximum geographic bounds (-180, -90, 180, 90)s
     */
    public static Extents maxGeoBounds() {
        return new Extents(LAT_MIN, LAT_MAX, LNG_MIN, LNG_MAX);
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public void grow(double lat, double lng) {
        if (lat < minLat) {
            minLat = lat;
        }
        if (lat > maxLat) {
            maxLat = lat;
        }
        if (lng < minLon) {
            minLon = lng;
        }
        if (lng > maxLon) {
            maxLon = lng;
        }
    }

    /**
     * Calculates the intersection of this Extents with given Extents
     *
     * @param b another Extents with which to intersect this Extents
     * @return the intersection of the two Extentss
     */
    public Extents intersect(Extents b) {
        return new Extents(Math.max(minLat, b.minLat),
                Math.min(maxLat, b.maxLat),
                Math.max(minLon, b.minLon),
                Math.min(maxLon, b.maxLon));
    }

    /**
     * @return true if this Extents intersects with <code>b</code>
     */
    public boolean intersects(Extents b) {
        return !(b.maxLon < minLon || b.minLon > maxLon || b.maxLat < minLat || b.minLat > maxLat);
    }

    public void grow(Extents extents) {

        if (!extents.isEmpty()) {
            grow(extents.minLat, extents.minLon);
            grow(extents.maxLat, extents.maxLon);
        }
    }

    public static Extents emptyExtents() {
        return new Extents(+90.0, -90.0, +180.0, -180.0);
    }

    public static Extents empty() {
        return emptyExtents();
    }

    /**
     * @param b
     * @return true if this Extents contains <code>b</code>
     */
    public boolean contains(Extents b) {
        return b.minLon >= minLon && b.maxLon <= maxLon && b.minLat >= minLat && b.maxLat <= maxLat;
    }

    public boolean contains(AiLatLng center) {
        return contains(center.getLng(), center.getLat());
    }

    /**
     * @return true if this Extents contains the point at (x,y)
     */
    public boolean contains(double x, double y) {
        return x >= minLon && x <= maxLon && y >= minLat && y <= maxLat;
    }

    public static Extents create(double x1, double y1, double x2, double y2) {
        assert x1 <= x2 : x1 + " should be less than or equal to " + x2;
        assert y1 <= y2 : y1 + " should be less than or equal to " + y2;
        return new Extents(y1, y2, x1, x2);
    }
    

    public static Extents fromLatLng(double latitude, double longitude) {
        return new Extents(latitude, latitude, longitude, longitude);
    }
    
    /**
     * @return the x (longitude) coordinate of the Extents's centroid, (x1+x2)/2
     */
    public double getCenterX() {
        return (minLon + maxLon) / 2d;
    }

    /**
     * @return the y (latitudinal) coordinate of the Extents's centroid,
     * (y1+y2)/2
     */
    public double getCenterY() {
        return (minLat + maxLat) / 2d;
    }

    public boolean isEmpty() {
        return minLat > maxLat || minLon > maxLon;
    }

    public AiLatLng center() {
        return new AiLatLng((minLat + maxLat) / 2.0, (minLon + maxLon) / 2.0);
    }
    
    public double area() {
        if(isEmpty()) {
            return 0d; 
        } else {
            return (maxLat - minLat) * (maxLon - minLon);
        }
    }

    @JsonSetter
    public void setX1(double x1) {
        setMinLon(x1);
    }
    
    @JsonSetter
    public void setY1(double y1) {
        setMinLat(y1);
    }
    
    @JsonSetter
    public void setX2(double x2) {
        setMaxLon(x2);
    }
    
    @JsonSetter
    public void setY2(double y2) {
        setMaxLat(y2);
    }
    
    public double getX1() {
        return getMinLon();
    }
    
    public double getY1() {
        return getMinLat();
    }
    
    public double getX2() {
        return getMaxLon();
    }
    
    public double getY2() {
        return getMaxLat();
    }
    
    @Override
    public int hashCode() {
        return (minLon + "").hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Extents other = (Extents) obj;
        return minLat == other.minLat &&
               maxLat == other.maxLat &&
               minLon == other.minLon &&
               maxLon == other.maxLon;
    }
    
    

    @Override
    public String toString() {
        return "Extents{" +
               "minLat=" + minLat +
               ", maxLat=" + maxLat +
               ", minLon=" + minLon +
               ", maxLon=" + maxLon +
               '}';
    }

    public JsonValue toJsonElement() {
        JsonValue object = createObject();
        if (!Double.isNaN(minLat)) {
            object.put("minLat", minLat);
        }
        if (!Double.isNaN(maxLat)) {
            object.put("maxLat", maxLat);
        }
        if (!Double.isNaN(minLon)) {
            object.put("minLon", minLon);
        }
        if (!Double.isNaN(maxLon)) {
            object.put("maxLon", maxLon);
        }
        return object;
    }

    public static Extents fromJsonObject(JsonValue object) {
        Extents area = Extents.empty();
        if(object.hasKey("minLat")) {
            area.minLat = object.get("minLat").asNumber();
        }
        if(object.hasKey("maxLat")) {
            area.maxLat = object.get("maxLat").asNumber();
        }
        if(object.hasKey("minLon")) {
            area.minLon = object.get("minLon").asNumber();
        }
        if(object.hasKey("maxLon")) {
            area.maxLon = object.get("maxLon").asNumber();
        }
        return area;
    }
}
