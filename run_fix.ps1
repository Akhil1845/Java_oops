# Run this script to fix the database schema
# Usage: .\run_fix.ps1

$sqlFile = "d:\OOPS PROJECT\fix_database_schema.sql"
$database = "skill_bridge"

Write-Host "Fixing database schema..." -ForegroundColor Yellow
Write-Host "Please enter your MySQL root password when prompted." -ForegroundColor Cyan

# Execute the SQL file
Get-Content $sqlFile | mysql -u root -p $database

Write-Host "`nDatabase schema fixed successfully!" -ForegroundColor Green
Write-Host "You can now restart your Spring Boot application." -ForegroundColor Green
