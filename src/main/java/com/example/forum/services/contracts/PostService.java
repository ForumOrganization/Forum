package com.example.forum.services.contracts;

import com.example.forum.models.Post;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.utils.PostFilterOptions;

import java.util.List;

public interface PostService {

    List<Post> getAll(PostFilterOptions postFilterOptions);

    List<Post> getTopCommentedPosts();
    long getAllNumber();

    List<Post> getMostRecentPosts();

    Post getById(int id);

    Post getByTitle(String title);

    Post getByComment(int commentId);

    void create(Post post, User user,List<Tag>tags);

    void update(Post post, User user,List<Tag> tags);

    void delete(int id, User user);

    Post reactToPost(int postId, Reaction_posts reactionPostToAdd);

    long countReactionLikes(Post post);

    long countReactionDislikes(Post post);
}