package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.Post;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.Reaction_posts;
import com.example.forum.repositories.contracts.PostRepository;
import com.example.forum.utils.PostFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getAll(PostFilterOptions postFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            postFilterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            postFilterOptions.getCreatedBy().ifPresent(value -> {
                filters.add("createdBy.username like :createdBy");
                params.put("createdBy", String.format("%%%s%%", value));
            });

            postFilterOptions.getCreationTime().ifPresent(value -> {
//                LocalDate parsedDate=LocalDate.parse(value.toString(),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
//                String formattedDate=parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                filters.add("creationTime > :creationTime");
                params.put("creationTime", value);
            });

            StringBuilder queryString = new StringBuilder("from Post");

            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            queryString.append(generateOrderBy(postFilterOptions));

            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);


            return query.list();
        }
    }

    @Override
    public long getAllNumber() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post", Post.class);
            return query.list().size();

        }}


    @Override
    public List<Post> getTopCommentedPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "SELECT p FROM Post p " +
                            "ORDER BY size(p.comments) DESC "
                            +
                            "LIMIT 10"
                    , Post.class);

            List<Post> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Posts", "top comments");
            }

            result.sort(Comparator.comparingInt(post -> post.getComments().size()));
            Collections.reverse(result);

            return result;
        }
    }

    @Override
    public List<Post> getMostRecentPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                            "SELECT p FROM Post p " +
                                    "ORDER BY p.id DESC ",
//                                    + "LIMIT 10",
                            Post.class)
//                    "SELECT p FROM Post p " +
//                            "ORDER BY p.creationTime DESC ",
//                    Post.class)
                    .setMaxResults(10);

            List<Post> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Posts", "recent time");
            }

            return result;
        }
    }

    @Override
    public Post getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post as p where p.id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> posts = query.list();

            if (posts.isEmpty()) {
                throw new EntityNotFoundException("Post", "id", String.valueOf(id));
            }

            return posts.get(0);
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);

            List<Post> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return result.get(0);
        }
    }

    @Override
    public Post getByComment(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("select post from Comment c where c.id = :id", Post.class);
            query.setParameter("id", id);

            List<Post> result = query.list();

            if (result.isEmpty()) {
                throw new EntityNotFoundException("Comment", "id", String.valueOf(id));
            }

            return result.get(0);
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            post.setCreationTime(LocalDate.now());
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Post postToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            for (Reaction_posts reaction : postToDelete.getReactions()) {
                session.remove(reaction);
            }

            for (Comment comment : postToDelete.getComments()) {
                for (Reaction_comments reaction : comment.getReactions()) {
                    session.remove(reaction);
                }

                session.remove(comment);
            }

            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    public Post reactToPost(Post currentPost) {
        try (Session session = sessionFactory.openSession()) {
//            Reaction_posts existingReaction = reactionRepository
//                    .findReactionByPostIdAndUserId(currentPost.getId(), reaction.getUser().getId());

            session.beginTransaction();
            session.merge(currentPost);
            session.getTransaction().commit();
        }
        return currentPost;

    }

    private String generateOrderBy(PostFilterOptions postFilterOptions) {
        if (postFilterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (postFilterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "createdBy":
                orderBy = "createdBy.username";
                break;
            case "creationTime":
                orderBy = "creationTime";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (postFilterOptions.getSortOrder().isPresent() && postFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}