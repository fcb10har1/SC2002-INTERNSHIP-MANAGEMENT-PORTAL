package ControlClass;

import EntityClass.Student;
import EntityClass.CareerStaff;
import RepositoryClass.IUserRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Handles importing and exporting of data in CSV format
 */
public class CSVImporter {
    /*
     * Imports students from a CSV file into the user repository
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
                    
                    String studentId = fields[0].trim();  
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

    /*
     * Imports career staff from a CSV file into the user repository
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
                    
                
                    String staffId = fields[0].trim(); 
                    String name = fields[1].trim();
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

    /*
     * Exports a CompanyRep to a CSV file in append mode
     */
    public static boolean exportCompanyRep(String filePath, EntityClass.CompanyRep companyRep) {
        try (java.io.FileWriter fw = new java.io.FileWriter(filePath, true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {
            
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

    /*
     * Imports company representatives from a CSV file into the user repository
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
                    
                    
                    String repId = fields[0].trim();
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
            if (e.getMessage().contains("No such file")) {
                return 0;
            }
            System.err.println("Error reading company reps CSV file: " + e.getMessage());
            return 0;
        }
        
        return count;
    }
}
