package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@NamedEntityGraphs({
        @NamedEntityGraph(name="Event.owner", attributeNodes={
                @NamedAttributeNode("owner")}
        ),
        @NamedEntityGraph(name="Event.owner&accounts", attributeNodes={
                @NamedAttributeNode("owner"),
                @NamedAttributeNode("AccountEvent")}
        )
})
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime date;
    @Column(name = "expire_date")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime expireDate;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Photo> photos;
    @OneToOne(fetch = FetchType.LAZY)
    private Account owner;
/*    @Enumerated(EnumType.STRING)
    @Column(name = "group_type")
    private EventType eventType;*/
	@OneToMany(mappedBy = "event")
	private List<AccountEvent> accounts;
    private Double latitude;
    private Double longitude;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    /**
     * Is the group visible in the general mode of search?
     */
    private boolean visible = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(final Account owner) {
        this.owner = owner;
    }
/*
    public EventType getEventType() {
        return eventType;
    }*/

/*    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }*/

	public List<AccountEvent> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountEvent> accounts) {
		this.accounts = accounts;
	}

	public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", expireDate=" + expireDate +
                /*", eventType='" + eventType + '\'' +*/
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
