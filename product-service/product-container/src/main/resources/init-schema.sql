DROP SCHEMA IF EXISTS "product" CASCADE;

CREATE SCHEMA "product";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "product".products CASCADE;

CREATE TABLE "product".products
(
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    price numeric(19, 2) NOT NULL,           -- Price stored as numeric for precision
    total_stock integer NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);
