-- Fix Database Schema - Column Type Mismatch
-- This script fixes the incompatible column types between foreign keys

USE skill_bridge;

-- Step 1: Drop existing foreign key constraints
ALTER TABLE internships DROP FOREIGN KEY IF EXISTS internships_ibfk_1;
ALTER TABLE tests DROP FOREIGN KEY IF EXISTS test_ibfk_1;
ALTER TABLE tests DROP FOREIGN KEY IF EXISTS FKduf3ovxi1vhsqx0lyawjqboca;

-- Step 2: Modify client_id columns in child tables to BIGINT
ALTER TABLE internships MODIFY COLUMN client_id BIGINT NOT NULL;
ALTER TABLE tests MODIFY COLUMN client_id BIGINT NOT NULL;

-- Step 3: Modify id column in clients table to BIGINT
ALTER TABLE clients MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

-- Step 4: Re-create foreign key constraints
ALTER TABLE internships 
    ADD CONSTRAINT internships_ibfk_1 
    FOREIGN KEY (client_id) REFERENCES clients(id);

ALTER TABLE tests 
    ADD CONSTRAINT tests_ibfk_1 
    FOREIGN KEY (client_id) REFERENCES clients(id);

-- Verify the changes
DESCRIBE clients;
DESCRIBE internships;
DESCRIBE tests;
