package com.example.forum.controllers.mvc;

import com.example.forum.exceptions.AuthorizationException;
import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.helpers.AuthenticationHelper;
import com.example.forum.helpers.CommentMapper;
import com.example.forum.helpers.PostMapper;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.User;
import com.example.forum.models.dtos.CommentDto;
import com.example.forum.models.dtos.PostDto;
import com.example.forum.models.dtos.PostFilterDto;
import com.example.forum.models.enums.Reaction;
import com.example.forum.services.ReactionServiceImpl;
import com.example.forum.services.contracts.CommentService;
import com.example.forum.services.contracts.PostService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

@Controller
@RequestMapping("/comments")

public class CommentMvcController {
    private final CommentService commentService;
    private final PostService postService;
    private final ReactionServiceImpl reactionService;
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public CommentMvcController(CommentService commentService, PostService postService, ReactionServiceImpl reactionService, CommentMapper commentMapper, AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.postService=postService;
        this.reactionService = reactionService;
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
    public String showAllComments(@PathVariable int postId, Model model) {
        List<Comment> comments = commentService.getAllCommentsByPostId(postId);
        model.addAttribute("comments", comments);
        return "CommentsView";
    }

    @GetMapping("/{id}")
    public String showSingleComment(@PathVariable int id, Model model) {
        try {
            Comment comment = commentService.getCommentById(id);
            model.addAttribute("comment", comment);
            return "CommentView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new/posts/{postId}")
    public String showNewCommentPage(  @PathVariable ("postId") int postId,
                                       Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("comment", new CommentDto());
        return "CommentCreateView";
    }

    @PostMapping("/new/posts/{postId}")
    public String createComment(@Valid @ModelAttribute("comment") CommentDto commentDto,
                                @PathVariable ("postId") int postId,
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
            commentService.createComment(comment,postId, user);
            return "redirect:/posts/"+postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{commentId}/update")
    public String showEditCommentPage(@PathVariable int commentId,
//                                      @PathVariable ("postId") int postId,
                                      Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Comment comment = commentService.getCommentById(commentId);
            CommentDto commentDto = commentMapper.toDto(comment);
            model.addAttribute("commentId", commentId);
            model.addAttribute("comment", commentDto);
            return "CommentUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateComment(@PathVariable int id,
                             @Valid @ModelAttribute("comment") CommentDto dto,
//                                @PathVariable ("postId") int postId,
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
            Comment comment = commentMapper.fromDto(id, dto);
            Post post=postService.getByComment(id);
            commentService.updateComment(comment, user);
//            return "redirect:/comments";
            return "redirect:/posts/"+post.getId();

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

    @GetMapping("/{id}/delete")
    public String deleteComment(@PathVariable int id, Model model, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post=postService.getByComment(id);
            commentService.deleteComment(id, user);
            return "redirect:/posts/"+post.getId();
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
//    @GetMapping("/{commentId}/Like")
//    public String viewComment(@PathVariable int commentId, Model model) {
//        try {
//            Comment comment = commentService.getCommentById(commentId);
//            List<Reaction_comments> reactions = reactionService.getAllReactionsByCommentId(commentId);
//
//            model.addAttribute("comment", comment);
//            model.addAttribute("reactions", reactions);
//
//            return "CommentView"; // Replace "commentView" with the name of your view template
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
        @GetMapping("/{commentId}/Like")
        public String likeComment(@PathVariable int commentId,  HttpSession session,Model model) {
            User user;
            try {
                user = authenticationHelper.tryGetCurrentUser(session);
            } catch (AuthorizationException e) {
                return "redirect:/auth/login";
            }
            try {
                Post post=postService.getByComment(commentId);
                Comment comment=commentService.getCommentById(commentId);
                Reaction_comments reaction=new Reaction_comments();
                reaction.setReaction(Reaction.LIKES);
                reaction.setComment(comment);
                reaction.setUser(user);
                reactionService.updateReactionComment(reaction,commentId);
                return "redirect:/posts/"+ post.getId();

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

    @GetMapping("/{commentId}/reaction-count")
    @ResponseBody
    public Map<String,Integer> getReactionCountForComment(@PathVariable int commentId) {
        Map<Reaction_comments, Integer> reactionCountMap = reactionService.countReactionsComment(commentId);
        int likeCount =
                reactionCountMap.getOrDefault(Reaction.LIKES, 0);
        int dislikeCount =
                reactionCountMap.getOrDefault(Reaction.DISLIKES, 0);
        Map<String, Integer> combinedCount = new HashMap<>();
        combinedCount.put("likeCount", likeCount);
        combinedCount.put("dislikeCount", dislikeCount);
        return combinedCount;
    }
}
