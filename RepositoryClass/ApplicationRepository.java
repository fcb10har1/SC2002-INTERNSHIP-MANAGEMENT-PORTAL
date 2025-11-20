package RepositoryClass;

import java.util.*;

import EntityClass.Application;
import EntityClass.Student;
import EntityClass.InternshipOpportunity;

public class ApplicationRepository implements IApplicationRepository {

    private final Map<String, Application> store = new HashMap<>();
    private int nextId = 1;

    private String newId() {
        return "APP-" + (nextId++);
    }

    @Override
    public void add(Application a) {
        String id = newId();
        store.put(id, a);
    }

    @Override
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Application> findByStudent(Student s) {
        return store.values().stream()
                .filter(a -> a.getStudent().equals(s))
                .toList();
    }

    @Override
    public List<Application> findByOpportunity(InternshipOpportunity io) {
        return store.values().stream()
                .filter(a -> a.getTarget().equals(io))
                .toList();
    }

    @Override
    public void update(Application a) {
        store.replaceAll((id, existing) -> existing == a ? a : existing);
    }

    @Override
    public List<Application> all() {
        return new ArrayList<>(store.values());
    }
}
