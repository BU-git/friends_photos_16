package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "photos")
public class Photo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime date;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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
                ", date=" + date +
                ", previewURL='" + previewUrl + '\'' +
                ", url='" + url + '\'' +
                ", event=" + event +
                ", owner=" + owner +
                '}';
    }
}
