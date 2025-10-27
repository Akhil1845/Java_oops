# Foreign Key Constraint Fix - Summary

## Problem
You were getting a foreign key constraint error when trying to upload internships or tests from a new client account:
```
Cannot add or update a child row: a foreign key constraint fails 
(`skill_bridge`.`internships`, CONSTRAINT `internships_ibfk_1` 
FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`))
```

## Root Cause
1. **Internship entity** was using `Integer clientId` as a plain column instead of a proper JPA relationship
2. **Client entity** didn't specify the table name, defaulting to `client` instead of `clients`
3. **Test entity** was using table name `test` instead of `tests`
4. The database had foreign key constraints that required valid client IDs, but the entities weren't properly configured

## Changes Made

### 1. Client Entity (`Client.java`)
- Added `@Table(name = "clients")` to match the database table name

### 2. Internship Entity (`Internship.java`)
- **Before:** `private Integer clientId;`
- **After:** `private Client client;` with `@ManyToOne` relationship
- This ensures that only valid Client entities can be associated with internships

### 3. InternshipRepository (`InternshipRepository.java`)
- Updated method from `findByClientId(Integer clientId)` to `findByClient_Id(Long clientId)`

### 4. InternshipService (`InternshipService.java`)
- Added `ClientRepository` dependency
- Updated `saveInternship()` to accept `clientId` parameter and fetch the Client entity
- Updated `updateInternship()` to accept `clientId` parameter
- Updated `getByClientId()` to use `Long` instead of `Integer`

### 5. InternshipController (`InternshipController.java`)
- Updated `createInternship()` to accept `clientId` as a `@RequestParam`
- Updated `updateInternship()` to accept `clientId` as a `@RequestParam`
- Changed parameter type from `Integer` to `Long` for consistency

### 6. Test Entity (`Test.java`)
- Updated table name from `test` to `tests` to match database

### 7. Frontend Files
- **client_dashboard.html**: Updated to send `clientId` as query parameter instead of in request body
- **update_internship.html**: Updated to send `clientId` as query parameter for test uploads

## How to Use

### Creating an Internship
**Frontend (JavaScript):**
```javascript
const res = await fetch(`http://localhost:8080/api/internships?clientId=${clientId}`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({
        title: "Software Developer Internship",
        description: "Work on exciting projects",
        location: "Remote",
        domain: "Software Development",
        // ... other fields (DO NOT include clientId in body)
    })
});
```

**Backend (Java):**
```java
@PostMapping
public Internship createInternship(@RequestBody Internship internship, 
                                   @RequestParam Long clientId) {
    return internshipService.saveInternship(internship, clientId);
}
```

### Creating a Test
**Frontend (JavaScript):**
```javascript
const res = await fetch(`http://localhost:8080/api/tests?clientId=${clientId}`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({
        title: "Java Programming Test",
        description: "Basic Java concepts",
        topic: "Java",
        maxMarks: 100,
        questions: [...]
        // DO NOT include clientId in body
    })
});
```

## Important Notes

1. **Client Must Exist**: Before creating internships or tests, ensure the client account exists in the `clients` table
2. **Use Correct Client ID**: Always use the logged-in client's ID from `localStorage.getItem("clientId")`
3. **Query Parameter**: The `clientId` must be passed as a query parameter, NOT in the request body
4. **Data Type**: Client IDs are now `Long` (not `Integer`) for consistency

## Testing the Fix

1. **Restart your Spring Boot application** to load the updated entity mappings
2. Log in as a client
3. Try creating an internship - it should now work without foreign key errors
4. Try uploading a test - it should also work correctly
5. Verify that internships and tests appear in the student dashboard

## Database Cleanup (Optional)

You have duplicate tables in your database:
- `client` and `clients`
- `internship` and `internships`
- `test` and `tests`

Consider dropping the singular-named tables if they're not being used:
```sql
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS internship;
DROP TABLE IF EXISTS test;
```

## Troubleshooting

If you still get errors:
1. Check that the client ID exists: `SELECT * FROM clients WHERE id = ?;`
2. Verify the foreign key constraint: `SHOW CREATE TABLE internships;`
3. Check application logs for detailed error messages
4. Ensure you've restarted the Spring Boot application after code changes
