DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TYPE IF EXISTS userrole;
/*
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);*/

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id VARCHAR(250) UNIQUE,
    role VARCHAR(50),
    description VARCHAR(250)
)