DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       LONG AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(48)  NOT NULL UNIQUE,
    password VARCHAR(128),
    role     VARCHAR(12)  NOT NULL
);