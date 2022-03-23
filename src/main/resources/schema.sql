DROP TABLE IF EXISTS UserRoles;
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password CHAR(255),
    token VARCHAR(255)
);

CREATE TABLE UserRoles (
    user_id INT NOT NULL UNIQUE,
    publisher_token VARCHAR(250) UNIQUE,
    subscriber_token VARCHAR(250) UNIQUE
)