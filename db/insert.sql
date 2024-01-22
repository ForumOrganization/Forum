INSERT INTO user (first_name, last_name, email, username, password, phone_number)
VALUES
    ('John', 'Doe', 'john.doe@example.com', 'john_doe', 'password123', '123-456-7890'),
    ('Jane', 'Smith', 'jane.smith@example.com', 'jane_smith', 'password456', '987-654-3210');

INSERT INTO admin (first_name, last_name, email, phone_number)
VALUES
    ('Admin', 'User', 'admin@example.com', '555-123-4567');

INSERT INTO post (user_id, title, content, likes_count)
VALUES
    (1, 'Introduction to Forum System', 'This is a forum system introduction post.', 10),
    (2, 'Getting Started with Java', 'Lets discuss Java programming language.', 5);

INSERT INTO comment (user_id, post_id, content)
VALUES
    (1, 1, "Great introduction! Looking forward to participating."),
    (2, 1, "Nice post. I have some questions though.");

INSERT INTO tag (name)
VALUES ('Java'), ('Forum'), ('Programming');

INSERT INTO post_tag (post_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);
