package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.helpers.PostMapper;
import com.example.forum.helpers.TagMapper;
import com.example.forum.models.*;
import com.example.forum.models.dtos.CommentDto;
import com.example.forum.models.dtos.PostDto;
import com.example.forum.models.dtos.PostFilterDto;
import com.example.forum.models.dtos.TagListDto;
import com.example.forum.models.enums.Reaction;
import com.example.forum.services.contracts.CommentService;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.ReactionService;
import com.example.forum.services.contracts.TagService;
import com.example.forum.utils.PostFilterOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final ReactionService reactionService;
    private final TagService tagService;
    private final PostMapper postMapper;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostMvcController(PostService postService, CommentService commentService, ReactionService reactionService, TagService tagService, PostMapper postMapper, TagMapper tagMapper, CommentMapper commentMapper,
                             AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.tagService = tagService;
        this.postMapper = postMapper;
        this.tagMapper = tagMapper;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterOptions") PostFilterDto filterDto,
                               Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if (user != null) {
                model.addAttribute("currentUser", user);
            }
            PostFilterOptions filterOptions = new PostFilterOptions(
                    filterDto.getTitle(),
                    filterDto.getCreatedBy(),
                    filterDto.getCreationTime(),
                    filterDto.getSortBy(),
                    filterDto.getSortOrder());
            List<Post> posts = postService.getAll(filterOptions);
            List<List<Tag>> tagsList = new ArrayList<>();
            for (Post post : posts) {
                List<Tag> tags = tagService.getAllTagsByPostId(post.getId());
                tagsList.add(tags);
            }
            model.addAttribute("filterOptions", filterDto);
            model.addAttribute("posts", posts);
            model.addAttribute("tagsList", tagsList);
            return "PostsView";
        } catch (AuthorizationException e) {
            return "PostsView";
        }
    }


    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            List<Comment> comments = commentService.getAllCommentsByPostId(id);

            long likeCount = postService.countReactionLikes(post);
            long dislikeCount = postService.countReactionDislikes(post);

            model.addAttribute("likeCount", likeCount);
            model.addAttribute("dislikeCount", dislikeCount);
            model.addAttribute("postId", id);
            model.addAttribute("post", post);
            model.addAttribute("currentUser", user);

            if (!comments.isEmpty()) {
                model.addAttribute("comments", comments);
            }

            return "PostView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String showNewPostPage(Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
//        List<String> dummy= Arrays.asList("Tag 1", "Tag2");
//        TagListDto newTags=new TagListDto();
//        newTags.setNames(dummy);
        List<Tag> allTags = tagService.getAllTags();
        TagListDto newTags = new TagListDto();
        newTags.setNames(new ArrayList<>());


        model.addAttribute("post", new PostDto());
        model.addAttribute("allTags", allTags);
        model.addAttribute("newTags", newTags);
        model.addAttribute("currentUser", user);
        return "PostCreateView";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult postBindingResult,
                             @Valid @ModelAttribute("newTags") TagListDto tagDtos,
                             BindingResult tagBindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (postBindingResult.hasErrors() || tagBindingResult.hasErrors()) {
            return "PostCreateView";
        }

        try {
            Post post = postMapper.fromDto(postDto);
            List<Tag> newTags = tagMapper.fromListDto(tagDtos);
            postService.create(post, user, newTags);
            model.addAttribute("newTags", newTags);

            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditPostPage(@PathVariable int id,
                                   Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        try {
            Post post = postService.getById(id);
            PostDto postDto = postMapper.toDto(post);
            List<Tag> allTags = tagService.getAllTags();
            TagListDto newTags = new TagListDto();
            List<Tag> postTags = tagService.getAllTagsByPostId(post.getId());
            for (Tag tag : postTags) {
                newTags.getNames().add(tag.getName());
            }

            model.addAttribute("postId", id);
            model.addAttribute("post", postDto);
            model.addAttribute("allTags", allTags);
            model.addAttribute("newTags", newTags);
            model.addAttribute("currentUser", user);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id,
                             @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult postBindingResult,
                             @Valid @ModelAttribute("newTags") TagListDto tagDtos,
                             BindingResult tagsBindingResult,
                             Model model,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (postBindingResult.hasErrors() || tagsBindingResult.hasErrors()) {
            return "PostUpdateView";
        }

        try {
            Post post = postMapper.fromDto(id, postDto);
            List<Tag> newTags = tagMapper.fromListDto(tagDtos);

            postService.update(post, user, newTags);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            postBindingResult.rejectValue("title", "duplicate_post", e.getMessage());
            return "PostUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
    public String showDeletePage(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/most-recent-posts")
    public String showMostRecentPosts(Model model) {
        List<Post> mostRecentPosts = postService.getMostRecentPosts();
        model.addAttribute("posts", mostRecentPosts);
        return "TopRecentPostsView";
    }

    @GetMapping("/most-commented-posts")
    public String showMostCommentedPosts(Model model) {
        List<Post> mostCommentedPosts = postService.getTopCommentedPosts();
        model.addAttribute("posts", mostCommentedPosts);
        return "MostCommentedPostsView";
    }

    @GetMapping("/{postId}/like")
    public String likeComment(@PathVariable int postId, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        Reaction_posts reaction = reactionService.findReactionByPostIdAndUserId(postId, user.getId());

        if (reaction != null && reaction.getReaction().equals(Reaction.LIKES)) {
            reactionService.deleteReactionPost(reaction.getId(), user);
            return "redirect:/posts/" + postId;
        }

        Reaction_posts reactionPost = new Reaction_posts();
        reactionPost.setReaction(Reaction.LIKES);
        reactionPost.setUser(user);
        postService.reactToPost(postId, reactionPost);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}/dislike")
    public String dislikeComment(@PathVariable int postId, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        Reaction_posts reaction = reactionService.findReactionByPostIdAndUserId(postId, user.getId());

        if (reaction != null && reaction.getReaction().equals(Reaction.DISLIKES)) {
            reactionService.deleteReactionPost(reaction.getId(), user);
            return "redirect:/posts/" + postId;
        }

        Reaction_posts reactionPost = new Reaction_posts();
        reactionPost.setReaction(Reaction.DISLIKES);
        reactionPost.setUser(user);
        postService.reactToPost(postId, reactionPost);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/{postId}/newComment")
    public String showNewCommentPage(@PathVariable("postId") int postId,
                                     Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("comment", new CommentDto());
        model.addAttribute("postId", postId);
        model.addAttribute("currentUser", user);
        return "CommentCreateView";
    }

    @PostMapping("/{postId}/newComment")
    public String createComment(@PathVariable("postId") int postId,
                                @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CommentCreateView";
        }

        try {
            Comment comment = commentMapper.fromDto(commentDto);
            commentService.createComment(comment, postId, user);
            return "redirect:/posts/" + postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }

    }

    @GetMapping("/{postId}/comments/{commentId}/update")
    public String showEditCommentPage(@PathVariable int commentId,
                                      @PathVariable int postId,
                                      Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }


        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentDto commentDto = commentMapper.toDto(comment);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", commentDto);
            model.addAttribute("currentUser", user);
            return "CommentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{postId}/comments/{commentId}/update")
    public String updateComment(@PathVariable int postId,
                                @PathVariable int commentId,
                                @Valid @ModelAttribute("comment") CommentDto dto,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "CommentUpdateView";
        }

        try {
            Post existingPost = postService.getById(postId);
            Comment comment = commentMapper.fromDto(commentId, dto, existingPost);
            commentService.updateComment(comment, user);
            return "redirect:/posts/" + existingPost.getId();

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable int postId,
                                @PathVariable int commentId,
                                Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            Post post = postService.getById(postId);
            commentService.deleteComment(commentId, user);
            return "redirect:/posts/" + post.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}