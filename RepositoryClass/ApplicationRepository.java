package RepositoryClass;

import EntityClass.Application;
import EntityClass.InternshipOpportunity;
import EntityClass.Student;

import java.util.*;
import java.util.stream.Collectors;

public class ApplicationRepository implements IApplicationRepository {

    private final Map<String, Application> store = new HashMap<>();

    @Override
    public void add(Application a) {
        store.put(generateId(a), a);
    }

    @Override
    public Optional<Application> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Application> findByStudent(Student s) {
        return store.values().stream()
                .filter(a -> a.getStudent().equals(s))
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> findByOpportunity(InternshipOpportunity io) {
        return store.values().stream()
                .filter(a -> a.getTarget().equals(io))
                .collect(Collectors.toList());
    }

    @Override
    public void update(Application a) {
        // simple replace-by-identity
        store.replace(generateId(a), a);
    }

    @Override
    public List<Application> all() {
        return new ArrayList<>(store.values());
    }

    private String generateId(Application a) {
        // for now, just use identityHashCode – you can improve later
        return Integer.toHexString(System.identityHashCode(a));
    }
}
