package RepositoryClass;

import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory repository for Application entities with query helpers by student/opportunity.
 */
public class ApplicationRepository implements IApplicationRepository {

    private final Map<String, Application> store = new HashMap<>();

    /**
     * Adds a new application to the repository.
     * @param a application instance
     */
    @Override
    public void add(Application a) {
        store.put(generateId(a), a);
    }

    /**
     * Finds an application by its generated ID.
     * @param id generated application id
     * @return optional application
     */
    @Override
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Finds all applications submitted by a specific student.
     * @param s student
     * @return list of applications for the student
     */
    @Override
    public List<Application> findByStudent(Student s) {
        return store.values().stream()
                .filter(a -> a.getStudent().equals(s))
                .collect(Collectors.toList());
    }

    /**
     * Finds all applications targeting a specific internship opportunity.
     * @param io target opportunity
     * @return list of applications for the opportunity
     */
    @Override
    public List<Application> findByOpportunity(InternshipOpportunity io) {
        return store.values().stream()
                .filter(a -> a.getTarget().equals(io))
                .collect(Collectors.toList());
    }

    /**
     * Updates (replaces) an existing application.
     * @param a application instance (by identity)
     */
    @Override
    public void update(Application a) {
        // simple replace-by-identity
        store.replace(generateId(a), a);
    }

    /**
     * Returns all applications currently stored.
     * @return list of applications
     */
    @Override
    public List<Application> all() {
        return new ArrayList<>(store.values());
    }

    /**
     * Generates a pseudo-unique id for an application using identity hash.
     * @param a application
     * @return generated id string
     */
    private String generateId(Application a) {
        // for now, just use identityHashCode – you can improve later
        return Integer.toHexString(System.identityHashCode(a));
    }
}
