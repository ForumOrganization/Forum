//package com.example.forum.controllers.rest;
//
//import com.example.forum.exceptions.AuthorizationException;
//import com.example.forum.exceptions.DuplicateEntityException;
//import com.example.forum.exceptions.EntityNotFoundException;
//import com.example.forum.helpers.AuthenticationHelper;
//import com.example.forum.helpers.PostMapper;
//import com.example.forum.helpers.TagMapper;
//import com.example.forum.models.Post;
//import com.example.forum.models.Tag;
//import com.example.forum.models.User;
//import com.example.forum.models.dtos.PostDto;
//import com.example.forum.models.dtos.TagDto;
//import com.example.forum.services.contracts.PostService;
//import com.example.forum.services.contracts.TagService;
//import com.example.forum.utils.PostFilterOptions;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.models.media.MediaType;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/posts")
//public class PostRestController {
//
//    private final PostService postService;
//    private final TagService tagService;
//    private final AuthenticationHelper authenticationHelper;
//    private final PostMapper postMapper;
//    private final TagMapper tagMapper;
//
//    @Autowired
//    public PostRestController(PostService postService, TagService tagService, AuthenticationHelper authenticationHelper, PostMapper postMapper, TagMapper tagMapper) {
//        this.postService = postService;
//        this.tagService = tagService;
//        this.authenticationHelper = authenticationHelper;
//        this.postMapper = postMapper;
//        this.tagMapper = tagMapper;
//    }
//
//    @GetMapping
//    @Operation(tags ={"Get all posts"},
//            summary = "This method retrieve information for all posts.",
//            description = "This method search for all post. When a person is authorized and there are valid posts, a list with all posts will be presented.",
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "Posts were found successfully"),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to access the posts.")})
//    public List<Post> getAll(@RequestHeader HttpHeaders headers,
//                             @RequestParam(required = false) String title,
//                             @RequestParam(required = false) String createdBy,
//                             @RequestParam(required = false) LocalDate creationTime,
//                             @RequestParam(required = false) String sortBy,
//                             @RequestParam(required = false) String sortOrder) {
//        PostFilterOptions postFilterOptions = new PostFilterOptions(title, createdBy, creationTime, sortBy, sortOrder);
//
//        try {
//            this.authenticationHelper.tryGetUser(headers);
//            return postService.getAll(postFilterOptions);
//        } catch (AuthorizationException e) {
//            try {
//                List<Post> topCommentedPosts = postService.getTopCommentedPosts();
//                List<Post> mostRecentPosts = postService.getMostRecentPosts();
//
//                List<Post> combinedList = new ArrayList<>();
//                combinedList.addAll(topCommentedPosts);
//                combinedList.addAll(mostRecentPosts);
//
//                return combinedList;
//            } catch (EntityNotFoundException exception) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
//            }
//        }
//    }
//
//    @GetMapping("/{id}")
//    @Operation(tags ={"Get a post"},
//            operationId = "id to be searched for",
//            summary = "This method search for a post when id is given.",
//            description = "This method search for a post. A valid id must be given as an input.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts id as a parameters.",
//                    content = @Content(schema = @Schema(implementation = Post.class))),
//            parameters = {@Parameter( name = "postId", description = "path variable", example = "5")},
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post has been found successfully"),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to access this post.")})
//    public Post getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            authenticationHelper.tryGetUser(headers);
//            return this.postService.getById(id);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @GetMapping("/search")
//    @Operation(tags ={"Search for a post"},
//            summary = "This method search for a post when title is given.",
//            description = "This method for a post post. A valid title must be given as an input.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts title parameters.",
//                    content = @Content(schema = @Schema(implementation = Post.class))),
//            parameters = {@Parameter( name = "postTitle", description = "Post title", example = "new post")},
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post has been updated successfully"),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to modify this post.")})
//    public Post getByTitle(@RequestHeader HttpHeaders headers, @RequestParam String title) {
//        try {
//            authenticationHelper.tryGetUser(headers);
//            return this.postService.getByTitle(title);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @PostMapping
//    @Operation(tags ={"Create a post"},
//            summary = "This method create a post when input is given.",
//            description = "This method create a post. A valid object must be given as an input.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts object parameters.",
//                    content = @Content(schema = @Schema(implementation = Post.class))),
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post has been created successfully"),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to create a post.")})
//    public Post create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto, @Valid @RequestBody TagDto tagDto) {
//        try {
//            User user = this.authenticationHelper.tryGetUser(headers);
//            Post post = this.postMapper.fromDto(postDto);
//            Tag tag = this.tagMapper.fromDto(tagDto);
//            postService.create(post, user, tag);
//            return post;
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (DuplicateEntityException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}")
//    @Operation(tags ={"Update a post"},
//            operationId = "id to be updated",
//            summary = "This method update post when id is given.",
//            description = "This method update post. A valid object must be given as an input. An optional feature is that a tag can be attached to the post.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request body that accepts PostDto and TagDto parameters.",
//                    content = @Content(schema = @Schema(implementation = Post.class))),
//            parameters = {@Parameter( name = "postId", description = "path variable", example = "5")},
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post has been updated successfully"),
//                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post with this id was not found."),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to modify this post.")})
//    public Post update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody PostDto postDto,
//                       @PathVariable int tagId, @Valid @RequestBody TagDto tagDto) {
//        try {
//            User user = this.authenticationHelper.tryGetUser(headers);
//            Post post = this.postMapper.fromDto(id, postDto);
//            List <Tag> tags=tagService.getAllTagsByPostId(post.getId());
////            Tag tag=this.tagMapper.fromDto(tagId,tagDto);
//            this.postService.update(post, user,tags);
//            return post;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (DuplicateEntityException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(tags ={"Delete a post"},
//            operationId = "id to be deleted",
//            summary = "This method delete a post when id is given.",
//            description = "This method delete a post. A valid object must be given as an input.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "This is a request that accepts post id",
//                    content = @Content(schema = @Schema(implementation = Post.class))),
//            parameters = {@Parameter( name = "postId", description = "path variable", example = "5")},
//            responses ={@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post has been deleted successfully"),
//                    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Post.class)), description = "The post with this id was not found."),
//                    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Post.class)), description = "You are not allowed to delete this post.")})
//    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User user = this.authenticationHelper.tryGetUser(headers);
//            this.postService.delete(id, user);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (AuthorizationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        }
//    }
//}