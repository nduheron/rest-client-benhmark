package fr.nduheron.poc.restclientbenchmark.config;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.nduheron.poc.restclientbenchmark.users.impl.UserServiceWebflux;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.resources.PoolResources;

@Configuration
@PropertySource("classpath:client.properties")
public class WebClientConfiguration {
	private static final Logger log = LoggerFactory.getLogger(UserServiceWebflux.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${client.readTimeoutMillis}")
	private Integer readTimeoutMillis;
	@Value("${client.connectionTimeoutMillis}")
	private Integer connectionTimeoutMillis;
	@Value("${client.maxTotalConnections}")
	private Integer maxTotalConnections;

	@Value("${baseUrl}")
	private String baseUrl;

	@Bean
	WebClient webClient() {
		ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(configurer -> {
			configurer.registerDefaults(false);
			configurer.customCodecs().encoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
			configurer.customCodecs().decoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
		}).build();

		ReactorClientHttpConnector connector = new ReactorClientHttpConnector(options -> {
			options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMillis);
			options.afterNettyContextInit(ctx -> {
				ctx.addHandlerLast(new ReadTimeoutHandler(readTimeoutMillis, TimeUnit.MILLISECONDS));
			});
			options.poolResources(PoolResources.fixed("myPool", maxTotalConnections));
		});
		return WebClient.builder().exchangeStrategies(strategies).filter(logRequest()) // here is the magic
				.baseUrl(baseUrl).clientConnector(connector).build();
	}

	private static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			// clientRequest.headers().forEach((name, values) -> values.forEach(value ->
			// log.info("{}={}", name, value)));
			return Mono.just(clientRequest);
		});
	}
}
