package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group implements Serializable {
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
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Photo> photos = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY)
    private Account owner;
    @Enumerated(EnumType.STRING)
    @Column(name = "group_type")
    private GroupType groupType;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountGroupConnection> accountConnections = new ArrayList<>();
    private double latitude;
    private double longitude;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public List<AccountGroupConnection> getAccountConnections() {
        return accountConnections;
    }

    public void setAccountConnections(List<AccountGroupConnection> accountConnections) {
        this.accountConnections = accountConnections;
    }

    public void addAccountConnection(AccountGroupConnection accountConnection) {
        accountConnections.add(accountConnection);
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", expireDate=" + expireDate +
                ", groupType='" + groupType + '\'' +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
}
