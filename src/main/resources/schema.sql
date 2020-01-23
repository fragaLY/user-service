CREATE TABLE IF NOT EXISTS USER
(
    id           LONG IDENTITY PRIMARY KEY,
    name         VARCHAR(50),
    email        VARCHAR(50),
    phone        VARCHAR(50),
    password     VARCHAR(50)
);