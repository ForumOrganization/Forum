INSERT INTO users (first_name, last_name, email, username, password, role, status)
VALUES ('Yoana', 'Maksimova', 'yoana.maksimova@gmmail.com', 'yoana', 'Parola_1', 'ADMIN', 'ACTIVE'),
       ('Siyana', 'Baileva', 'siyana.baileva@gmail.com', 'siyana', 'Parola_2', 'USER', 'ACTIVE'),
       ('Veronika', 'Kashaykova', 'veronika.kashaykova@gmail.com', 'veronika', 'Parola_3', 'USER', 'ACTIVE');

INSERT INTO phone_numbers (phone_number, user_id)
VALUES ('1234567890', 1),
       ('9876543210', 2),
       ('5555555555', 3);

INSERT INTO posts (created_by, title, content, creation_time)
VALUES (1, 'First Post', 'This is the content of the first post.', NOW()),
       (2, 'Second Post', 'Content for the second post goes here.', NOW()),
       (3, 'Third Post', 'This for the third post goes here.', NOW()),
       (1, 'Fourth Post', 'Content for the fourth post goes here.', NOW());

INSERT INTO comments (user_id, post_id, content)
VALUES (2, 1, 'Nice post!'),
       (3, 1, 'I disagree with some points.'),
       (1, 2, 'Great content!');

INSERT INTO reactions_posts (type_reaction, user_id, post_id)
VALUES ('LIKES', 1, 1),
       ('DISLIKES', 2, 1),
       ('LOVE', 3, 2);

INSERT INTO reactions_comments (type_reaction, user_id,comment_id)
VALUES ('LOVE', 2, 2),
       ('DISLIKES', 3, 3),
       ('LIKES', 1, 3);


INSERT INTO tags (name)
VALUES ('Technology'),
       ('Travel'),
       ('Food');

INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3);