package BoundaryClass;

import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.User;

import java.io.*;
import java.util.List;

/**
 * DataStore handles the serialization and deserialization of users,
 * internship opportunities, and applications to and from files.
 */
public class DataStore {

    private static final String USERS_FILE = "users.ser";
    private static final String OPPS_FILE = "opportunities.ser";
    private static final String APPS_FILE = "applications.ser";

    /*
     * Writes the list of users to the users.ser file.
     */
    public void writeUsers(List<User> users) throws IOException {
        writeObject(USERS_FILE, users);
    }

    /*
     * Writes the list of internship opportunities to the opportunities.ser file.
     */
    public void writeOpportunities(List<InternshipOpportunity> opportunities) throws IOException {
        writeObject(OPPS_FILE, opportunities);
    }

    /*
     * Writes the list of applications to the applications.ser file.
     */
    public void writeApplications(List<Application> applications) throws IOException {
        writeObject(APPS_FILE, applications);
    }

    @SuppressWarnings("unchecked")
    public List<User> readUsers() throws IOException, ClassNotFoundException {
        return (List<User>) readObject(USERS_FILE);
    }

    @SuppressWarnings("unchecked")
    public List<InternshipOpportunity> readOpportunities() throws IOException, ClassNotFoundException {
        return (List<InternshipOpportunity>) readObject(OPPS_FILE);
    }

    @SuppressWarnings("unchecked")
    public List<Application> readApplications() throws IOException, ClassNotFoundException {
        return (List<Application>) readObject(APPS_FILE);
    }
    
    /*
     * Writes an object to a file using serialization.
     */
    private void writeObject(String filename, Object obj) throws IOException {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(obj);
        }
    }

    /*
     * Reads an object from a file using deserialization.
     */
    private Object readObject(String filename) throws IOException, ClassNotFoundException {
        File f = new File(filename);
        if (!f.exists()) {
            return null;
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        }
    }
}
