package com.bionic.fp.web.rest.dto;

import com.bionic.fp.AbstractHelperTest;
import com.bionic.fp.Constants;
import com.bionic.fp.Constants.RestConstants.EVENT;
import com.bionic.fp.Constants.RestConstants.PHOTO;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * todo: comment
 *
 * @author Sergiy Gabriel
 */
public class PhotoTransformerTest extends AbstractHelperTest {

    @Test
    public void testPhotoTransformWithoutFieldsSuccess() throws Exception {
        Photo photo = getNewPhoto();
        photo.setId(123L);

        // fields is null
        PhotoInfo photoInfo = PhotoInfo.Transformer.transform(photo, null);
        assertAllFields(photo, photoInfo);

        // fields is empty
        photoInfo = PhotoInfo.Transformer.transform(photo, "");
        assertAllFields(photo, photoInfo);

        // the fields without the specified properties
        photoInfo = PhotoInfo.Transformer.transform(photo, "I'll,be,back");
        assertAllFields(photo, photoInfo);
    }



    @Test
    public void testPhotosTransformWithoutFieldsSuccess() throws Exception {
        List<Photo> photos = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Photo photo = getNewPhoto();
            photo.setId(123L);
            photos.add(photo);
        }

        // fields is null
        List<PhotoInfo> photoInfos = PhotoInfo.Transformer.transform(photos, null);
        assertAllFields(photos, photoInfos);


        // fields is empty
        photoInfos = PhotoInfo.Transformer.transform(photos, "");
        assertAllFields(photos, photoInfos);

        // the fields without the specified properties
        photoInfos = PhotoInfo.Transformer.transform(photos, "I'll,be,back");
        assertAllFields(photos, photoInfos);
    }

    @Test
    public void testPhotoTransformFieldsSuccess() throws Exception {
        Photo photo = getNewPhoto();
        photo.setId(123L);

        // fields=name
        PhotoInfo photoInfo = PhotoInfo.Transformer.transform(photo, PHOTO.NAME);
        assertEquals(photo.getName(), photoInfo.getName());
        assertNull(photoInfo.getUrl());
        assertNull(photoInfo.getEventId());
        assertNull(photoInfo.getOwnerId());

        // fields=name,description
        photoInfo = PhotoInfo.Transformer.transform(photo, String.format("%s,%s", PHOTO.NAME, PHOTO.URL));
        assertEquals(photo.getName(), photoInfo.getName());
        assertEquals(photo.getUrl(), photoInfo.getUrl());
        assertNull(photoInfo.getEventId());
        assertNull(photoInfo.getOwnerId());

        // fields=name,event_id,description
        photoInfo = PhotoInfo.Transformer.transform(photo, String.format("%s,%s,%s", PHOTO.NAME, EVENT.ID, PHOTO.URL));
        assertEquals(photo.getName(), photoInfo.getName());
        assertEquals(photo.getUrl(), photoInfo.getUrl());
        assertEquals(photo.getEvent().getId(), photoInfo.getEventId());
        assertNull(photoInfo.getOwnerId());
    }

    @Test
    public void testEventsTransformFieldsSuccess() throws Exception {
        List<Photo> photos = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            Photo photo = getNewPhoto();
            photo.setId(123L);
            photos.add(photo);
        }


        // fields=name
        List<PhotoInfo> photoInfos = PhotoInfo.Transformer.transform(photos, EVENT.NAME);
        Iterator<Photo> photoIterator = photos.iterator();
        Iterator<PhotoInfo> photoInfoIterator = photoInfos.iterator();
        while (photoIterator.hasNext() && photoInfoIterator.hasNext()) {
            Photo photo = photoIterator.next();
            PhotoInfo photoInfo = photoInfoIterator.next();

            assertEquals(photo.getName(), photoInfo.getName());
            assertNull(photoInfo.getUrl());
            assertNull(photoInfo.getEventId());
            assertNull(photoInfo.getOwnerId());
        }

        // fields=name,description
        photoInfos = PhotoInfo.Transformer.transform(photos, String.format("%s,%s", PHOTO.NAME, PHOTO.URL));
        photoIterator = photos.iterator();
        photoInfoIterator = photoInfos.iterator();
        while (photoIterator.hasNext() && photoInfoIterator.hasNext()) {
            Photo photo = photoIterator.next();
            PhotoInfo photoInfo = photoInfoIterator.next();

            assertEquals(photo.getName(), photoInfo.getName());
            assertEquals(photo.getUrl(), photoInfo.getUrl());
            assertNull(photoInfo.getEventId());
            assertNull(photoInfo.getOwnerId());
        }

        // fields=name,event_id,description
        photoInfos = PhotoInfo.Transformer.transform(photos, String.format("%s,%s,%s", PHOTO.NAME, EVENT.ID, PHOTO.URL));
        photoIterator = photos.iterator();
        photoInfoIterator = photoInfos.iterator();
        while (photoIterator.hasNext() && photoInfoIterator.hasNext()) {
            Photo photo = photoIterator.next();
            PhotoInfo photoInfo = photoInfoIterator.next();

            assertEquals(photo.getName(), photoInfo.getName());
            assertEquals(photo.getUrl(), photoInfo.getUrl());
            assertEquals(photo.getEvent().getId(), photoInfo.getEventId());
            assertNull(photoInfo.getOwnerId());
        }
    }

    private void assertAllFields(final Photo photo, final PhotoInfo photoInfo) {
        assertEquals(photo.getName(), photoInfo.getName());
        assertEquals(photo.getUrl(), photoInfo.getUrl());
        assertEquals(photo.getEvent().getId(), photoInfo.getEventId());
        assertEquals(photo.getOwner().getId(), photoInfo.getOwnerId());
    }

    private void assertAllFields(final List<Photo> photos, final List<PhotoInfo> photoInfos) {
        Iterator<Photo> photoIterator = photos.iterator();
        Iterator<PhotoInfo> photoInfoIterator = photoInfos.iterator();
        while (photoIterator.hasNext() && photoInfoIterator.hasNext()) {
            assertAllFields(photoIterator.next(), photoInfoIterator.next());
        }
    }
}