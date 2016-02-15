package com.bionic.fp.web.rest.dto;

import com.bionic.fp.domain.Photo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.bionic.fp.Constants.RestConstants.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.bionic.fp.util.Checks.check;
import static java.util.stream.Collectors.toList;

/**
 * Created by franky_str on 26.11.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoInfo {

    @JsonProperty(PHOTO.ID)         private Long photoId;
    @JsonProperty(PHOTO.NAME)       private String name;
    @JsonProperty(PARAM.OWNER_ID)   private Long ownerId;
	@JsonProperty(EVENT.ID)         private Long eventId;

    public PhotoInfo() {
    }

    public PhotoInfo(final Photo photo) {
        build(photo);
    }

    public void build(final Photo photo) {
        this.photoId = photo.getId();
        this.name = photo.getName();
        this.ownerId = (photo.getOwner() == null || photo.getOwner().isDeleted()) ? null : photo.getOwner().getId();
        this.eventId = (photo.getEvent() == null || photo.getEvent().isDeleted()) ? null : photo.getEvent().getId();
    }

    public static class Transformer {
        private final Photo photo;
        private final PhotoInfo photoInfo;

        public Transformer(final Photo photo, final PhotoInfo photoInfo) {
            check(photo != null, "The photo is not initialized");
            check(photoInfo != null, "The photoInfo is not initialized");
            this.photo = photo;
            this.photoInfo = photoInfo;
        }

        public Photo getPhoto() {
            return photo;
        }

        public PhotoInfo getPhotoInfo() {
            return photoInfo;
        }

        public static PhotoInfo transform(final Photo photo, final String fields) {
            if(photo == null) {
                return null;
            }
            PhotoInfo dto = new PhotoInfo();
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                consumer.accept(new Transformer(photo, dto));
                return dto;
            }
            dto.build(photo);
            return dto;
        }

        public static List<PhotoInfo> transform(final List<Photo> photos, final String fields) {
            if(photos == null || photos.isEmpty()) {
                return Collections.emptyList();
            }
            Consumer<Transformer> consumer = getConsumer(fields);
            if(consumer != null) {
                return photos.stream().parallel()
                        .map(photo -> {
                            PhotoInfo dto = new PhotoInfo();
                            consumer.accept(new Transformer(photo, dto));
                            return dto;
                        })
                        .collect(toList());
            }
            return photos.stream().parallel()
                    .map(PhotoInfo::new)
                    .collect(toList());
        }

        public static Consumer<Transformer> getConsumer(final String fields) {
            if(StringUtils.isNotEmpty(fields)) {
                Consumer<Transformer> result = addConsumer(fields, PHOTO.ID, null, t -> t.getPhotoInfo().setPhotoId(t.getPhoto().getId()));
                result = addConsumer(fields, PHOTO.NAME, result, t -> t.getPhotoInfo().setName(t.getPhoto().getName()));
                result = addConsumer(fields, PARAM.OWNER_ID, result, t -> {
                    if(t.getPhoto().getOwner() != null && !t.getPhoto().getOwner().isDeleted()) {
                        t.getPhotoInfo().setOwnerId(t.getPhoto().getOwner().getId());
                    }
                });
                result = addConsumer(fields, EVENT.ID, result, t -> {
                    if(t.getPhoto().getEvent() != null && !t.getPhoto().getEvent().isDeleted()) {
                        t.getPhotoInfo().setEventId(t.getPhoto().getEvent().getId());
                    }
                });

                return result;
            }
            return null;
        }

        private static Consumer<Transformer> addConsumer(final String fields, final String field,
                                                         final Consumer<Transformer> result, final Consumer<Transformer> consumer) {
            if(fields.matches("(.*[,\\s]+?|^)" + field +"([,\\s].*|$)")) {
                return result == null ? consumer : result.andThen(consumer);
            }
            return result;
        }
    }

    public Long getPhotoId() {
        return photoId;
    }
    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoInfo photoInfo = (PhotoInfo) o;

        if (photoId != null ? !photoId.equals(photoInfo.photoId) : photoInfo.photoId != null) return false;
        if (name != null ? !name.equals(photoInfo.name) : photoInfo.name != null) return false;
        if (ownerId != null ? !ownerId.equals(photoInfo.ownerId) : photoInfo.ownerId != null) return false;
        return eventId != null ? eventId.equals(photoInfo.eventId) : photoInfo.eventId == null;

    }
}
