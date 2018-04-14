package fr.nduheron.poc.restclientbenchmark.tools;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 *
 * <p>
 * Filtre REST permettant de logguer les requêtes et réponses REST.
 * </p>
 *
 * @author Nicolas.DUHERON
 *
 */
public class LoggingFilter implements ClientHttpRequestInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {

		if (LOG.isInfoEnabled()) {
			LOG.info("{} - {}.", request.getMethod(), request.getURI().toString());
			if (body != null && body.length > 0) {
				LOG.info("Content send: {}", new String(body, "UTF-8"));
			}
		}
		if (LOG.isTraceEnabled()) {
			for (String key : request.getHeaders().keySet()) {
				LOG.trace("[Request Header] {} : {}.", key, request.getHeaders().get(key));
			}
		}
		long debut = System.currentTimeMillis();
		try {
			ClientHttpResponse response = execution.execute(request, body);
			if (LOG.isInfoEnabled()) {
				long end = System.currentTimeMillis();
				LOG.info("{} - {} - {} - {} - Tps : {} ms.", request.getMethod(), request.getURI().toString(),
						response.getStatusCode(), response.getStatusText(), end - debut);
			}
			if (LOG.isTraceEnabled()) {
				for (String key : response.getHeaders().keySet()) {
					LOG.trace("[Response Header] {} : {}.", key, response.getHeaders().get(key));
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Content received : {}",
						new String(StreamUtils.copyToByteArray(response.getBody()), Charset.forName("UTF-8")));
			}
			return response;

		} catch (Exception e) {
			long end = System.currentTimeMillis();
			LOG.warn("{} - {} - Tps : {} ms.", request.getMethod(), request.getURI().toString(), end - debut, e);
			throw e;
		}

	}

}
