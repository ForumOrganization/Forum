package com.example.forum.models;

import com.example.forum.models.enums.Reaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "reactions_posts")
public class Reaction_posts {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_post_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_reaction")
    private Reaction reaction = Reaction.UNDEFINED;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"id", "firstName", "lastName", "email", "role", "status", "isDeleted"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties({"id", "content", "createdBy", "creationTime",  "isDeleted"})
    private Post post;

    public Reaction_posts() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public void setReaction(Reaction reaction) {
        this.reaction = reaction;
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
}