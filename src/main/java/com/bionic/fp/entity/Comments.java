package com.bionic.fp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Entity
@Table(name = "comments")
public class Comments implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String text;

    @Column(name = "author_id")
    private Long authorID;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "groups_comments",
                joinColumns = {@JoinColumn(name = "comments_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "Group_id", referencedColumnName = "id")})
    private List<Groups> groups;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "photos_comments",
                joinColumns = {@JoinColumn(name = "comments_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "Photo_id", referencedColumnName = "id")})
    private List<Photos> photos;

    public Comments() {
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", authorID=" + authorID +
                '}';
    }
}
