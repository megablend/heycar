INSERT INTO dealers(id, created_at, name, updated_at) 
SELECT * FROM (SELECT 1, to_date('2019-02-09', 'YYYY-MM-DD'), 'Chevrolet', to_date('2019-02-09', 'YYYY-MM-DD')) AS tmp
WHERE NOT EXISTS (
    SELECT name FROM dealers WHERE name = 'Chevrolet'
) LIMIT 1;

-- Second Query
INSERT INTO providers(id, dealer_id, created_at, name, updated_at) 
SELECT * FROM (SELECT 1, 1, to_date('2019-02-09', 'YYYY-MM-DD'), 'Chevrolet Provider', to_date('2019-02-09', 'YYYY-MM-DD')) AS tmp
WHERE NOT EXISTS (
    SELECT name FROM providers WHERE name = 'Chevrolet Provider' AND dealer_id = 1
) LIMIT 1;