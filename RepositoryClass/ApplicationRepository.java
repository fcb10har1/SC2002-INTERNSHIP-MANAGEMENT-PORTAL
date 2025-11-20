package RepositoryClass;

import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;

import java.util.*;
import java.util.stream.Collectors;

/*
 * ApplicationRepository provides in-memory storage and retrieval of Application entities.
 */
public class ApplicationRepository implements IApplicationRepository {

    private final Map<String, Application> store = new HashMap<>();

    /*     
     * Adds a new Application to the repository.
     */
    @Override
    public void add(Application a) {
        store.put(generateId(a), a);
    }

    /*     
     * Finds an Application by its ID.
     */
    @Override
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /*     
     * Finds all Applications submitted by a specific Student.
     */
    @Override
    public List<Application> findByStudent(Student s) {
        return store.values().stream()
                .filter(a -> a.getStudent().equals(s))
                .collect(Collectors.toList());
    }

    /*     
     * Finds all Applications targeting a specific InternshipOpportunity.
     */
    @Override
    public List<Application> findByOpportunity(InternshipOpportunity io) {
        return store.values().stream()
                .filter(a -> a.getTarget().equals(io))
                .collect(Collectors.toList());
    }

    /*     
     * Updates an existing Application in the repository.
     */
    @Override
    public void update(Application a) {
        // simple replace-by-identity
        store.replace(generateId(a), a);
    }

    /*     
     * Retrieves all Applications in the repository.
     */
    @Override
    public List<Application> all() {
        return new ArrayList<>(store.values());
    }

    /*     
     * Generates a unique ID for an Application.
     */
    private String generateId(Application a) {
        // for now, just use identityHashCode – you can improve later
        return Integer.toHexString(System.identityHashCode(a));
    }
}
