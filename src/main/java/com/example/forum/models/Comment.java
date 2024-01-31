package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @Column(name = "content")
    private String content;

    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    // @JsonIgnoreProperties({"id", "firstName", "lastName", "email", "role", "status", "deleted"})
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    //@JsonIgnoreProperties({"id", "content", "createdBy", "creationTime", "deleted"})
    private Post post;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    private Set<Reaction_comments> reactions;

    public Comment() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonIgnore
    public Boolean isDeleted() {
        return Boolean.TRUE.equals(isDeleted);
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Set<Reaction_comments> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction_comments> reactions) {
        this.reactions = reactions;
    }
}