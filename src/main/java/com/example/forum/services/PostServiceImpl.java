package com.example.forum.services;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.Tag;
import com.example.forum.models.User;
import com.example.forum.models.enums.Reaction;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.repositories.contracts.ReactionRepository;
import com.example.forum.repositories.contracts.TagRepository;
import com.example.forum.services.contracts.PostService;
import com.example.forum.utils.PostFilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.forum.utils.CheckPermission.checkAccessPermissions;
import static com.example.forum.utils.CheckPermission.checkAccessPermissionsUser;
import static com.example.forum.utils.Messages.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ReactionRepository reactionRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.reactionRepository = reactionRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Post> getAll(PostFilterOptions postFilterOptions) {
        return this.postRepository.getAll(postFilterOptions);
    }

    @Override
    public List<Post> getTopCommentedPosts() {
        return this.postRepository.getTopCommentedPosts();

    }

    @Override
    public long getAllNumber() {
        return postRepository.getAllNumber();
    }

    @Override
    public List<Post> getMostRecentPosts() {
        return this.postRepository.getMostRecentPosts();
    }

    @Override
    public Post getById(int id) {
        return this.postRepository.getById(id);
    }

    @Override
    public Post getByTitle(String title) {
        return this.postRepository.getByTitle(title);
    }

    @Override
    public Post getByComment(int commentId) {
        return this.postRepository.getByComment(commentId);
    }

    @Override
    public void create(Post post, User user, List<Tag> tags) {
        checkBlockOrDeleteUser(user);


        boolean duplicateExists = true;

        try {
            Post existingPost = this.postRepository.getByTitle(post.getTitle());

            if (existingPost.getTitle().equals(post.getTitle())) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "id", String.valueOf(post.getId()));
        }

        post.setCreatedBy(user);
        if(tags.isEmpty()){
            this.postRepository.create(post);
        }else {
//            List<Tag> existingTags = tagRepository.getAllTags();
            this.postRepository.create(post);
            for (Tag tag : tags) {
//                if (!existingTags.contains(tag)) {
                    tagRepository.createTagInPost(tag, post.getId(), user);


            }
        }

    }

    @Override
    public void update(Post post, User user, List<Tag> tags) {
        checkBlockOrDeleteUser(user);
        checkAccessPermissionsUser(post.getCreatedBy().getId(), user, UPDATE_USER_MESSAGE_ERROR);

        boolean duplicateExists = true;

        try {
            Post existingPost = this.postRepository.getById(post.getId());

            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }

        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Post", "id", String.valueOf(post.getId()));
        }
        List<Tag> existingAllTags=tagRepository.getAllTags();
        List <Tag> existingTagsInPost=tagRepository.getAllTagsByPostId(post.getId());
        for (Tag tag : existingTagsInPost) {
            for(Tag newTag : tags){
                if(!tag.getName().contains(newTag.getName())){
                    Tag tagToBeDelete=tagRepository.getTagByName(tag.getName());
                    tagRepository.deleteTagInPost(post.getId(),tagToBeDelete.getId());
                }else if(!newTag.getName().contains(tag.getName())){
                    tagRepository.createTagInPost(newTag, post.getId(), user);
                }else{
                    postRepository.update(post);

                }
            }

        }
    }

    @Override
    public void delete(int id, User user) {
        checkBlockOrDeleteUser(user);
        Post post = postRepository.getById(id);
        checkAccessPermissions(post.getCreatedBy().getId(), user, MODIFY_POST_ERROR_MESSAGE);
        this.postRepository.delete(id);
    }

    public Post reactToPost(int postId, Reaction_posts reactionPostToAdd) {
        Post currentPost = getById(postId);
        User user = reactionPostToAdd.getUser();
        Optional<Reaction_posts> reaction = currentPost.getReactions().stream().filter(reactionPosts -> reactionPosts.getUser().equals(user)).findFirst();
        if (reaction.isPresent()) {
            reaction.get().setReaction(reactionPostToAdd.getReaction());
            reactionRepository.updateReactionPost(reactionPostToAdd, currentPost.getId());

        } else {
            reactionPostToAdd.setPost(currentPost);
            reactionRepository.updateReactionPost(reactionPostToAdd, currentPost.getId());
        }
        return postRepository.reactToPost(currentPost);

    }

    public long countReactionLikes(Post post) {
        return post.getReactions().stream()
                .filter(r -> r.getReaction().equals(Reaction.LIKES))
                .count();


    }

    public long countReactionDislikes(Post post) {
        return post.getReactions().stream()
                .filter(r -> r.getReaction().equals(Reaction.DISLIKES))
                .count();


    }

    private static void checkBlockOrDeleteUser(User user) {
        if (user.getStatus() == Status.BLOCKED || user.isDeleted()) {
            throw new AuthorizationException(USER_HAS_BEEN_BLOCKED_OR_DELETED);
        }
    }


}