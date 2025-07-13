package com.jpmc.midascore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;

@Configuration
public class IncentiveConduit {

    /**
     * TODO: Consider WebClient for non-blocking I/O
     * (And refactor the Kafka consumer or Transaction service for asynchronicity?)
     * <p>
     * Currently, the restTemplate helps complete a POST operation to the Incentive API.
     */
    private final RestTemplate restTemplate;

    /* Strings that comprise the so-far single endpoint of the Incentive API */

    private final String scheme;
    private final String host;
    private final String port;

    /**
     * The relative path of the Incentive endpoint.
     */
    private final String resourcePath = "/incentive";

    /**  */
    private final URI url;

    public IncentiveConduit(RestTemplateBuilder builder
            , @Value("${incentive.api.scheme}") String scheme
            , @Value("${incentive.api.host}") String host
            , @Value("${incentive.api.port}") String port)
    {
        if (builder == null)
            throw new IllegalArgumentException("builder must not be null");

        if (scheme == null || scheme.isBlank())
            throw new IllegalArgumentException("incentive.api.scheme must not be null or blank");

        if (host == null || host.isBlank())
            throw new IllegalArgumentException("incentive.api.host must not be null or blank");

        if (port == null || port.isBlank())
            throw new IllegalArgumentException("incentive.api.port must not be null or blank");

        // Build the restTemplate with connection and read timeouts.

        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();

        // Build the full URI for the Incentive API endpoint.

        this.scheme = scheme;
        this.host = host;
        this.port = port;

        url = UriComponentsBuilder.newInstance()
                .scheme(this.scheme)
                .host(this.host)
                .port(this.port)
                .path(resourcePath)
                .build()
                .toUri();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public URI getUrl() {
        return url;
    }
}
