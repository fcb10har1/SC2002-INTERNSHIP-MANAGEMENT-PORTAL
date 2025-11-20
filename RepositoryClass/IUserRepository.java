package RepositoryClass;

import java.util.List;
import java.util.Optional;

import EntityClass.User;

// DIP abstraction, managers and services depend on this
public interface IUserRepository {

    void add(User u);

    Optional<User> findById(String id);

    void update(User u);

    List<User> all();
}