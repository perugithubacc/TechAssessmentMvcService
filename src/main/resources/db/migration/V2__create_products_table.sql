CREATE TABLE Products
(
    product_Id          VARCHAR(36)    PRIMARY KEY,
    product_code        VARCHAR(10)   NOT NULL UNIQUE,
    product_name        VARCHAR(50)   NOT NULL,
    product_description TEXT,
    product_price       DECIMAL(18, 2) NOT NULL,
    product_quantity    INT            NOT NULL,
    product_type        VARCHAR(20)   NOT NULL
);

CREATE INDEX product_idx_product_id ON Products (product_Id);