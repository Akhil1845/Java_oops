-- COPY AND PASTE THIS ENTIRE BLOCK INTO MYSQL WORKBENCH OR COMMAND LINE

USE skill_bridge;

-- Drop foreign keys
ALTER TABLE internships DROP FOREIGN KEY internships_ibfk_1;
ALTER TABLE tests DROP FOREIGN KEY IF EXISTS test_ibfk_1;

-- Change column types to BIGINT
ALTER TABLE internships MODIFY COLUMN client_id BIGINT NOT NULL;
ALTER TABLE tests MODIFY COLUMN client_id BIGINT NOT NULL;
ALTER TABLE clients MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;

-- Recreate foreign keys
ALTER TABLE internships ADD CONSTRAINT internships_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);
ALTER TABLE tests ADD CONSTRAINT tests_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);

-- Show results
SELECT 'FIXED!' AS Status;
DESCRIBE clients;
DESCRIBE internships;
DESCRIBE tests;
