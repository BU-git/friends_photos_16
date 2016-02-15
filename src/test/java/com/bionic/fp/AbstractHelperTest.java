package com.bionic.fp;

import com.bionic.fp.domain.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class AbstractHelperTest {

    //////////////////////////////////////////////
    //                 ACCOUNT                  //
    //////////////////////////////////////////////


    protected Account getNewEmailAccount() {
        return new Account(generateEmail(), generateUsername(), generatePassword());
    }

    protected Account fb(final Account account) {
        account.setFbId("fb" + String.valueOf(System.currentTimeMillis()));
        account.setProfileImageUrl(String.format("https://www.facebook.com/%s.jpg", account.getFbId()));
        account.setFbProfileUrl(String.format("https://www.facebook.com/%s", account.getFbId()));
        account.setFbToken(String.format("T#%s", account.getFbId()));
        return account;
    }

    protected Account vk(final Account account) {
        account.setVkId("vk" + String.valueOf(System.currentTimeMillis()));
        account.setProfileImageUrl(String.format("https://www.vk.com/%s.jpg", account.getVkId()));
        account.setVkProfileUrl(String.format("https://www.vk.com/%s", account.getVkId()));
        account.setVkToken(String.format("T#%s", account.getVkId()));
        return account;
    }

    protected String generateEmail() {
        return String.format("yaya%d@gmail.com", System.currentTimeMillis());
    }

    protected String generateUsername() {
        return String.format("yaya%d", System.currentTimeMillis());
    }

    protected String generatePassword() {
        return String.format("secret%d", System.currentTimeMillis());
    }


    //////////////////////////////////////////////
    //                  EVENT                   //
    //////////////////////////////////////////////


    protected Event getNewEventMin() {
        Event event = new Event();
        event.setName("Name is " + System.currentTimeMillis());
        event.setDescription("Today is " + System.currentTimeMillis());
        event.setEventType(getPrivateEventType());

        assertFalse(event.isDeleted());
        assertTrue(event.isVisible());
        assertFalse(event.isGeoServicesEnabled());

        return event;
    }

    protected Event getNewEventMax() {
        Event event = getNewEventMin();
        event.setVisible(true);
        event.setLocation(getNewCoordinate());
        event.setGeoServicesEnabled(true);

        assertFalse(event.isDeleted());

        return event;
    }

    protected Coordinate getNewCoordinate() {
        Random random = new Random();
        return new Coordinate(random.nextDouble(), random.nextDouble());
    }

    protected EventType getPrivateEventType() {
        EventType eventType = new EventType();
        eventType.setId(1L);
        eventType.setTypeName("PRIVATE");
        return eventType;
    }


    //////////////////////////////////////////////
    //                  PHOTO                   //
    //////////////////////////////////////////////


    protected Photo getNewPhoto() {
        Photo photo = new Photo();
        Event event = new Event();
        event.setId(System.currentTimeMillis());
        Account owner = new Account();
        owner.setId(System.currentTimeMillis());
        LocalDateTime now = LocalDateTime.now();
        photo.setName("photo" + now.getNano());
        photo.setUrl("http://" + now.getNano());
        photo.setEvent(event);
        photo.setOwner(owner);
        photo.setCreated(now);
        return photo;
    }

    protected Photo getNewPhoto(final Event event, final Account owner) {
        Photo photo = new Photo();
        photo.setName("photo" + System.currentTimeMillis());
        photo.setUrl(String.format("/fp/%d/%d/%s", event.getId(), owner.getId(), photo.getName()));
        photo.setEvent(event);
        photo.setOwner(owner);

        assertNull(photo.getCreated());
        assertNull(photo.getModified());
        assertNull(photo.getId());
        assertFalse(photo.isDeleted());

        return photo;
    }


    //////////////////////////////////////////////
    //                 COMMENT                  //
    //////////////////////////////////////////////


    protected Comment getNewComment(final Account author) {
        Comment comment = new Comment();
        comment.setText("Some test" + System.currentTimeMillis());
        comment.setAuthor(author);
        return comment;
    }


    //////////////////////////////////////////////
    //                  OTHER                   //
    //////////////////////////////////////////////


    protected void assertEqualsDate(final LocalDateTime expected, final LocalDateTime actual) {
        assertTrue(Duration.between(expected, actual).getSeconds() < 1L);
    }

    protected  <T extends BaseEntity & IdEntity<Long>> void assertEqualsEntities(final List<T> actual, final T ... expected) {
        if(actual == null) {
            fail("actual == null");
        }
        if(expected == null && !actual.isEmpty()) {
            fail("expected == null && !actual.isEmpty()");
        }
        if(expected == null) {
            fail("expected == null");
        }
        if(expected.length != actual.size()) {
            fail(String.format("expected.length[%d] != actual.size()[%d]", expected.length, actual.size()));
        }
        for (T entity : expected) {
            Optional<T> optional = actual.stream().parallel()
                    .filter(e -> entity.getId().equals(e.getId())).findFirst();
            if(optional.isPresent()) {
                assertEqualsEntity(entity, optional.get());
            } else {
                fail();
            }
        }
    }

    protected <T extends BaseEntity & IdEntity<Long>> void assertEqualsEntity(T expected, T actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEqualsDate(expected.getCreated(), actual.getCreated());
        assertEquals(expected, actual);
    }

    /**
     * Return a file from resources by file name.
     * If file name is empty or null then it returns path to resources of the application
     *
     * @param file the file
     * @return a file
     */
    protected static File getFileFromResources(String file) {
        if(StringUtils.isEmpty(file)) {
            file = "";
        }
        URL resource = AbstractHelperTest.class.getClassLoader().getResource(file);
        if(resource != null) {
            try {
                return Paths.get(resource.toURI()).toFile();
            } catch (URISyntaxException e) {
                throw new RuntimeException("The file " + file + " is not found in the resources");
            }
        }
        return null;
    }
}
