# Database Schema Fix Instructions

## Problem
Column type mismatch: `clients.id` needs to be BIGINT but `internships.client_id` and `tests.client_id` are INT.

## Solution - Follow These Steps Exactly

### Step 1: Open MySQL Command Line or MySQL Workbench

Connect to your MySQL database.

### Step 2: Run These SQL Commands One by One

```sql
-- Use the database
USE skill_bridge;

-- Step 1: Drop existing foreign key constraints
ALTER TABLE internships DROP FOREIGN KEY internships_ibfk_1;

-- Check if tests has any foreign key (try both possible names)
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
```

### Step 3: Verify the Changes

After running the commands, you should see:
- `clients.id` is `BIGINT`
- `internships.client_id` is `BIGINT`
- `tests.client_id` is `BIGINT`

### Step 4: Re-enable Hibernate DDL (Optional)

After the database is fixed, you can change back to:
```properties
spring.jpa.hibernate.ddl-auto=validate
```

Or keep it as `none` if you want full control over schema changes.

### Step 5: Restart Your Spring Boot Application

The application should now start without errors.

## Alternative: Copy-Paste Single Command

If you prefer, copy and paste this entire block into MySQL:

```sql
USE skill_bridge;
ALTER TABLE internships DROP FOREIGN KEY internships_ibfk_1;
ALTER TABLE tests DROP FOREIGN KEY IF EXISTS test_ibfk_1;
ALTER TABLE tests DROP FOREIGN KEY IF EXISTS FKduf3ovxi1vhsqx0lyawjqboca;
ALTER TABLE internships MODIFY COLUMN client_id BIGINT NOT NULL;
ALTER TABLE tests MODIFY COLUMN client_id BIGINT NOT NULL;
ALTER TABLE clients MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE internships ADD CONSTRAINT internships_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);
ALTER TABLE tests ADD CONSTRAINT tests_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);
```

## What This Does

1. **Drops foreign keys** - Removes the constraints temporarily
2. **Changes column types** - Updates all ID columns to BIGINT (64-bit integers)
3. **Recreates foreign keys** - Adds back the constraints with matching types
4. **Ensures compatibility** - All foreign key relationships now use the same data type

## Why This Happened

- Java `Long` type maps to MySQL `BIGINT`
- Your database had `INT` columns
- Hibernate tried to auto-update but couldn't because of existing foreign keys
- Manual intervention is required to change column types with foreign key constraints
