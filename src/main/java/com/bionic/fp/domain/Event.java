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
        @NamedEntityGraph(name = "Event.accounts",
                attributeNodes = @NamedAttributeNode(value = "accounts", subgraph = "accounts"),
                subgraphs = @NamedSubgraph(name = "accounts", attributeNodes = @NamedAttributeNode("account"))),
        @NamedEntityGraph(name="Event.photos", attributeNodes={
                @NamedAttributeNode("photos")}),
        @NamedEntityGraph(name="Event.comments", attributeNodes={
                @NamedAttributeNode("comments")})
})
@NamedQueries({
        @NamedQuery(
                name = Event.FIND_BY_NAME_AND_DESCRIPTION,
                query = "SELECT e FROM Event e WHERE e.visible = TRUE AND e.name LIKE :name AND e.description LIKE :description"
        ),
        @NamedQuery(
                name = Event.FIND_BY_NAME,
                query = "SELECT e FROM Event e WHERE e.visible = TRUE AND e.name LIKE :name "
        ),
        @NamedQuery(
                name = Event.FIND_BY_DESCRIPTION,
                query = "SELECT e FROM Event e WHERE e.visible = TRUE AND e.description LIKE :description"
        ),
        @NamedQuery(
                name = Event.FIND_ALL,
                query = "SELECT e FROM Event e WHERE e.visible = TRUE"
        )
})
public class Event implements Serializable {
    @Transient
    public static final String FIND_BY_NAME_AND_DESCRIPTION = "Event.findByNameAndDescription";
    @Transient
    public static final String FIND_BY_NAME = "Event.findByName";
    @Transient
    public static final String FIND_BY_DESCRIPTION = "Event.findByDescription";
    @Transient
    public static final String FIND_ALL = "Event.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "event_type")
    private EventType eventType;
    @OneToOne(fetch = FetchType.EAGER)
    private Account owner;
    /**
     * Is this event visible in the general mode of search?
     */
    private boolean visible = true;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime date;
    @Column(name = "expire_date")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime expireDate;
    @Column(name = "lat")
    private Double latitude;
    @Column(name = "lng")
    private Double longitude;
    private Float radius;
    @Column(name = "geo")
    private boolean geoServicesEnabled = false;
    @Column(name = "private")
    private boolean isPrivate = false;
    private String password;
    /**
     * Is this event deleted? Because the event is not deleted physically
     */
    private boolean deleted = false;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountEvent> accounts = new ArrayList<>();
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "events_comments",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    private List<Comment> comments = new ArrayList<>();

    public Event() {
    }

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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public boolean isGeoServicesEnabled() {
        return geoServicesEnabled;
    }

    public void setGeoServicesEnabled(boolean geoServicesEnabled) {
        this.geoServicesEnabled = geoServicesEnabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<AccountEvent> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEvent> accounts) {
        this.accounts = accounts;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (description != null ? !description.equals(event.description) : event.description != null) return false;
        if (eventType != null ? !eventType.equals(event.eventType) : event.eventType != null) return false;
        if (latitude != null ? !latitude.equals(event.latitude) : event.latitude != null) return false;
        if (longitude != null ? !longitude.equals(event.longitude) : event.longitude != null) return false;
        return !(radius != null ? !radius.equals(event.radius) : event.radius != null);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", eventType=").append(eventType);
        sb.append(", owner=").append(owner);
        sb.append(", visible=").append(visible);
        sb.append(", date=").append(date);
        sb.append(", expireDate=").append(expireDate);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", radius=").append(radius);
        sb.append(", geoServicesEnabled=").append(geoServicesEnabled);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }
}
