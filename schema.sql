CREATE TABLE category (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE items (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      quantity INTEGER NOT NULL,
                      price NUMERIC NOT NULL,
                      category_id INTEGER,
                      CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category(id)
);
