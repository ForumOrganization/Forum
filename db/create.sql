CREATE TABLE users
(
    user_id    INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(32)        NOT NULL,
    last_name  VARCHAR(32)        NOT NULL,
    email      VARCHAR(50) UNIQUE NOT NULL,
    username   VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(50)        NOT NULL,
    role       ENUM ('USER', 'ADMIN'),
    is_deleted BOOLEAN DEFAULT FALSE,
    status     ENUM ('BLOCKED', 'ACTIVE')
);

CREATE TABLE phone_numbers
(
    phone_number_id INT PRIMARY KEY AUTO_INCREMENT,
    phone_number    VARCHAR(20) NOT NULL,
    user_id         INT         NOT NULL,
    CONSTRAINT phone_numbers_users_user_id_fk
        FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE posts
(
    post_id       INT PRIMARY KEY AUTO_INCREMENT,
    created_by    INT           NOT NULL,
    title         VARCHAR(64)   NOT NULL,
    content       VARCHAR(8192) NOT NULL,
    creation_time DATE,
    is_deleted    BOOLEAN DEFAULT FALSE,
    CONSTRAINT posts_users_user_id_fk
        FOREIGN KEY (created_by) REFERENCES users (user_id)
);
CREATE TABLE comments
(
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT           NOT NULL,
    post_id    INT           NOT NULL,
    content    VARCHAR(8192) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT comments_users_user_id_fk
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT comments_posts_post_id_fk
        FOREIGN KEY (post_id) REFERENCES posts (post_id)
);
CREATE TABLE reactions_posts
(
    reaction_post_id   INT PRIMARY KEY AUTO_INCREMENT,
    type_reaction ENUM ('LIKES', 'DISLIKES', 'LOVE'),
    user_id       INT NOT NULL,
    post_id       INT NOT NULL,
    CONSTRAINT reactions_posts_users_user_id_fk
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT reactions_posts_posts_post_id_fk
        FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

CREATE TABLE reactions_comments
(
    reaction_comment_id   INT PRIMARY KEY AUTO_INCREMENT,
    type_reaction ENUM ('LIKES', 'DISLIKES', 'LOVE'),
    user_id       INT NOT NULL,
    comment_id    INT NOT NULL,
    CONSTRAINT reactions_comments_users_user_id_fk
        FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT reactions_comments_comments_comment_id_fk
        FOREIGN KEY (comment_id) REFERENCES comments (comment_id)
);


CREATE TABLE tags
(
    tag_id INT PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE post_tags
(
    post_id INT NOT NULL,
    tag_id  INT NOT NULL,
    CONSTRAINT post_tags_posts_post_id_fk
        FOREIGN KEY (post_id) REFERENCES posts (post_id),
    CONSTRAINT post_tags_tag_tag_id_fk
        FOREIGN KEY (tag_id) REFERENCES tags (tag_id)
);