DROP SCHEMA IF EXISTS "user" CASCADE;

CREATE SCHEMA "user";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "user".users CASCADE;

CREATE TABLE "user".users
(
    id uuid NOT NULL,
    email varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    role varchar(255) NOT NULL,   -- Enum for role
    token varchar(255),                  -- Assuming token can be nullable, adjust if needed
    is_active boolean NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);
