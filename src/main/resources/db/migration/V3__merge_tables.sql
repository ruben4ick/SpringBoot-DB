CREATE TABLE final_items (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             quantity INT NOT NULL,
                             price DOUBLE PRECISION NOT NULL,
                             total_items INT NOT NULL,
                             average_price DOUBLE PRECISION NOT NULL
);

INSERT INTO final_items (name, quantity, price, total_items, average_price)
SELECT i.name, i.quantity, i.price, s.total_items, s.average_price
FROM items_test i CROSS JOIN item_summary s;

DROP TABLE item_summary;
DROP TABLE items_test;
