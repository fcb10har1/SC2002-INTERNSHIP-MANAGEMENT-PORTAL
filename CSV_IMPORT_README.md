# CSV Import Feature Documentation

## Overview
The system now supports importing users from CSV files and includes an email attribute for all user types.

## User ID Format Requirements

According to the system specifications:
- **Students' IDs**: Start with U, followed by 7-digit numbers and ends with a letter (e.g., U2345123F)
- **Company Representatives ID**: Their company email address (e.g., john.doe@techcorp.com)
- **Career Center Staff's ID**: Their NTU account (e.g., sng001)
- **Default Password**: All users use the default password `password`
- Users can change their password in the system

## Changes Made

### 1. Email Attribute Added
All user classes now include an email field:
- **User.java**: Added `email` field with getter/setter
- **Student.java**: Updated constructor to include email parameter
- **CompanyRep.java**: Updated constructor to include email parameter (email = userId for company reps)
- **CareerStaff.java**: Updated constructor to include email parameter

### 2. CSV Import Functionality
Created **CSVImporter.java** utility class in ControlClass package with methods:
- `importStudents(filePath, userRepo)` - Imports students from CSV
- `importCareerStaff(filePath, userRepo)` - Imports career staff from CSV
- `importCompanyReps(filePath, userRepo)` - Imports company representatives from CSV
- `exportCompanyRep(filePath, companyRep)` - Exports a single CompanyRep to CSV (for new registrations)

### 3. Sample CSV Files

#### students.csv
Format: `StudentID,Name,Major,Year,Email`

Example:
```csv
StudentID,Name,Major,Year,Email
U2310001A,Tan Wei Ling,Computer Science,2,tan001@e.ntu.edu.sg
U2310002B,Ng Jia Hao,Data Science & AI,3,ng002@e.ntu.edu.sg
```

- Student IDs follow format: U + 7 digits + letter
- Email addresses: `@e.ntu.edu.sg` domain

#### careerstaff.csv
Format: `StaffID,Name,Role,Department,Email`

Example:
```csv
StaffID,Name,Role,Department,Email
sng001,Dr. Sng Hui Lin,Career Centre Staff, CCDS,sng001@ntu.edu.sg
tan002,Mr. Tan Boon Kiat,Career Centre Staff, CCDS,tan002@ntu.edu.sg
```

- Staff IDs: NTU account format (e.g., sng001)
- Email addresses: `@ntu.edu.sg` domain
- Role field is included in CSV but not used in CareerStaff entity

#### companyreps.csv
Format: `CompanyRepID,Name,Email,CompanyName,Department,Position`

Example:
```csv
CompanyRepID,Name,Email,CompanyName,Department,Position
john.doe@company.com,John Doe,john.doe@company.com,Company,Cybersecurity,Manager
```

- Company Rep IDs: Company email address (same as Email field)
- Automatically populated when new CompanyReps register
- New registrations are appended to this file to persist across restarts

### 4. Updated Files

#### MainApp.java
- Added CSV import on startup via `importUsersFromCSV()` method
- Now imports from three CSV files: students.csv, careerstaff.csv, and companyreps.csv
- Updated `seedDemoUsers()` to use proper ID formats:
  - Student: U9999999Z
  - CompanyRep: demo.rep@techcorp.com (email as ID)
  - CareerStaff: demo001 (NTU account)
- Imports CSVImporter class

#### LoginUI.java
- CompanyRep registration now requires company email address as User ID
- Email validation added (must contain @ and .)
- Email field automatically set to userId for company reps
- **New registrations are automatically exported to companyreps.csv**
- After registration, user returns to login menu instead of exiting
- Updated user prompts to clarify ID format requirements

#### CSVImporter.java
- Updated to parse new CSV header formats:
  - Students: `StudentID,Name,Major,Year,Email`
  - Career Staff: `StaffID,Name,Role,Department,Email`
- Proper field mapping to match new column order

## Usage

### Running the Application
1. Ensure CSV files are in the root directory of the project:
   - `students.csv`
   - `careerstaff.csv`
   - `companyreps.csv` (will be created automatically if not present)

2. Run MainApp - it will automatically:
   - Import students from students.csv
   - Import career staff from careerstaff.csv
   - Import company representatives from companyreps.csv
   - Create demo users (if not already imported from CSV)

3. When a new CompanyRep registers:
   - Their information is saved to the in-memory repository
   - Their information is also exported to companyreps.csv
   - They can now log in immediately on the next restart (after approval)

### CSV File Format

**students.csv:**
```csv
StudentID,Name,Major,Year,Email
U2310001A,Tan Wei Ling,Computer Science,2,tan001@e.ntu.edu.sg
U2310002B,Ng Jia Hao,Data Science & AI,3,ng002@e.ntu.edu.sg
```

**careerstaff.csv:**
```csv
StaffID,Name,Role,Department,Email
sng001,Dr. Sng Hui Lin,Career Centre Staff, CCDS,sng001@ntu.edu.sg
tan002,Mr. Tan Boon Kiat,Career Centre Staff, CCDS,tan002@ntu.edu.sg
```

### Default Password
All users imported from CSV have the default password: **password**

Users should change their password after first login.

## Testing

### Login with CSV-imported users:
- **Students**: U2310001A, U2310002B, etc., password: password
- **Career Staff**: sng001, tan002, lee003, password: password
- **Demo CompanyRep**: demo.rep@techcorp.com, password: password
- **Demo Student**: U9999999Z, password: password
- **Demo Career Staff**: demo001, password: password

### Registering New Company Representatives:
When registering a new CompanyRep:
1. User ID must be their company email (e.g., john.smith@company.com)
2. Email will automatically be set to the same as User ID
3. Must include @ and . in the email address

### Verify Import
When the application starts, you'll see:
```
=== Importing Users from CSV Files ===
✓ Imported X students from students.csv
✓ Imported X career staff from careerstaff.csv
```

## Error Handling
- CSV import errors are logged to console but don't stop application startup
- Invalid lines in CSV files are skipped with error messages
- Missing CSV files result in 0 imports but application continues
- Duplicate user IDs from CSV vs demo users are prevented
- Email validation for CompanyRep registration (must contain @ and .)

## Future Enhancements
- Add password setting in CSV (currently all default to "password")
- Add ability to export users to CSV
- Add more robust email validation
- Add Student ID format validation (U + 7 digits + letter)
