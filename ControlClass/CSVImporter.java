package ControlClass;

import EntityClass.Student;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVImporter {

    /**
     * Import students from CSV file
     * CSV format: StudentID,Name,Major,Year,Email
     * 
     * @param filePath Path to the students CSV file
     * @param userRepo User repository to add students to
     * @return Number of students imported
     */
    public static int importStudents(String filePath, IUserRepository userRepo) {
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] fields = line.split(",");
                    
                    if (fields.length < 5) {
                        System.err.println("Skipping invalid student line: " + line);
                        continue;
                    }
                    
                    // CSV format: StudentID,Name,Major,Year,Email
                    String studentId = fields[0].trim();  // e.g., U2310001A
                    String name = fields[1].trim();
                    String major = fields[2].trim();
                    int yearOfStudy = Integer.parseInt(fields[3].trim());
                    String email = fields[4].trim();
                    
                    Student student = new Student(studentId, name, email, yearOfStudy, major);
                    userRepo.add(student);
                    count++;
                    
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing student line: " + line + " - " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error processing student line: " + line + " - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading students CSV file: " + e.getMessage());
            return 0;
        }
        
        return count;
    }

    /**
     * Import career staff from CSV file
     * CSV format: StaffID,Name,Role,Department,Email
     * 
     * @param filePath Path to the career staff CSV file
     * @param userRepo User repository to add career staff to
     * @return Number of career staff imported
     */
    public static int importCareerStaff(String filePath, IUserRepository userRepo) {
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] fields = line.split(",");
                    
                    if (fields.length < 5) {
                        System.err.println("Skipping invalid career staff line: " + line);
                        continue;
                    }
                    
                    // CSV format: StaffID,Name,Role,Department,Email
                    String staffId = fields[0].trim();  // NTU account e.g., sng001
                    String name = fields[1].trim();
                    // fields[2] is Role - not used in CareerStaff constructor
                    String department = fields[3].trim();
                    String email = fields[4].trim();
                    
                    CareerStaff staff = new CareerStaff(staffId, name, email, department);
                    userRepo.add(staff);
                    count++;
                    
                } catch (Exception e) {
                    System.err.println("Error processing career staff line: " + line + " - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading career staff CSV file: " + e.getMessage());
            return 0;
        }
        
        return count;
    }

    /**
     * Export a CompanyRep to CSV file (append mode)
     * CSV format: CompanyRepID,Name,Email,CompanyName,Department,Position
     * 
     * @param filePath Path to the company reps CSV file
     * @param companyRep CompanyRep to export
     * @return true if successful, false otherwise
     */
    public static boolean exportCompanyRep(String filePath, EntityClass.CompanyRep companyRep) {
        try (java.io.FileWriter fw = new java.io.FileWriter(filePath, true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
            
            // Create CSV line: CompanyRepID,Name,Email,CompanyName,Department,Position
            String line = String.format("%s,%s,%s,%s,%s,%s",
                companyRep.getUserId(),
                companyRep.getName(),
                companyRep.getEmail(),
                companyRep.getCompanyName(),
                companyRep.getDepartment(),
                companyRep.getPosition()
            );
            
            out.println(line);
            return true;
            
        } catch (java.io.IOException e) {
            System.err.println("Error exporting CompanyRep to CSV: " + e.getMessage());
            return false;
        }
    }

    /**
     * Import company representatives from CSV file
     * CSV format: CompanyRepID,Name,Email,CompanyName,Department,Position
     * 
     * @param filePath Path to the company reps CSV file
     * @param userRepo User repository to add company reps to
     * @return Number of company reps imported
     */
    public static int importCompanyReps(String filePath, IUserRepository userRepo) {
        int count = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] fields = line.split(",");
                    
                    if (fields.length < 6) {
                        System.err.println("Skipping invalid company rep line: " + line);
                        continue;
                    }
                    
                    // CSV format: CompanyRepID,Name,Email,CompanyName,Department,Position
                    String repId = fields[0].trim();  // Company email
                    String name = fields[1].trim();
                    String email = fields[2].trim();
                    String companyName = fields[3].trim();
                    String department = fields[4].trim();
                    String position = fields[5].trim();
                    
                    EntityClass.CompanyRep rep = new EntityClass.CompanyRep(repId, name, email, companyName, department, position);
                    userRepo.add(rep);
                    count++;
                    
                } catch (Exception e) {
                    System.err.println("Error processing company rep line: " + line + " - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            // File might not exist yet, which is okay for first run
            if (e.getMessage().contains("No such file")) {
                return 0; // No error message, just return 0
            }
            System.err.println("Error reading company reps CSV file: " + e.getMessage());
            return 0;
        }
        
        return count;
    }
}
