package com.example.forum.models.dtos;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CommentDto {
    @NotNull(message = "Comment content can't be empty")
    @Size(min = 32, max = 8192, message = "The comment should be between 32 and 8192 symbols long.")
    private String content;

//    @Positive(message = "User id can't be negative")
//    @NotNull(message = "User id can't be empty")
//    private int userId;
//    @Positive(message = "Post id can't be negative")
//    @NotNull(message = "Post id can't be empty")
//    private int postId;

//    private boolean isDeleted = false;

    public CommentDto(String content, int usertId, int postId, boolean isDeleted) {
        this.content = content;
      //  this.userId = usertId;
       // this.postId = postId;
      //  this.isDeleted = isDeleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public int getUsertId() {
//        return userId;
//    }
//
//    public void setUsertId(int usertId) {
//        this.userId = usertId;
//    }

//    public int getPostId() {
//        return postId;
//    }
//
//    public void setPostId(int postId) {
//        this.postId = postId;
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }


}
