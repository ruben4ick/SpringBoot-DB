CREATE TABLE item_summary (
                              id SERIAL PRIMARY KEY,
                              total_items INT,
                              average_price DOUBLE PRECISION
);

INSERT INTO item_summary (total_items, average_price)
SELECT SUM(quantity), AVG(price) FROM items;