package com.example.forum.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "comment_id")
    private int id;

    @Column(name = "content")
    private String content;
    @JsonIgnore
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    private Set<Reaction_comments> reactions;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


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
    public Boolean getDeleted() {
        return isDeleted;
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

    public Set<Reaction_comments> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction_comments> reactions) {
        this.reactions = reactions;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


}