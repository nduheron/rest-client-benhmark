package fr.nduheron.poc.restclientbenchmark.users;

import java.util.List;

import fr.nduheron.poc.restclientbenchmark.users.model.User;

public interface UserService {

	List<User> findAll();
}
