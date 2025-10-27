-- Simple fix for tests table
USE skill_bridge;

-- Check which tests have NULL client_id
SELECT id, title, client_id FROM tests WHERE client_id IS NULL;

-- Option 1: Delete tests with NULL client_id (if they're not important)
-- DELETE FROM tests WHERE client_id IS NULL;

-- Option 2: Update NULL client_id to the first available client (recommended)
-- Get the first client ID and update NULL values
UPDATE tests SET client_id = (SELECT MIN(id) FROM clients) WHERE client_id IS NULL;

-- Now modify the column to BIGINT NOT NULL
ALTER TABLE tests MODIFY COLUMN client_id BIGINT NOT NULL;

-- Add the foreign key constraint
ALTER TABLE tests ADD CONSTRAINT tests_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);

-- Verify
DESCRIBE tests;
SELECT * FROM tests;
