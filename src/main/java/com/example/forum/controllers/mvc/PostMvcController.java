package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.PostMapper;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_posts;
import com.example.forum.models.User;
import com.example.forum.models.dtos.PostDto;
import com.example.forum.models.dtos.PostFilterDto;
import com.example.forum.models.enums.Reaction;
import com.example.forum.services.contracts.CommentService;
import com.example.forum.services.contracts.PostService;
import com.example.forum.services.contracts.ReactionService;
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

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final ReactionService reactionService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostMvcController(PostService postService, CommentService commentService, ReactionService reactionService, PostMapper postMapper,
                             AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.postMapper = postMapper;
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
                               Model model) {
        PostFilterOptions filterOptions = new PostFilterOptions(
                filterDto.getTitle(),
                filterDto.getCreatedBy(),
                filterDto.getCreationTime(),
                filterDto.getSortBy(),
                filterDto.getSortOrder());

        List<Post> posts = postService.getAll(filterOptions);
        model.addAttribute("filterOptions", filterDto);
        model.addAttribute("posts", posts);
        return "PostsView";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
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
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("post", new PostDto());
        return "PostCreateView";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
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
            return "PostCreateView";
        }

        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);
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
    public String showEditPostPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getById(id);
            PostDto postDto = postMapper.toDto(post);
            model.addAttribute("postId", id);
            model.addAttribute("post", postDto);
            return "PostUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id,
                             @Valid @ModelAttribute("post") PostDto dto,
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
            return "PostUpdateView";
        }

        try {
            Post post = postMapper.fromDto(id, dto);
            postService.update(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("title", "duplicate_post", e.getMessage());
            return "PostUpdateView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{id}/delete")
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
}