package com.bionic.fp.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photos")
@NamedEntityGraph(name = "Photo.comments",
        attributeNodes = @NamedAttributeNode("comments")
)
@NamedQueries({
        @NamedQuery(name = Photo.FIND_BY_OWNER_ID,
                query = "SELECT p FROM Photo p WHERE p.owner.id = :ownerId"),
        @NamedQuery(name = Photo.FIND_BY_EVENT_ID,
                query = "SELECT p FROM Photo p WHERE p.event.id = :eventId"),
        @NamedQuery(name = Photo.FIND_COMMENTS,
                query = "SELECT p FROM Photo p JOIN FETCH p.comments WHERE p.id = :photoId")
})
public class Photo extends BaseEntity {

    @Transient public static final String FIND_BY_OWNER_ID = "Photo.findByOwnerId";
    @Transient public static final String FIND_BY_EVENT_ID = "Photo.findByEventId";
    @Transient public static final String FIND_COMMENTS = "Photo.findComments";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(name = "preview_url")
    private String previewUrl;
    @OneToOne(fetch = FetchType.LAZY)
    private Event event;
    @OneToOne(fetch = FetchType.LAZY)
    private Account owner;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "photos_comments",
            joinColumns = {@JoinColumn(name = "photo_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    private List<Comment> comments = new ArrayList<>();

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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPreviewUrl() {
        return previewUrl;
    }
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public Account getOwner() {
        return owner;
    }
    public void setOwner(Account owner) {
        this.owner = owner;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", date=" + created +
                ", previewURL='" + previewUrl + '\'' +
                ", url='" + url + '\'' +
                ", event=" + event +
                ", owner=" + owner +
                '}';
    }
}
