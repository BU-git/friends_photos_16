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
     * @param coordinate the coordinate of the centre
     * @param radius the radius
     * @return a list of events
     */
    List<Event> get(Boolean visible, Coordinate coordinate, float radius);

    /**
     * Returns a list of events in the coordinate range
     *
     * @param visible the visibility of the event.
     *                Null if no need to take into account the visibility of the event
     * @param sw the South-West coordinate
     * @param ne the North-East coordinate
     * @return a list of events
     */
    List<Event> get(Boolean visible, Coordinate sw, Coordinate ne);
}
