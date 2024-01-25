-- Insert data into 'users' table
INSERT INTO users (first_name, last_name, email, username, password, role, status)
VALUES ('John', 'Doe', 'john.doe@example.com', 'johndoe', 'password123', 'USER', 'ACTIVE'),
       ('Alice', 'Smith', 'alice.smith@example.com', 'alicesmith', 'password456', 'USER', 'ACTIVE'),
       ('Admin', 'Adminson', 'admin@example.com', 'admin', 'adminpassword', 'ADMIN', 'ACTIVE');

-- Insert data into 'phone_numbers' table
INSERT INTO phone_numbers (phone_number, user_id)
VALUES ('1234567890', 1),
       ('9876543210', 2),
       ('5555555555', 3);

-- Insert data into 'posts' table
INSERT INTO posts (created_by, title, content, creation_time)
VALUES (1, 'First Post', 'This is the content of the first post.', NOW()),
       (2, 'Second Post', 'Content for the second post goes here.', NOW());

-- Insert data into 'comments' table
INSERT INTO comments (user_id, post_id, content)
VALUES (2, 1, 'Nice post!'),
       (3, 1, 'I disagree with some points.'),
       (1, 2, 'Great content!');

-- Insert data into 'reactions' table
INSERT INTO reactions (type_reaction, user_id, post_id, comment_id)
VALUES ('LIKES', 1, 1, 1),
       ('DISLIKES', 2, 1, 2),
       ('LOVE', 3, 2, 3);



-- Insert data into 'tags' table
INSERT INTO tags (name)
VALUES ('Technology'),
       ('Travel'),
       ('Food');

-- Insert data into 'post_tags' table
INSERT INTO post_tags (post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 3);