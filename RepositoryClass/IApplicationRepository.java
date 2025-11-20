package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.Application;
import EntityClass.Student;
import EntityClass.InternshipOpportunity;

public interface IApplicationRepository {

    void add(Application a);

    Optional<Application> findById(String id);

    List<Application> findByStudent(Student s);

    List<Application> findByOpportunity(InternshipOpportunity io);

    void update(Application a);

    List<Application> all();

    default int countByStudent(Student s) {
        return findByStudent(s).size();
    }
}
