package com.bionic.fp.util;

import com.bionic.fp.domain.Coordinate;
import com.bionic.fp.exception.logic.InvalidParameterException;

import static com.bionic.fp.util.Checks.check;
import static com.bionic.fp.util.Checks.checkNotNull;
import static java.lang.Math.*;
import static java.lang.Math.sin;

/**
 * Contains geo utils
 *
 * @author Sergiy Gabriel
 */
public class GeoUtils {

    private GeoUtils() {}

    /**
     * Distance unit per degree of latitude
     * @see <a href="http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/">
     *     http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/</a>
     */
    public enum DistanceUnitPerDegree {
        KM(111.045),
        MILE(69.0),
        NAUTICAL(60.0);

        private final double distance;

        DistanceUnitPerDegree(double distance) {
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }
    }

    /**
     * Returns the distance between two geographic coordinates.
     * Haversine formula
     *
     * @param lat0 the first latitude
     * @param lng0 the first longitude
     * @param lat1 the second latitude
     * @param lng1 the second longitude
     * @param distanceUnit the distance unit per degree of latitude
     * @return the distance
     * @throws InvalidParameterException if the incoming parameters are incorrect
     */
    public static double getDistance(final double lat0, final double lng0,
                                     final double lat1, final double lng1,
                                     final DistanceUnitPerDegree distanceUnit) {
        checkNotNull(distanceUnit, "distance unit");
        if(Double.compare(lat0, lat1) == 0 && Double.compare(lng0, lng1) == 0) {
            return 0;
        }
        return distanceUnit.getDistance() * toDegrees(acos(
                cos(toRadians(lat0)) * cos(toRadians(lat1)) *
                cos(toRadians(lng0 - lng1)) +
                sin(toRadians(lat0)) * sin(toRadians(lat1))));
    }

    /**
     * Returns the distance between two geographic coordinates.
     * Vincenty formula
     * It’s more stable for small distances.
     * In those cases the cosine is very close to 1, so the inverse cosine function
     * is not as precise as the inverse tangent function used here.
     * @see <a href="https://en.wikipedia.org/wiki/Great-circle_distance">
     *     https://en.wikipedia.org/wiki/Great-circle_distance</a>
     *
     * @param lat0 the first latitude
     * @param lng0 the first longitude
     * @param lat1 the second latitude
     * @param lng1 the second longitude
     * @param distanceUnit the distance unit per degree of latitude
     * @return the distance
     * @throws InvalidParameterException if the incoming parameters are incorrect
     */
    public static double getDistanceViaVincenty(final double lat0, final double lng0,
                                                final double lat1, final double lng1,
                                                final DistanceUnitPerDegree distanceUnit) {
        checkNotNull(distanceUnit, "distance unit");
        return distanceUnit.getDistance() * toDegrees(atan2(
                sqrt(
                        pow(cos(toRadians(lat1)) * sin(toRadians(lng1 - lng0)), 2) +
                                pow(cos(toRadians(lat0)) * sin(toRadians(lat1)) -
                                        (sin(toRadians(lat0)) * cos(toRadians(lat1)) * cos(toRadians(lng1 - lng0))), 2)),
                cos(toRadians(lat0)) * cos(toRadians(lat1)) * cos(toRadians(lng0 - lng1)) +
                        sin(toRadians(lat0)) * sin(toRadians(lat1))));
    }

    /**
     * Returns the distance between two geographic coordinates
     * Haversine formula
     *
     * @param first the first coordinate
     * @param second the second coordinate
     * @param distanceUnit the distance unit per degree of latitude
     * @return the distance
     * @throws InvalidParameterException if the incoming parameters are incorrect
     */
    public static double getDistance(final Coordinate first, final Coordinate second,
                                     final DistanceUnitPerDegree distanceUnit) {
        checkNotNull(first, "first coordinate");
        checkNotNull(second, "second coordinate");
        return getDistance(
                first.getLatitude(), first.getLongitude(),
                second.getLatitude(), second.getLongitude(),
                distanceUnit);
    }

    /**
     * Returns the distance between two geographic coordinates
     * Vincenty formula
     * It’s more stable for small distances.
     * In those cases the cosine is very close to 1, so the inverse cosine function
     * is not as precise as the inverse tangent function used here.
     * @see <a href="https://en.wikipedia.org/wiki/Great-circle_distance">
     *     https://en.wikipedia.org/wiki/Great-circle_distance</a>
     *
     * @param first the first coordinate
     * @param second the second coordinate
     * @param distanceUnit the distance unit per degree of latitude
     * @return the distance
     * @throws InvalidParameterException if the incoming parameters are incorrect
     */
    public static double getDistanceViaVincenty(final Coordinate first, final Coordinate second,
                                                final DistanceUnitPerDegree distanceUnit) {
        checkNotNull(first, "first coordinate");
        checkNotNull(second, "second coordinate");
        return getDistanceViaVincenty(
                first.getLatitude(), first.getLongitude(),
                second.getLatitude(), second.getLongitude(),
                distanceUnit);
    }
}
