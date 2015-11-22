package com.bionic.fp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * Created by boubdyk on 11.11.2015.
 */

@Entity
@Table(name = "groups")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;
    private String description;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "group_type")
    private String groupType;

    private String name;

    @Column(name = "owner_id")
    private Long ownerID;

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "groups")
    private List<AccountsGroups> accounts;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "groups_comments",
                joinColumns = {@JoinColumn(name = "Group_id", referencedColumnName = "id")},
                inverseJoinColumns = {@JoinColumn(name = "comments_id", referencedColumnName = "id")})
    private List<Comment> comments;

    public Group() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public List<AccountsGroups> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountsGroups> accounts) {
        this.accounts = accounts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
