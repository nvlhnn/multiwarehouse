-- Drop schema if exists
DROP SCHEMA IF EXISTS "order" CASCADE;
CREATE SCHEMA "order";

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create ENUM for order status
DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING');

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
    is_active BOOLEAN NOT NULL
);

-- Create orders table
DROP TABLE IF EXISTS "order".orders CASCADE;
CREATE TABLE "order".orders (
    id uuid PRIMARY KEY,
    customer_id uuid NOT NULL,
    warehouse_id uuid NOT NULL,
    tracking_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    order_status order_status NOT NULL,
    failure_messages character varying,
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
    street character varying NOT NULL,
    postal_code character varying NOT NULL,
    city character varying NOT NULL,
    CONSTRAINT fk_order_address_order_id FOREIGN KEY (order_id)
        REFERENCES "order".orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
