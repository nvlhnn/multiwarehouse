-- Drop schema if exists
DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create ENUM for order status
DROP TYPE IF EXISTS "order".order_status;
CREATE TYPE "order".order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING');

-- Create customers table
DROP TABLE IF EXISTS "order".customers CASCADE;
CREATE TABLE "order".customers (
    id uuid PRIMARY KEY
);

-- Create warehouses table
DROP TABLE IF EXISTS "order".warehouses CASCADE;
CREATE TABLE "order".warehouses (
    id uuid PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    city VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

-- Create products table
DROP TABLE IF EXISTS "order".products CASCADE;
CREATE TABLE "order".products (
    id uuid PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price numeric(10, 2) NOT NULL
);

-- Create stocks table
DROP TABLE IF EXISTS "order".stocks CASCADE;
CREATE TABLE "order".stocks (
    id uuid PRIMARY KEY,
    warehouse_id uuid NOT NULL,
    product_id uuid NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT fk_warehouse FOREIGN KEY (warehouse_id) REFERENCES "order".warehouses(id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES "order".products(id) ON DELETE CASCADE
);

-- Create orders table
DROP TABLE IF EXISTS "order".orders CASCADE;
CREATE TABLE "order".orders (
    id uuid PRIMARY KEY,
    customer_id uuid NOT NULL,
    warehouse_id uuid NOT NULL,
    tracking_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    order_status "order".order_status NOT NULL,
    failure_messages VARCHAR(255),
    CONSTRAINT fk_customer_id FOREIGN KEY (customer_id)
        REFERENCES "order".customers (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_warehouse_id FOREIGN KEY (warehouse_id)
        REFERENCES "order".warehouses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Create order_items table
DROP TABLE IF EXISTS "order".order_items CASCADE;
CREATE TABLE "order".order_items (
    id bigint NOT NULL,
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    quantity integer NOT NULL,
    sub_total numeric(10,2) NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id, order_id),
    CONSTRAINT fk_order_id FOREIGN KEY (order_id)
        REFERENCES "order".orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Create order_address table
DROP TABLE IF EXISTS "order".order_address CASCADE;
CREATE TABLE "order".order_address (
    id uuid PRIMARY KEY,
    order_id uuid UNIQUE NOT NULL,
    street VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    CONSTRAINT fk_order_address_order_id FOREIGN KEY (order_id)
        REFERENCES "order".orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- Drop existing materialized view if it exists
DROP MATERIALIZED VIEW IF EXISTS "order".warehouse_product_m_view;

-- Create materialized view to combine warehouse and product data
CREATE MATERIALIZED VIEW "order".warehouse_product_m_view
TABLESPACE pg_default
AS
SELECT
    p.id AS product_id,
    w.id AS warehouse_id,
    p.name AS product_name,
    s.quantity AS stock,
    w.latitude AS warehouse_latitude,
    w.longitude AS warehouse_longitude,
    w.name AS warehouse_name
FROM "order".products p
JOIN "order".stocks s ON p.id = s.product_id
JOIN "order".warehouses w ON s.warehouse_id = w.id
WITH DATA;

-- Refresh the materialized view to make sure it's up-to-date
REFRESH MATERIALIZED VIEW "order".warehouse_product_m_view;

-- Drop any existing function for refreshing the materialized view
DROP FUNCTION IF EXISTS "order".refresh_warehouse_product_m_view;

-- Create function to refresh the materialized view
CREATE OR REPLACE FUNCTION "order".refresh_warehouse_product_m_view()
RETURNS TRIGGER AS $$
BEGIN
    -- Refresh the materialized view whenever a change occurs
    REFRESH MATERIALIZED VIEW "order".warehouse_product_m_view;
    RETURN NULL; -- No return value needed as we just want to refresh the view
END;
$$ LANGUAGE plpgsql;

-- Drop any existing trigger for refreshing the materialized view
DROP TRIGGER IF EXISTS refresh_warehouse_product_m_view ON "order".stocks;

-- Create the trigger on the 'stocks' table to refresh the materialized view after data changes
CREATE TRIGGER refresh_warehouse_product_m_view
AFTER INSERT OR UPDATE OR DELETE
ON "order".stocks
FOR EACH STATEMENT
EXECUTE PROCEDURE "order".refresh_warehouse_product_m_view();
