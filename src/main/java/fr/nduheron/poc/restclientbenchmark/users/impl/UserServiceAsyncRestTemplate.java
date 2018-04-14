package fr.nduheron.poc.restclientbenchmark.users.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import fr.nduheron.poc.restclientbenchmark.users.UserService;
import fr.nduheron.poc.restclientbenchmark.users.model.User;

@Service
@PropertySource("classpath:client.properties")
public class UserServiceAsyncRestTemplate implements UserService {

	@Autowired
	private RestOperations restOperations;

	@Value("${users.url}")
	private String url;

	@Override
	public List<User> findAll() {
		int[] usersId = restOperations.getForObject(url, int[].class);
		List<User> users = new ArrayList<>(usersId.length);
		for (int id : usersId) {
			users.add(restOperations.getForObject(url + "/" + id, User.class));
		}

		return users;
	}

}
