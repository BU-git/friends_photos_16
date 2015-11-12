package com.bionic.fp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private Date date;
    @Column(name = "expire_date")
    private Date expireDate;
    @OneToMany(mappedBy = "group")
    private List<Photo> photos;
    @OneToOne
    private Account owner;
    @Enumerated(EnumType.STRING)
    @Column(name = "group_type")
    private GroupType groupType;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<AccountGroupConnection> accountConnections;
    private double latitude;
    private double longitude;

    @OneToMany
    private List<Comment> comments;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
