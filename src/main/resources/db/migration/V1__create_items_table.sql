CREATE TABLE items_test (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       quantity INT NOT NULL DEFAULT 0,
                       price DOUBLE PRECISION NOT NULL
);




INSERT INTO items_test (name, quantity, price) VALUES
                                              ('Laptop', 10, 1200.50),
                                              ('Smartphone', 5, 699.99);
