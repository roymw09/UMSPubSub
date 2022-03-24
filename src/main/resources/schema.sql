DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password CHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id VARCHAR(250) UNIQUE,
    role VARCHAR(50),
    description VARCHAR(250)
)