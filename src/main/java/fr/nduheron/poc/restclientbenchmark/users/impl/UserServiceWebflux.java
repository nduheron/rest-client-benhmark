package fr.nduheron.poc.restclientbenchmark.users.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.collect.Lists;

import fr.nduheron.poc.restclientbenchmark.users.UserService;
import fr.nduheron.poc.restclientbenchmark.users.model.User;

@Service
public class UserServiceWebflux implements UserService {

	@Autowired
	private WebClient webClient;

	@Override
	public List<User> findAll() {
		Iterable<User> users = webClient.get().uri("/users").retrieve().bodyToFlux(Integer.class)
				.flatMap(id -> webClient.get().uri("/users/{id}", id).retrieve().bodyToMono(User.class)).toIterable();

		return Lists.newArrayList(users);
	}

}
