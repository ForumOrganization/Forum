CREATE TABLE users (
                      user_id INT PRIMARY KEY AUTO_INCREMENT,
                      first_name VARCHAR(32) NOT NULL,
                      last_name VARCHAR(32) NOT NULL,
                      email VARCHAR(50) UNIQUE NOT NULL,
                      username VARCHAR(50) UNIQUE NOT NULL,
                      is_admin BOOLEAN NOT NULL

);

CREATE TABLE phone_numbers (
                        phone_number_id INT PRIMARY KEY AUTO_INCREMENT,
                        phone_number VARCHAR(20) NOT NULL,
                        user_id INT NOT NULL,
                        CONSTRAINT phone_numbers_users_user_id_fk
                            FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE posts (
                      post_id  INT PRIMARY KEY AUTO_INCREMENT,
                      user_id INT NOT NULL,
                      title VARCHAR(64) NOT NULL,
                      content VARCHAR(8192) NOT NULL,
                      likes_count INT DEFAULT 0,
                      CONSTRAINT posts_users_user_id_fk
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE comments (
                         comment_id  INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL,
                         post_id INT NOT NULL,
                         content VARCHAR(8192) NOT NULL,
                         CONSTRAINT comments_users_user_id_fk
                            FOREIGN KEY (user_id) REFERENCES users(user_id),
                         CONSTRAINT comments_posts_post_id_fk
                             FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE tags (
                     tag_id  INT PRIMARY KEY AUTO_INCREMENT,
                     name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE post_tags (
                          post_id INT NOT NULL,
                          tag_id INT NOT NULL,
                          CONSTRAINT post_tags_posts_post_id_fk
                              FOREIGN KEY (post_id) REFERENCES posts(post_id),
                          CONSTRAINT post_tags_tag_tag_id_fk
                              FOREIGN KEY (tag_id) REFERENCES tags(tag_id)
);