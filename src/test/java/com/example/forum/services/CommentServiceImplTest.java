package com.example.forum.services;

import com.example.forum.repositories.contracts.CommentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository mockRepository;

    @InjectMocks
    CommentServiceImpl commentService;

//    @Test
//    public void get_Should_CallRepository() {
//        CommentFilterOptions mockCommentFilterOptions = new CommentFilterOptions();
//
//        commentService.getAllCommentsByPostId(1, mockCommentFilterOptions);
//
//        Mockito.verify(mockRepository, Mockito.times(1))
//                .getAllCommentsByPostId(1, mockCommentFilterOptions);
//    }
}