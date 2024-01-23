INSERT INTO users (first_name, last_name, email, username, password, phone_number)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'john_doe', 'password123', '123-456-7890'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'jane_smith', 'password456', '987-654-3210');


INSERT INTO posts (user_id, title, content, likes_count)
VALUES
    (1, 'Introduction to Forum System', 'This is a forum system introduction post.', 10),
    (2, 'Getting Started with Java', 'Lets discuss Java programming language.', 5);

INSERT INTO comments (user_id, post_id, content)
VALUES
    (1, 1, "Great introduction! Looking forward to participating."),
    (2, 1, "Nice post. I have some questions though.");

INSERT INTO tags (name)
VALUES ('Java'), ('Forum'), ('Programming');

INSERT INTO post_tags (post_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);