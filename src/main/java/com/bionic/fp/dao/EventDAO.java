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
     * @param name the name of the event
     * @param description the description of the event
     * @return a list of events
     */
    List<Event> get(String name, String description);

    /**
     * Returns a list of events which are within the specified radius from the specified coordinates
     * http://www.plumislandmedia.net/mysql/haversine-mysql-nearest-loc/
     *
     * @param latitude the latitude of the centre
     * @param longitude the longitude of the centre
     * @param radius the radius
     * @return a list of events
     */
    List<Event> get(double latitude, double longitude, float radius);
}
