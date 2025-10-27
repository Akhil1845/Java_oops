@echo off
echo Fixing database schema...
echo.

mysql -u root -pAkhil@2006 skill_bridge < "d:\OOPS PROJECT\fix_database_schema.sql"

if %errorlevel% equ 0 (
    echo.
    echo Database schema fixed successfully!
    echo You can now restart your Spring Boot application.
) else (
    echo.
    echo Error fixing database. Please run the SQL commands manually.
)

pause
