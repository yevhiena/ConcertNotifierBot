create table users (
                       id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       chat_id BIGINT NOT NULL UNIQUE,
                       created_at TIMESTAMP NOT NULL,
                       token VARCHAR(256) NOT NULL
);

create table concerts(
                         id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                         artist VARCHAR(30) NOT NULL ,
                         date DATE NOT NULL,
                         price VARCHAR(15) NOT NULL,
                         place VARCHAR(256) NOT NULL,
                         url TEXT NOT NULL
);

create table user_concert(
                             user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                             concert_id BIGINT NOT NULL REFERENCES  concerts(id) ON DELETE CASCADE,
                             PRIMARY KEY (user_id, concert_id)
)
