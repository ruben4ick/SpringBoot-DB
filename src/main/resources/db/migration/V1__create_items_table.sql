CREATE TABLE items (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       price DECIMAL(10,2) NOT NULL
);

INSERT INTO items (name, price) VALUES
                                    ('Laptop', 1200.50),
                                    ('Smartphone', 699.99),
