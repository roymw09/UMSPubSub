DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id VARCHAR(250) UNIQUE,
    role VARCHAR(50),
    description VARCHAR(250),
    refresh_token VARCHAR(250) UNIQUE
)