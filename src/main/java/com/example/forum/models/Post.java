package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"id", "firstName", "lastName", "email", "role", "status", "deleted"})
    private User createdBy;

    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "creation_time")
    private LocalDate creationTime = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "post",
          //  cascade = CascadeType.REMOVE, orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)

    private Set<Reaction_posts> reactions;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    public Post() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean isDeleted() {
        return Boolean.TRUE.equals(isDeleted);
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Reaction_posts> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction_posts> reactions) {
        this.reactions = reactions;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}