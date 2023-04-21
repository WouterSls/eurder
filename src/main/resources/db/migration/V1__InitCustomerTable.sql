CREATE TABLE customers
(
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    email_address VARCHAR(255),
    address       VARCHAR(255),
    phone_number  VARCHAR(20),
    role          VARCHAR(20),
    password      VARCHAR(255)
);