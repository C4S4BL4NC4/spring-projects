-- Seed data, run by Spring Boot on every startup (spring.sql.init.mode=always).
-- Each insert only fires while its table is empty, so existing data is never touched.

INSERT INTO beer (id, version, beer_name, beer_style, upc, quantity_on_hand, price, created_date, update_date)
SELECT * FROM (
    SELECT '0b1f3c1e-6a2d-4c55-9a51-1f0e2d3c4b01' AS id, 0 AS version, 'EFES' AS beer_name, 2 AS beer_style,
           '222222' AS upc, 110 AS quantity_on_hand, 120.00 AS price,
           CURRENT_TIMESTAMP - INTERVAL '3' DAY AS created_date, CURRENT_TIMESTAMP - INTERVAL '20' MINUTE AS update_date
    UNION ALL
    SELECT '0b1f3c1e-6a2d-4c55-9a51-1f0e2d3c4b02', 0, 'TUBORG', 2, '111111', 60, 125.00,
           CURRENT_TIMESTAMP - INTERVAL '2' DAY, CURRENT_TIMESTAMP - INTERVAL '50' MINUTE
    UNION ALL
    SELECT '0b1f3c1e-6a2d-4c55-9a51-1f0e2d3c4b03', 0, 'CARLSBERG', 5, '333333', 115, 120.00,
           CURRENT_TIMESTAMP - INTERVAL '1' DAY, CURRENT_TIMESTAMP - INTERVAL '20' MINUTE
) seed
WHERE NOT EXISTS (SELECT 1 FROM beer);

INSERT INTO customer (id, version, name, created_date, update_date)
SELECT * FROM (
    SELECT '5d7e9a2b-3c4f-4e61-8b72-9a0c1d2e3f01' AS id, 0 AS version, 'Riki Maro' AS name,
           CURRENT_TIMESTAMP - INTERVAL '30' DAY AS created_date, CURRENT_TIMESTAMP - INTERVAL '30' HOUR AS update_date
    UNION ALL
    SELECT '5d7e9a2b-3c4f-4e61-8b72-9a0c1d2e3f02', 0, 'Ibrahim Tatlises',
           CURRENT_TIMESTAMP - INTERVAL '60' DAY, CURRENT_TIMESTAMP - INTERVAL '2' DAY
    UNION ALL
    SELECT '5d7e9a2b-3c4f-4e61-8b72-9a0c1d2e3f03', 0, 'Carmine Berzatto',
           CURRENT_TIMESTAMP - INTERVAL '30' DAY, CURRENT_TIMESTAMP - INTERVAL '30' DAY
) seed
WHERE NOT EXISTS (SELECT 1 FROM customer);
