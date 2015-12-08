package com.bionic.fp.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Yevhenii on 12/8/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventsIDsList {

    @JsonProperty("all_events_id")
    private List<Long> allEventsIDs;

    public EventsIDsList() {
    }

    public EventsIDsList(List<Long> allEventsIDs) {
        this.allEventsIDs = allEventsIDs;
    }

    public List<Long> getAllEventsIDs() {
        return allEventsIDs;
    }

    public void setAllEventsIDs(List<Long> allEventsIDs) {
        this.allEventsIDs = allEventsIDs;
    }
}
