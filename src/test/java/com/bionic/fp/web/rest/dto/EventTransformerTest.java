package com.bionic.fp.web.rest.dto;

import com.bionic.fp.AbstractHelperTest;
import com.bionic.fp.Constants.RestConstants.EVENT;
import com.bionic.fp.domain.Event;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class EventTransformerTest extends AbstractHelperTest {

    @Test
    public void testEventTransformWithoutFieldsSuccess() throws Exception {
        Event event = getNewEventMax();
        event.setId(123L);

        // fields is null
        EventInfo eventInfo = EventInfo.Transformer.transform(event, null);
        assertAllFields(event, eventInfo);

        // fields is empty
        eventInfo = EventInfo.Transformer.transform(event, "");
        assertAllFields(event, eventInfo);

        // the fields without the specified properties
        eventInfo = EventInfo.Transformer.transform(event, "I'll,be,back");
        assertAllFields(event, eventInfo);
    }

    @Test
    public void testEventsTransformWithoutFieldsSuccess() throws Exception {
        List<Event> events = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Event event = getNewEventMax();
            event.setId(i*10L);
            events.add(event);
        }

        // fields is null
        List<EventInfo> eventInfos = EventInfo.Transformer.transform(events, null);
        assertAllFields(events, eventInfos);


        // fields is empty
        eventInfos = EventInfo.Transformer.transform(events, "");
        assertAllFields(events, eventInfos);

        // the fields without the specified properties
        eventInfos = EventInfo.Transformer.transform(events, "I'll,be,back");
        assertAllFields(events, eventInfos);
    }

    @Test
    public void testEventTransformFieldsSuccess() throws Exception {
        Event event = getNewEventMax();
        event.setId(123L);

        // fields=name
        EventInfo eventInfo = EventInfo.Transformer.transform(event, EVENT.NAME);
        assertEquals(event.getName(), eventInfo.getName());
        assertNull(eventInfo.getDescription());
        assertNull(eventInfo.getId());
        assertNull(eventInfo.getTypeId());
        assertNull(eventInfo.getVisible());
        assertNull(eventInfo.getLatitude());
        assertNull(eventInfo.getLongitude());
        assertNull(eventInfo.getRadius());
        assertNull(eventInfo.getGeo());
        assertNull(eventInfo.getDate());
        assertNull(eventInfo.getExpireDate());

        // fields=name,description
        eventInfo = EventInfo.Transformer.transform(event, String.format("%s,%s", EVENT.NAME, EVENT.DESCRIPTION));
        assertEquals(event.getName(), eventInfo.getName());
        assertEquals(event.getDescription(), eventInfo.getDescription());
        assertNull(eventInfo.getId());
        assertNull(eventInfo.getTypeId());
        assertNull(eventInfo.getVisible());
        assertNull(eventInfo.getLatitude());
        assertNull(eventInfo.getLongitude());
        assertNull(eventInfo.getRadius());
        assertNull(eventInfo.getGeo());
        assertNull(eventInfo.getDate());
        assertNull(eventInfo.getExpireDate());

        // fields=name,event_id,description
        eventInfo = EventInfo.Transformer.transform(event, String.format("%s,%s,%s", EVENT.NAME, EVENT.ID, EVENT.DESCRIPTION));
        assertEquals(event.getName(), eventInfo.getName());
        assertEquals(event.getDescription(), eventInfo.getDescription());
        assertEquals(event.getId(), eventInfo.getId());
        assertNull(eventInfo.getTypeId());
        assertNull(eventInfo.getVisible());
        assertNull(eventInfo.getLatitude());
        assertNull(eventInfo.getLongitude());
        assertNull(eventInfo.getRadius());
        assertNull(eventInfo.getGeo());
        assertNull(eventInfo.getDate());
        assertNull(eventInfo.getExpireDate());
    }

    @Test
    public void testEventsTransformFieldsSuccess() throws Exception {
        List<Event> events = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Event event = getNewEventMax();
            event.setId(i*10L);
            events.add(event);
        }


        // fields=name
        List<EventInfo> eventInfos = EventInfo.Transformer.transform(events, EVENT.NAME);
        Iterator<Event> eventIterator = events.iterator();
        Iterator<EventInfo> eventInfoIterator = eventInfos.iterator();
        while (eventIterator.hasNext() && eventInfoIterator.hasNext()) {
            Event event = eventIterator.next();
            EventInfo eventInfo = eventInfoIterator.next();

            assertEquals(event.getName(), eventInfo.getName());

            assertNull(eventInfo.getDescription());
            assertNull(eventInfo.getId());
            assertNull(eventInfo.getTypeId());
            assertNull(eventInfo.getVisible());
            assertNull(eventInfo.getLatitude());
            assertNull(eventInfo.getLongitude());
            assertNull(eventInfo.getRadius());
            assertNull(eventInfo.getGeo());
            assertNull(eventInfo.getDate());
            assertNull(eventInfo.getExpireDate());
        }

        // fields=name,description
        eventInfos = EventInfo.Transformer.transform(events, String.format("%s,%s", EVENT.NAME, EVENT.DESCRIPTION));
        eventIterator = events.iterator();
        eventInfoIterator = eventInfos.iterator();
        while (eventIterator.hasNext() && eventInfoIterator.hasNext()) {
            Event event = eventIterator.next();
            EventInfo eventInfo = eventInfoIterator.next();

            assertEquals(event.getName(), eventInfo.getName());
            assertEquals(event.getDescription(), eventInfo.getDescription());

            assertNull(eventInfo.getId());
            assertNull(eventInfo.getTypeId());
            assertNull(eventInfo.getVisible());
            assertNull(eventInfo.getLatitude());
            assertNull(eventInfo.getLongitude());
            assertNull(eventInfo.getRadius());
            assertNull(eventInfo.getGeo());
            assertNull(eventInfo.getDate());
            assertNull(eventInfo.getExpireDate());
        }

        // fields=name,event_id,description
        eventInfos = EventInfo.Transformer.transform(events, String.format("%s,%s,%s", EVENT.NAME, EVENT.ID, EVENT.DESCRIPTION));
        eventIterator = events.iterator();
        eventInfoIterator = eventInfos.iterator();
        while (eventIterator.hasNext() && eventInfoIterator.hasNext()) {
            Event event = eventIterator.next();
            EventInfo eventInfo = eventInfoIterator.next();

            assertEquals(event.getName(), eventInfo.getName());
            assertEquals(event.getDescription(), eventInfo.getDescription());
            assertEquals(event.getId(), eventInfo.getId());

            assertNull(eventInfo.getTypeId());
            assertNull(eventInfo.getVisible());
            assertNull(eventInfo.getLatitude());
            assertNull(eventInfo.getLongitude());
            assertNull(eventInfo.getRadius());
            assertNull(eventInfo.getGeo());
            assertNull(eventInfo.getDate());
            assertNull(eventInfo.getExpireDate());
        }
    }

    private void assertAllFields(final Event event, final EventInfo eventInfo) {
        assertEquals(event.getId(), eventInfo.getId());
        assertEquals(event.getName(), eventInfo.getName());
        assertEquals(event.getDescription(), eventInfo.getDescription());
        assertEquals(event.getEventType().getId(), eventInfo.getTypeId());
        assertEquals(event.isVisible(), eventInfo.getVisible());
        assertEquals(event.getLatitude(), eventInfo.getLatitude());
        assertEquals(event.getLongitude(), eventInfo.getLongitude());
        assertEquals(event.getRadius(), eventInfo.getRadius());
        assertEquals(event.isGeoServicesEnabled(), eventInfo.getGeo());
        assertEquals(event.getCreated(), eventInfo.getDate());
        assertEquals(event.getExpireDate(), eventInfo.getExpireDate());
    }

    private void assertAllFields(final List<Event> events, final List<EventInfo> eventInfos) {
        Iterator<Event> eventIterator = events.iterator();
        Iterator<EventInfo> eventInfoIterator = eventInfos.iterator();
        while (eventIterator.hasNext() && eventInfoIterator.hasNext()) {
            assertAllFields(eventIterator.next(), eventInfoIterator.next());
        }
    }
}