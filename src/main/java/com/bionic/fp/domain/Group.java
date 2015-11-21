package com.bionic.fp.domain;

import com.bionic.fp.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@NamedEntityGraphs({
        @NamedEntityGraph(name="Group.owner", attributeNodes={
                @NamedAttributeNode("owner")}
        ),
        @NamedEntityGraph(name="Group.owner&accounts", attributeNodes={
                @NamedAttributeNode("owner"),
                @NamedAttributeNode("accountConnections")}
        )
})
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
