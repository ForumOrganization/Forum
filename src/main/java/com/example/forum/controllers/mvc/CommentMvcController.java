//package com.example.forum.controllers.mvc;
//
//import com.example.forum.exceptions.AuthorizationException;
//import com.example.forum.exceptions.DuplicateEntityException;
//import com.example.forum.exceptions.EntityNotFoundException;
//import com.example.forum.helpers.AuthenticationHelper;
//import com.example.forum.helpers.CommentMapper;
//import com.example.forum.models.Comment;
//import com.example.forum.models.Post;
//import com.example.forum.models.User;
//import com.example.forum.models.dtos.CommentDto;
//import com.example.forum.services.contracts.CommentService;
//import com.example.forum.services.contracts.PostService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/comments")
//
//public class CommentMvcController {
//    private final CommentService commentService;
//    private final PostService postService;
//    private final CommentMapper commentMapper;
//    private final AuthenticationHelper authenticationHelper;
//
//    @Autowired
//    public CommentMvcController(CommentService commentService, PostService postService, CommentMapper commentMapper, AuthenticationHelper authenticationHelper) {
//        this.commentService = commentService;
//        this.postService = postService;
//        this.commentMapper = commentMapper;
//        this.authenticationHelper = authenticationHelper;
//    }
//
//    @ModelAttribute("isAuthenticated")
//    public boolean populateIsAuthenticated(HttpSession session) {
//        return session.getAttribute("currentUser") != null;
//    }
//
//    @ModelAttribute("requestURI")
//    public String requestURI(final HttpServletRequest request) {
//        return request.getRequestURI();
//    }
//
//    @GetMapping("/post/{postId}")
//    public String showAllComments(@PathVariable int postId, Model model) {
//        List<Comment> comments = commentService.getAllCommentsByPostId(postId);
//        model.addAttribute("comments", comments);
//        return "CommentView";
//    }
//
//    @GetMapping("/{id}")
//    public String showSingleComment(@PathVariable int id, Model model, HttpSession session) {
//        try {
//            authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//
//        try {
//            Comment comment = commentService.getCommentById(id);
//            model.addAttribute("comment", comment);
//            return "CommentView";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
//
////    @GetMapping("/new/posts/{postId}")
////    public String showNewCommentPage(@PathVariable("postId") int postId,
////                                     Model model, HttpSession session) {
////        User user;
////        try {
////           user= authenticationHelper.tryGetCurrentUser(session);
////        } catch (AuthorizationException e) {
////            return "redirect:/auth/login";
////        }
////
////        model.addAttribute("comment", new CommentDto());
////        model.addAttribute("postId", postId);
////        model.addAttribute("currentUser", user);
////        return "CommentCreateView";
////    }
////
////    @PostMapping("/new/posts/{postId}")
////    public String createComment(@Valid @ModelAttribute("comment") CommentDto commentDto,
////                                @PathVariable("postId") int postId,
////                                BindingResult bindingResult,
////                                Model model,
////                                HttpSession session) {
////        User user;
////        try {
////            user = authenticationHelper.tryGetCurrentUser(session);
////        } catch (AuthorizationException e) {
////            return "redirect:/auth/login";
////        }
////
////        if (bindingResult.hasErrors()) {
////            return "CommentCreateView";
////        }
////
////        try {
////            Comment comment = commentMapper.fromDto(commentDto);
////            commentService.createComment(comment, postId, user);
////            return "redirect:/posts/" + postId;
////        } catch (EntityNotFoundException e) {
////            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
////            model.addAttribute("error", e.getMessage());
////            return "ErrorView";
////        } catch (DuplicateEntityException e) {
////            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
////            model.addAttribute("error", e.getMessage());
////            return "ErrorView";
////        } catch (AuthorizationException e) {
////            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
////            model.addAttribute("error", e.getMessage());
////            return "ErrorView";
////        }
////
////    }
//
//    @GetMapping("/{commentId}/update")
//    public String showEditCommentPage(@PathVariable int commentId,
////                                      @PathVariable ("postId") int postId,
//                                      Model model, HttpSession session) {
//        User user;
//        try {
//           user= authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//
//        try {
//            Comment comment = commentService.getCommentById(commentId);
//            CommentDto commentDto = commentMapper.toDto(comment);
//            model.addAttribute("commentId", commentId);
//            model.addAttribute("comment", commentDto);
//            model.addAttribute("currentUser", user);
//            return "CommentUpdateView";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
//
//    @PostMapping("/{id}/update")
//    public String updateComment(@PathVariable int id,
//                                @Valid @ModelAttribute("comment") CommentDto dto,
////                                @PathVariable ("postId") int postId,
//                                BindingResult bindingResult,
//                                Model model,
//                                HttpSession session) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//
//        if (bindingResult.hasErrors()) {
//            return "CommentUpdateView";
//        }
//
//        try {
//            Post post = postService.getById(id);
//            Comment comment = commentMapper.fromDto(id, dto,post);
//
//            commentService.updateComment(comment, user);
//            return "redirect:/posts/" + post.getId();
//
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
//
//    @GetMapping("/{id}/delete")
//    public String deleteComment(@PathVariable int id, Model model, HttpSession session) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetCurrentUser(session);
//        } catch (AuthorizationException e) {
//            return "redirect:/auth/login";
//        }
//        try {
//            Post post = postService.getByComment(id);
//            commentService.deleteComment(id, user);
//            return "redirect:/posts/" + post.getId();
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        } catch (AuthorizationException e) {
//            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        }
//    }
//}