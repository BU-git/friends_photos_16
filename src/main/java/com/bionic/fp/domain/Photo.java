package com.bionic.fp.domain;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photos")
@NamedEntityGraph(name = "Photo.comments",
        attributeNodes = @NamedAttributeNode("comments")
)
@NamedQueries({
        @NamedQuery(name = Photo.FIND_COMMENTS,
                query = "SELECT p FROM Photo p JOIN FETCH p.comments WHERE p.id = :photoId")
})
public class Photo extends BaseEntity implements IdEntity<Long> {

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
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account owner;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (name != null ? !name.equals(photo.name) : photo.name != null) return false;
        if (url != null ? !url.equals(photo.url) : photo.url != null) return false;
        return previewUrl != null ? previewUrl.equals(photo.previewUrl) : photo.previewUrl == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (previewUrl != null ? previewUrl.hashCode() : 0);
        return result;
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
