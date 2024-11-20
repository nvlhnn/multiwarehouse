DROP SCHEMA IF EXISTS "warehouse" CASCADE;

CREATE SCHEMA "warehouse";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Drop and recreate the warehouses table
DROP TABLE IF EXISTS "warehouse".warehouses CASCADE;

CREATE TABLE "warehouse".warehouses
(
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    is_active boolean NOT NULL,
    CONSTRAINT warehouses_pkey PRIMARY KEY (id)
);

-- Drop and recreate the warehouse_address table
DROP TABLE IF EXISTS "warehouse".warehouse_address CASCADE;

CREATE TABLE "warehouse".warehouse_address
(
    id uuid NOT NULL,
    warehouse_id uuid NOT NULL,
    street varchar(255) NOT NULL,
    postal_code varchar(20) NOT NULL,
    city varchar(100) NOT NULL,
    latitude numeric(9,6),
    longitude numeric(9,6),
    CONSTRAINT warehouse_address_pkey PRIMARY KEY (id),
    CONSTRAINT fk_warehouse_id FOREIGN KEY (warehouse_id)
        REFERENCES "warehouse".warehouses (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
