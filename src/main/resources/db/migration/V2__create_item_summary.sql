CREATE TABLE item_summary (
                              id SERIAL PRIMARY KEY,
                              total_items INT NOT NULL,
                              average_price DECIMAL(10,2) NOT NULL
);

INSERT INTO item_summary (total_items, average_price)
SELECT COUNT(*), AVG(price) FROM items;