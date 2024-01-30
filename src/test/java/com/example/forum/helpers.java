package com.example.forum;

import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Role;
import com.example.forum.models.enums.Status;
import com.example.forum.utils.CommentFilterOptions;

import java.time.LocalDate;

public class helpers {

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setEmail("mock@user.com");
        mockUser.setStatus(Status.ACTIVE);
        mockUser.setPhoneNumber("3333333333");
        mockUser.setRole(Role.ADMIN);

        return mockUser;
    }

    public static Post createMockPost() {
        Post mockPost = new Post();
        mockPost.setId(1);
        mockPost.setTitle("Title");
        mockPost.setContent("Some content");
        mockPost.setCreationTime(LocalDate.now());
        mockPost.setCreatedBy(createMockUser());

        return mockPost;
    }

    public static Comment createMockComment() {
        Comment mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setContent("Some content");
        mockComment.setPost(createMockPost());
        mockComment.setUser(createMockUser());

        return mockComment;
    }

    public static CommentFilterOptions createMockCommentFilterOptions() {
        return new CommentFilterOptions(
                createMockPost(),
                "content",
                "sort",
                "order");
    }
}