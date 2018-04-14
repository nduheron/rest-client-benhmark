package fr.nduheron.poc.restclientbenchmark;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import fr.nduheron.poc.restclientbenchmark.users.UserService;
import fr.nduheron.poc.restclientbenchmark.users.impl.UserServiceRestTemplate;
import fr.nduheron.poc.restclientbenchmark.users.impl.UserServiceWebflux;
import fr.nduheron.poc.restclientbenchmark.users.model.User;

public class UserServiceBenchmark {

	@Configuration
	@ComponentScan("fr.nduheron.poc.restclientbenchmark")
	public static class SpringConfig {

	}

	@State(Scope.Benchmark)
	public static class UserServiceState {
		private UserService userServiceRestTemplate;
		private UserService userServiceWebflux;

		@Setup(Level.Trial)
		public void loadSpringContext() {
			try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
				context.register(SpringConfig.class);
				context.refresh();
				userServiceRestTemplate = context.getBean(UserServiceRestTemplate.class);
				userServiceWebflux = context.getBean(UserServiceWebflux.class);
			}

		}

		public UserService getUserServiceRestTemplate() {
			return userServiceRestTemplate;
		}

		public UserService getUserServiceWebflux() {
			return userServiceWebflux;
		}

	}

	@Benchmark
	public List<User> testRestTemplate(UserServiceState state) {
		return state.getUserServiceRestTemplate().findAll();
	}

	@Benchmark
	public List<User> testWebflux(UserServiceState state) {
		return state.getUserServiceWebflux().findAll();
	}

}
