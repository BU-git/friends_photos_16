package com.bionic.fp.dao;

import com.bionic.fp.domain.*;

import java.util.List;

/**
 * Represents data access object of the event
 *
 * @author Sergiy Gabriel
 */
public interface EventDAO extends GenericDAO<Event, Long> {

    /**
     * Returns a list of events as the result of searching by name and description
     *
     * @param visible the visibility of the event.
     *                Null if no need to take into account the visibility of the event
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    List<Event> get(Boolean visible, String name, String description);

    /**
     * Returns a list of events which are within the specified radius from the specified coordinates
     * @see <a href="http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/">
     *     http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/</a>
     *
     * @param visible the visibility of the event.
     *                Null if no need to take into account the visibility of the event
     * @param latitude the latitude of the centre
     * @param longitude the longitude of the centre
     * @param radius the radius
     * @return a list of events
     */
    List<Event> get(Boolean visible, double latitude, double longitude, float radius);

    /**
     * Returns a list of events in the coordinate range
     *
     * @param visible the visibility of the event.
     *                Null if no need to take into account the visibility of the event
     * @param latMin the minimal latitude
     * @param lngMin the minimal longitude
     * @param latMax the maximal latitude
     * @param lngMax the maximal longitude
     * @return a list of events
     */
    List<Event> get(Boolean visible, double latMin, double lngMin,double latMax, double lngMax);
}
