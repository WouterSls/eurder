CREATE TABLE customers
(
    id            bigint PRIMARY KEY,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    email_address VARCHAR(255),
    address       VARCHAR(255),
    phone_number  VARCHAR(20),
    role          VARCHAR(20),
    password      VARCHAR(255)
);

create sequence customer_seq start with 1 increment by 1;