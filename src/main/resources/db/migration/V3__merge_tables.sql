CREATE TABLE final_items (
                             id SERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             total_items INT NOT NULL,
                             average_price DECIMAL(10,2) NOT NULL
);

INSERT INTO final_items (name, price, total_items, average_price)
SELECT i.name, i.price, s.total_items, s.average_price
FROM items i CROSS JOIN item_summary s;

DROP TABLE items;
DROP TABLE item_summary;
