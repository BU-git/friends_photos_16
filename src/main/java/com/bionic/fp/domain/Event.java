package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@NamedNativeQueries({
    @NamedNativeQuery(name = Event.FIND_BY_RADIUS, resultClass=Event.class, query =
        "SELECT * FROM events as e " +
        "WHERE e.deleted = FALSE " +
            "AND e.geo = TRUE " +
            "AND (:visible is NULL OR e.visible = :visible) " +
            "AND e.lat BETWEEN :latitude - (:radius / :distance_unit) AND :latitude + (:radius / :distance_unit) " +
            "AND e.lng BETWEEN :longitude - (:radius / (:distance_unit * COS(RADIANS(:latitude)))) AND :longitude + (:radius / (:distance_unit * COS(RADIANS(:latitude)))) " +
        "HAVING (:distance_unit * DEGREES(ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(e.lat)) * COS(RADIANS(:longitude - e.lng)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(e.lat))))) < :radius")
})
public class Event extends BaseEntity implements IdEntity<Long> {

    @Transient public static final String FIND_BY_RADIUS = "Event.findByRadius";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;
    /**
     * Is this event visible in the general mode of search?
     */
    private boolean visible = true;
    @Column(name = "expire_date")
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime expireDate;
    @Embedded
    private Coordinate location;
    @Column(name = "geo")
    private boolean geoServicesEnabled = false;
    @Column(name = "private")
    private boolean isPrivate = false;
    private String password;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AccountEvent> accounts;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Photo> photos;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "events_comments",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id", referencedColumnName = "id", unique = true)})
    private List<Comment> comments;

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
    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public LocalDateTime getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
    public Coordinate getLocation() {
        return location;
    }
    public void setLocation(Coordinate coordinate) {
        this.location = coordinate;
    }
    public boolean isGeoServicesEnabled() {
        return geoServicesEnabled;
    }
    public void setGeoServicesEnabled(boolean geoServicesEnabled) {
        this.geoServicesEnabled = geoServicesEnabled;
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
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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
        return location != null ? location.equals(event.location) : event.location == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", eventType=").append(eventType);
        sb.append(", visible=").append(visible);
        sb.append(", date=").append(created);
        sb.append(", expireDate=").append(expireDate);
        sb.append(", location=").append(location);
        sb.append(", geoServicesEnabled=").append(geoServicesEnabled);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }
}
