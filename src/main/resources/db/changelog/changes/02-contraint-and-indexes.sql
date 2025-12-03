ALTER TABLE products
ADD CONSTRAINT uq_product_name_category UNIQUE (name, category_id);

CREATE INDEX idx_product_name ON products(name);

ALTER TABLE orders
ADD CONSTRAINT uq_customer_email UNIQUE (customer_email);