package fr.nduheron.poc.restclientbenchmark.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
@PropertySource("classpath:httpClient.properties")
public class HttpClientConfiguration {

	@Value("${client.maxTotalConnections}")
	private Integer maxTotalConnections;
	@Value("${client.readTimeoutMillis}")
	private Integer readTimeoutMillis;
	@Value("${client.connectionTimeoutMillis}")
	private Integer connectionTimeoutMillis;

	@Bean
	HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(maxTotalConnections);

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeoutMillis)
				.setSocketTimeout(readTimeoutMillis).build();

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		httpClientBuilder.setDefaultRequestConfig(requestConfig);
		httpClientBuilder.setConnectionManager(connectionManager);

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
				httpClientBuilder.build());
		factory.setConnectTimeout(connectionTimeoutMillis);
		factory.setReadTimeout(readTimeoutMillis);

		return factory;

	}
}
