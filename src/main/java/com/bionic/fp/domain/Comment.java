package com.bionic.fp.domain;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity implements IdEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    private Account author;

    public Comment() {}

    public Comment(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getAuthor() {
        return author;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "id=" + id +
                ", date=" + created +
                ", text='" + text + '\'' +
                ", author=" + author +
                '}';
    }
}
