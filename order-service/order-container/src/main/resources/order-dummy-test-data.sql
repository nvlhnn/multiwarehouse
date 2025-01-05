TRUNCATE TABLE "order".stocks CASCADE;
TRUNCATE TABLE "order".products CASCADE;
TRUNCATE TABLE "order".warehouses CASCADE;


-- Insert Products
INSERT INTO "order".products (id, name, price) VALUES
('a7f4f1e0-b67f-4f62-a8b7-83c5cd35c424', 'Product A', 50.00),
('ab5f89ff-d3f7-4d47-9701-dff66e1b058b', 'Product B', 30.00);


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
