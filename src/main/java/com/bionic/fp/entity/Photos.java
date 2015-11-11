package com.bionic.fp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Entity
@Table(name = "photos")
public class Photos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    @Column(name = "preview_url")
    private String previewURL;

    private String url;

    @Column(name = "group_id")
    private Long groupID;

    @Column(name = "owner_id")
    private Long ownerID;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "photos_comments",
                joinColumns = {@JoinColumn(name = "Photo_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "comments_id", referencedColumnName = "id")})
    private List<Comments> comments;

    public Photos() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Photos{" +
                "id=" + id +
                ", date=" + date +
                ", previewURL='" + previewURL + '\'' +
                ", url='" + url + '\'' +
                ", groupID=" + groupID +
                ", ownerID=" + ownerID +
                '}';
    }
}
