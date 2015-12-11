package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Event;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Yevhenii on 12/8/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsList {

    @JsonProperty("all_events")
    private List<Event> allEvents;

    public EventsList() {
    }

    public List<Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(List<Event> allEvents) {
        this.allEvents = allEvents;
    }
}
