-- Fix the tests table - Handle NULL values and foreign keys

USE skill_bridge;

-- Step 1: Check which tests have NULL client_id
SELECT id, title, client_id FROM tests WHERE client_id IS NULL;

-- Step 2: Drop the foreign key first (if it exists)
-- Try to drop it, ignore error if it doesn't exist
SET @query = (
    SELECT CONCAT('ALTER TABLE tests DROP FOREIGN KEY ', CONSTRAINT_NAME, ';')
    FROM information_schema.TABLE_CONSTRAINTS
    WHERE TABLE_SCHEMA = 'skill_bridge' 
    AND TABLE_NAME = 'tests' 
    AND CONSTRAINT_TYPE = 'FOREIGN KEY'
    LIMIT 1
);
PREPARE stmt FROM IFNULL(@query, 'SELECT 1');
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 3: Update NULL client_id values to a valid client ID
-- First, get the first available client ID
SET @first_client_id = (SELECT MIN(id) FROM clients);

-- Update NULL values to use the first client
UPDATE tests SET client_id = @first_client_id WHERE client_id IS NULL;

-- Step 4: Now modify the column to BIGINT NOT NULL
ALTER TABLE tests MODIFY COLUMN client_id BIGINT NOT NULL;

-- Step 5: Add the foreign key constraint
ALTER TABLE tests ADD CONSTRAINT tests_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);

-- Verify
SELECT 'Tests table fixed!' AS Status;
DESCRIBE tests;
SELECT id, title, client_id FROM tests;
