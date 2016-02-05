package com.bionic.fp.util;

import com.bionic.fp.domain.Event;
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

    /**
     * 111.045 km per degree of latitude
     * @see <a href="http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/">
     *     http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/</a>
     */
    public static final double KM_PER_DEGREE_LATITUDE = 111.045;

    private GeoUtils() {}

    /**
     * Returns the distance between two geographic coordinates
     *
     * @param lat0 the first latitude
     * @param lng0 the first longitude
     * @param lat1 the second latitude
     * @param lng1 the second longitude
     * @return the distance
     */
    public static double getDistance(final double lat0, final double lng0,
                                     final double lat1, final double lng1) {
        return KM_PER_DEGREE_LATITUDE * toDegrees(acos(
                cos(toRadians(lat0)) * cos(toRadians(lat1)) *
                cos(toRadians(lng0 - lng1)) +
                sin(toRadians(lat0)) * sin(toRadians(lat1))));
    }

    /**
     * Returns the distance between two events
     *
     * @param first the first event
     * @param second the second event
     * @return the distance
     */
    public static double getDistance(final Event first, final Event second) throws InvalidParameterException {
        checkNotNull(first, "first event");
        checkNotNull(second, "second event");
        check(first.isGeoServicesEnabled(), "The geo services of the first event are disabled");
        check(second.isGeoServicesEnabled(), "The geo services of the second event are disabled");
        checkNotNull(first.getLatitude(), "latitude of the first event");
        checkNotNull(first.getLongitude(), "longitude of the first event");
        checkNotNull(second.getLatitude(), "latitude of the second event");
        checkNotNull(second.getLongitude(), "longitude of the second event");

        return getDistance(first.getLatitude(), first.getLongitude(), second.getLatitude(), second.getLongitude());
    }
}
