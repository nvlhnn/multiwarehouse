--warehouses
INSERT INTO warehouse.warehouse(id, is_active, name)
    VALUES ('c415b6f8-1234-4bcd-9def-67cd12qqqb47', true, 'dummy');

INSERT INTO warehouse.products(id, name)
    VALUES ('c415b6f8-1234-4bcd-9def-67cd1234fb47', 'product_A');
INSERT INTO warehouse.products(id, name)
    VALUES ('c415b6f8-1234-4bcd-9def-67cd1234fb48', 'product_B');
INSERT INTO warehouse.products(id, name)
    VALUES ('c415b6f8-1234-4bcd-9def-67cd1234fb49', 'product_C');
INSERT INTO warehouse.products(id, name)
    VALUES ('c415b6f8-1234-4bcd-9def-67cd1234fb50', 'product_D');

    INSERT INTO warehouse.stocks(id, product_id, quantity, warehouse_id)
        VALUES ('c415b6f8-1234-4bcd-9def-67eee34fb47', 'c415b6f8-1234-4bcd-9def-67cd1234fb47', 100, 'c415b6f8-1234-4bcd-9def-67cd1234fb47');