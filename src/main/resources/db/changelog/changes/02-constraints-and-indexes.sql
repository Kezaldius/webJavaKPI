ALTER TABLE products
ADD CONSTRAINT uq_product_name_category UNIQUE (name, category_id);

CREATE INDEX idx_product_name ON products(name);

ALTER TABLE orders
ADD CONSTRAINT uq_customer_email UNIQUE (customer_email);

ALTER TABLE orders
ADD COLUMN tracking_code VARCHAR(255);

ALTER TABLE orders
ADD CONSTRAINT uq_order_tracking_code UNIQUE (tracking_code);