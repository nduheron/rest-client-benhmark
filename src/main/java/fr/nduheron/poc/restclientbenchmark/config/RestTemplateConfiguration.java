package fr.nduheron.poc.restclientbenchmark.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import fr.nduheron.poc.restclientbenchmark.tools.LoggingFilter;

@Configuration
public class RestTemplateConfiguration {

	@Autowired
	private HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory;

	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	RestOperations restOperations() {
		RestTemplate restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);

		// Add the message converters to the restTemplate
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter(objectMapper));
		restTemplate.setMessageConverters(messageConverters);

		// set up a buffering request factory, so response body is always buffered
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(
				requestFactory);
		requestFactory.setOutputStreaming(false);
		restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
		restTemplate.setInterceptors(Lists.newArrayList(new LoggingFilter()));
		return restTemplate;
	}

}
