package com.example.forum.models;

import jakarta.persistence.*;

@Entity
@Table(name = "reactions_comments")
public class Reaction_comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_reaction")
    private com.example.forum.models.enums.Reaction reaction= com.example.forum.models.enums.Reaction.UNDEFINED;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Reaction_comments() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public com.example.forum.models.enums.Reaction getReaction() {
        return reaction;
    }

    public void setReaction(com.example.forum.models.enums.Reaction reaction) {
        this.reaction = reaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
