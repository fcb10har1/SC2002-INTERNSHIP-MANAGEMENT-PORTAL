package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.Application;
import EntityClass.Student;
import EntityClass.InternshipOpportunity;

/**
 * Contract for Application data persistence and retrieval operations.
 */
public interface IApplicationRepository {
    /**
     * Adds a new application.
     * @param a application instance
     */
    void add(Application a);

    /**
     * Finds application by id.
     * @param id application id
     * @return optional application
     */
    Optional<Application> findById(String id);

    /**
     * Finds applications submitted by the student.
     * @param s student
     * @return list of applications
     */
    List<Application> findByStudent(Student s);

    /**
     * Finds applications targeting the opportunity.
     * @param io internship opportunity
     * @return list of applications
     */
    List<Application> findByOpportunity(InternshipOpportunity io);

    /**
     * Updates existing application.
     * @param a application
     */
    void update(Application a);

    /**
     * Returns all applications.
     * @return list of applications
     */
    List<Application> all();

    /**
     * Counts applications for the student.
     * @param s student
     * @return number of applications
     */
    default int countByStudent(Student s) {
        return findByStudent(s).size();
    }
}
