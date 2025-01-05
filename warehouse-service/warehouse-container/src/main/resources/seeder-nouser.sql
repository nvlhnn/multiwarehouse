
TRUNCATE TABLE "product".products CASCADE;
-- Insert Products
INSERT INTO "product".products (id, name, price, total_stock, image_url) VALUES
('a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 'Orange', 50000, 11, 'https://static.vecteezy.com/system/resources/previews/051/587/964/non_2x/a-unique-design-icon-of-lemon-vector.jpg'),
('ab5f89ff-d3f7-4d47-9701-dff66e1b058b', 'Cherry', 35000, 0, 'https://static.vecteezy.com/system/resources/previews/051/588/276/large_2x/an-icon-design-of-cherries-vector.jpg');



-- order service
TRUNCATE TABLE "order".stocks CASCADE;
TRUNCATE TABLE "order".products CASCADE;
TRUNCATE TABLE "order".warehouses CASCADE;
TRUNCATE TABLE "order".orders CASCADE;
TRUNCATE TABLE "order".order_items CASCADE;
TRUNCATE TABLE "order".order_address CASCADE;


-- Insert Products
INSERT INTO "order".products (id, name, price) VALUES
('a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 'Orange', 50000),
('ab5f89ff-d3f7-4d47-9701-dff66e1b058b', 'Cherry', 35000);
-- Insert Warehouses
INSERT INTO "order".warehouses (id, name, is_active, city, latitude, longitude) VALUES
('f6d3cb47-1167-4d89-bb4e-bc82a3c9cbf9', 'Warehouse A', TRUE, 'City A', 37.7749, -122.4194), -- 1 unit of stock
('a1f67b4a-9c07-45b7-bdb3-df3f9ed4327c', 'Warehouse B', TRUE, 'City B', 34.0522, -118.2437), -- 2 units of stock
('12bba0d9-3c7e-4de5-977f-e8973e36d6b7', 'Warehouse C', TRUE, 'City C', 40.7128, -74.0060),  -- 1 unit of stock
('b226e6a8-8b56-4f91-8556-15fcb56691d5', 'Warehouse D', TRUE, 'City D', 41.8781, -87.6298), -- 3 units of stock
('8c6e2d75-2f26-431f-a812-bff7122c967f', 'Warehouse E', TRUE, 'City E', 34.0522, -118.2437); -- 4 units of stock
-- Insert Stocks
INSERT INTO "order".stocks (id, warehouse_id, product_id, quantity) VALUES
('e5a2b0ff-8069-4d77-b16a-cc4b7a1a1b91', 'f6d3cb47-1167-4d89-bb4e-bc82a3c9cbf9', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 1), -- Warehouse A has 1 unit
('34cc70a4-e94c-4856-bcbb-fb6e9d12a61f', 'a1f67b4a-9c07-45b7-bdb3-df3f9ed4327c', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 2), -- Warehouse B has 2 units
('8f29b774-c028-4798-a32e-7d3fe00a5368', '12bba0d9-3c7e-4de5-977f-e8973e36d6b7', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 1), -- Warehouse C has 1 unit
('c4f41698-c378-4a3d-b3b1-55cb6b75593a', 'b226e6a8-8b56-4f91-8556-15fcb56691d5', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 3), -- Warehouse D has 3 units
('9bb2e344-c40a-4874-96cd-d6d4e97b7a9a', '8c6e2d75-2f26-431f-a812-bff7122c967f', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 4); -- Warehouse E has 4 units




----------------------------------------------------------------------------------------------------------------

--warehouse service

-- truncate Products
TRUNCATE TABLE "warehouse".stocks CASCADE;
TRUNCATE TABLE "warehouse".products CASCADE;
TRUNCATE TABLE "warehouse".warehouse_address CASCADE;
TRUNCATE TABLE "warehouse".warehouses CASCADE;
TRUNCATE TABLE "warehouse".order_stock_mutations CASCADE;
-- Insert Products
INSERT INTO "warehouse".products (id, name, price) VALUES
('a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 'Orange', 50000),
('ab5f89ff-d3f7-4d47-9701-dff66e1b058b', 'Cherry', 35000);
-- Insert Warehouses
INSERT INTO "warehouse".warehouses (id, name, is_active) VALUES
('f6d3cb47-1167-4d89-bb4e-bc82a3c9cbf9', 'Warehouse A', TRUE),
('a1f67b4a-9c07-45b7-bdb3-df3f9ed4327c', 'Warehouse B', TRUE),
('12bba0d9-3c7e-4de5-977f-e8973e36d6b7', 'Warehouse C', TRUE),
('b226e6a8-8b56-4f91-8556-15fcb56691d5', 'Warehouse D', TRUE),
('8c6e2d75-2f26-431f-a812-bff7122c967f', 'Warehouse E', TRUE);
-- Insert Warehouse Address Data
INSERT INTO "warehouse".warehouse_address (id, warehouse_id, street, postal_code, city, latitude, longitude) VALUES
('1fcb4b56-c537-4e74-a1a5-6202c801c000', 'f6d3cb47-1167-4d89-bb4e-bc82a3c9cbf9', '123 Warehouse St', '12345', 'CityA', 40.712776, -74.005974),
('5f4b9eeb-939d-4a82-8b51-c3fa6358fe3c', 'a1f67b4a-9c07-45b7-bdb3-df3f9ed4327c', '456 Market Ave', '54321', 'CityB', 34.052235, -118.243683),
('bd4d7e1d-6e5d-4671-80b3-573e64bc5da3', '12bba0d9-3c7e-4de5-977f-e8973e36d6b7', '789 Logistics Blvd', '98765', 'CityC', 41.878113, -87.629799),
('3a6f3fbc-89b0-4537-bd99-1ed18a8001ea', 'b226e6a8-8b56-4f91-8556-15fcb56691d5', '101 Industrial Park', '13579', 'CityD', 29.760427, -95.369804),
('e1a3a72e-88b4-47ac-b6ea-f759b1e773a9', '8c6e2d75-2f26-431f-a812-bff7122c967f', '202 Commerce Rd', '24680', 'CityE', 51.507351, -0.127758);
-- Insert Stocks
INSERT INTO "warehouse".stocks (id, warehouse_id, product_id, quantity) VALUES
('e5a2b0ff-8069-4d77-b16a-cc4b7a1a1b91', 'f6d3cb47-1167-4d89-bb4e-bc82a3c9cbf9', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 1), -- Warehouse A has 1 unit
('34cc70a4-e94c-4856-bcbb-fb6e9d12a61f', 'a1f67b4a-9c07-45b7-bdb3-df3f9ed4327c', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 2), -- Warehouse B has 2 units
('8f29b774-c028-4798-a32e-7d3fe00a5368', '12bba0d9-3c7e-4de5-977f-e8973e36d6b7', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 1), -- Warehouse C has 1 unit
('c4f41698-c378-4a3d-b3b1-55cb6b75593a', 'b226e6a8-8b56-4f91-8556-15fcb56691d5', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 3), -- Warehouse D has 3 units
('9bb2e344-c40a-4874-96cd-d6d4e97b7a9a', '8c6e2d75-2f26-431f-a812-bff7122c967f', 'a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 4); -- Warehouse E has 4 units
