package com.imageanalysis.commons.util.dynamikax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.imageanalysis.commons.errors.AppError;
import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.GatewayError;
import com.imageanalysis.commons.errors.ProjectError;
import com.imageanalysis.commons.spring.Profile;
import com.imageanalysis.commons.util.Jackson;
import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.ExpirableValue;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.imagingproject.DefaultMsImagingProjectClient;
import com.imageanalysis.commons.util.dynamikax.msuser.DefaultMsUserClient;
import com.imageanalysis.commons.util.dynamikax.msuser.MsUserClient;
import com.imageanalysis.commons.util.java.Maps;
import com.imageanalysis.commons.util.jooq.StopWatch;
import com.imageanalysis.commons.util.jooq.StringUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.net.HttpURLConnection.*;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.*;

/**
 * An opinionated Http client to call REST APIs and specifically Dynamika microservices,
 * but also could be used to call third-party APIs easily.
 * Look at the {@link DefaultMsImagingProjectClient} as a reference usage example.
 * Some ad-hoc example usages:
 * <pre>
 *     Duration expiration   = Duration.ofMinutes(30);
 *     Class&lt;VisitConfigPayload> responseType = VisitConfigPayload.class;
 *     VisitConfigPayload visitConfig = restClient.forMs(Microservice.MS_IMAGING_PROJECT)
 *             .withCache(expiration)
 *             .get("/api/visit-config/{visitConfigId}",Maps.of("visitConfigId", 1234))
 *             .execute(responseType);
 * </pre>
 * and
 * <pre>
 *     List<Long> visitConfigIds = ...;
 *     Duration expiration   = Duration.ofDays(7);
 *     TypeReference&lt;Map&lt;Long, Instant>> responseType = new TypeReference&lt;Map&lt;Long, Instant>>() {};
 *     Map&lt;Long, Instant> visitConfigsScanDate = restClient.forMs(Microservice.MS_IMAGING_PROJECT)
 *             .withCache(expiration)
 *             .put("/api/visit-config/get-scan-dates-by-visit-config-ids")
 *             .body(visitConfigIds)
 *             .execute(responseType);
 * </pre>
 */
@Slf4j
@Component
@With(PRIVATE)
@AllArgsConstructor
@ConditionalOnBean(DefaultMsUserClient.class)
public class RestClient {
    private final Supplier<String> NO_OP_TOKEN_GENERATOR = () -> null;

    private final Profile      activeProfile;
    private final RestTemplate restTemplate;
    private final Jackson      jackson;
    private final MsUserClient msUserClient;

    private final Cache<ExpirableValue<Object>>    cache                 = new InMemoryCache<>();
    private       Duration                         expiration;
    private       Supplier<String>                 authorizationProvider = () -> "Bearer " + msUserClient.getJwtToken();
    private       AuthorizationKeyProvider<Object> cacheKeyProvider      = this::defaultCacheKey;
    private       Microservice                     microservice;

    @Autowired
    public RestClient(Profile activeProfile, RestTemplate restTemplate, Jackson jackson, MsUserClient msUserClient) {
        this.activeProfile = activeProfile;
        this.restTemplate  = restTemplate;
        this.jackson       = jackson;
        this.msUserClient  = msUserClient;

        if (msUserClient instanceof DefaultMsUserClient) {
            ((DefaultMsUserClient) this.msUserClient).setRestClient(this);
        }
    }

    /**
     * Create a request with {@link HttpMethod#GET} method.
     *
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
     *                          Example:
     * 	 <pre class="code">
     * 	 String path = &quot;/hotels/42?filter={value}&quot;;<br/>
     * 	 restClient.get(path, &quot;hot&amp;cold&quot;);
     * 	 </pre>
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request get(@NonNull String path, Object... uriVariableValues) {
        return request(GET, path, uriVariableValues);
    }

    /**
     * Create a request with {@link HttpMethod#GET} method.
     *
     * @param path         The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                     it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                     But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariables The map of URI variables. Used to replace URI template variables with the values from a map.
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request get(@NonNull String path, @NonNull Map<String, ?> uriVariables) {
        return request(GET, path, uriVariables);
    }

    /**
     * Create a request with {@link HttpMethod#POST} method.
     *
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
     *                          Example:
     * 	 <pre class="code">
     * 	 String path = &quot;/hotels/42?filter={value}&quot;;<br/>
     * 	 restClient.post(path, &quot;hot&amp;cold&quot;);
     * 	 </pre>
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request post(@NonNull String path, Object... uriVariableValues) {
        return request(POST, path, uriVariableValues);
    }

    /**
     * Create a request with {@link HttpMethod#POST} method.
     *
     * @param path         The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                     it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                     But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariables The map of URI variables. Used to replace URI template variables with the values from a map.
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request post(@NonNull String path, @NonNull Map<String, ?> uriVariables) {
        return request(POST, path, uriVariables);
    }

    /**
     * Create a request with {@link HttpMethod#PUT} method.
     *
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
     *                          Example:
     * 	 <pre class="code">
     * 	 String path = &quot;/hotels/42?filter={value}&quot;;<br/>
     * 	 restClient.put(path, &quot;hot&amp;cold&quot;);
     * 	 </pre>
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request put(@NonNull String path, Object... uriVariableValues) {
        return request(PUT, path, uriVariableValues);
    }

    /**
     * Create a request with {@link HttpMethod#PUT} method.
     *
     * @param path         The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                     it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                     But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariables The map of URI variables. Used to replace URI template variables with the values from a map.
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request put(@NonNull String path, @NonNull Map<String, ?> uriVariables) {
        return request(PUT, path, uriVariables);
    }

    /**
     * Create a request with {@link HttpMethod#DELETE} method.
     *
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
     *                          Example:
     * 	 <pre class="code">
     * 	 String path = &quot;/hotels/42?filter={value}&quot;;<br/>
     * 	 restClient.request(HttpMethod.GET, path, &quot;hot&amp;cold&quot;);
     * 	 </pre>
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request delete(@NonNull String path, Object... uriVariableValues) {
        return request(DELETE, path, uriVariableValues);
    }

    /**
     * Create a request with {@link HttpMethod#DELETE} method.
     *
     * @param path         The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                     it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                     But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariables The map of URI variables. Used to replace URI template variables with the values from a map.
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request delete(@NonNull String path, @NonNull Map<String, ?> uriVariables) {
        return request(DELETE, path, uriVariables);
    }

    /**
     * Create a request with the provided http method.
     *
     * @param httpMethod        The http method of the request.
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
     *                          Example:
     * 	 <pre class="code">
     * 	 String path = &quot;/hotels/42?filter={value}&quot;;<br/>
     * 	 restClient.request(HttpMethod.GET, path, &quot;hot&amp;cold&quot;);
     * 	 </pre>
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request request(@NonNull HttpMethod httpMethod, @NonNull String path, Object... uriVariableValues) {
        val url = getUrl(path);
        val expandedUrl = uriVariableValues.length == 0
                          ? url
                          : UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(uriVariableValues).toUriString();

        return new Request(this, httpMethod, expandedUrl);
    }

    /**
     * Create a request with the provided http method.
     *
     * @param httpMethod   The http method of the request.
     * @param path         The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                     it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                     But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariables The map of URI variables. Used to replace URI template variables with the values from a map.
     * @return The request to set the headers and body and then to execute the API call.
     */
    public Request request(@NonNull HttpMethod httpMethod, @NonNull String path, @NonNull Map<String, ?> uriVariables) {
        val url = getUrl(path);
        val expandedUrl = uriVariables.isEmpty()
                          ? url
                          : UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(uriVariables).toUriString();

        return new Request(this, httpMethod, expandedUrl);
    }

    /**
     * Creates a new clone of the current client with the cache enabled.
     */
    public RestClient withCache() {
        return withCache(Duration.ofHours(1));
    }

    /**
     * Creates a new clone of the current client with the cache enabled.
     */
    public RestClient withCache(@NonNull Duration expiration) {
        return this.withExpiration(expiration);
    }

    /**
     * Creates a new clone of the current client without authorization.
     */
    public RestClient noAuth() {
        return this.withAuthorizationProvider(NO_OP_TOKEN_GENERATOR);
    }

    /**
     * Creates a new clone of the current client for the specified microservice.
     */
    public RestClient forMs(Microservice microservice) {
        return this.withMicroservice(microservice);
    }

    private boolean isCacheEnabled() {
        //noinspection ConstantConditions
        return expiration != null;
    }

    private ResponseEntity<JsonNode> request(HttpMethod httpMethod,
                                             String url,
                                             HttpHeaders headers,
                                             @Nullable Object body) {
        String key = getRequestSignature(httpMethod, url, headers, body);
        //noinspection ConstantConditions
        if (!isCacheEnabled())
            return doRequest(httpMethod, url, headers, body);

        Supplier<ExpirableValue<Object>> supplier = () -> toExpirable(doRequest(httpMethod, url, headers, body));

        //noinspection unchecked
        return (ResponseEntity<JsonNode>) cache.get(key, supplier).value;
    }

    private ResponseEntity<JsonNode> doRequest(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        val    stopWatch        = new StopWatch();
        String requestSignature = getRequestSignature(httpMethod, url, headers, body);
        try {
            val entity   = getHttpEntity(httpMethod, url, headers, body);
            val response = restTemplate.exchange(url, httpMethod, entity, JsonNode.class);

            val infoLog = String.format("Remote request executed: %s \nResponse: %s", requestSignature, response);
            stopWatch.splitInfo(infoLog);

            int statusCode = response.getStatusCodeValue();
            boolean failed = statusCode < 200
                             || statusCode > 299
                             || statusCode != HTTP_NO_CONTENT && statusCode != HTTP_CREATED && response.getBody() == null;

            if (failed) {
                log.error("Calling remote service failed. Request: {} \nResponse: {}", requestSignature, response);
                throw new AppException(toAppError(requestSignature, response));
            }

            return response;
        } catch (RestClientException ex) {
            log.error("Calling remote service failed. Request: {}", requestSignature, ex);
            val details = Maps.of("request", requestSignature, "exception", ex.getMessage());

            throw new AppException(ProjectError.REMOTE_SERVICE_FAILED.details(details));
        }
    }

    /**
     * Creates a new clone of the current client with the customized authorization value provider.
     * The provided values would be cached to improve performance.
     *
     * @param authorizationProvider A function that provides the value for {@code Authorization} header of request.
     *                              A {@link RestClient} wit disabled authorization is passed that could be used
     *                              to fetch the access token from the third-party API.
     *                              Examples of provided values: "{@code Basic YWRtaW46YWRtaW5wYXNz}"
     *                              and <br/> "{@code Bearer eyJhbG.eyJleHAiOjE2.OlFmLQHXPsuN}"
     */
    public RestClient withAuth(@NonNull Function<RestClient, String> authorizationProvider) {
        return this.withAuth(authorizationProvider, this::defaultCacheKey);
    }

    /**
     * Creates a new clone of the current client with the customized authorization value provider.
     * The provided values would be cached to improve performance.
     *
     * @param authorizationProvider A function that provides the value for {@code Authorization} header of request.
     *                              A {@link RestClient} wit disabled authorization is passed that could be used
     *                              to fetch the access token from the third-party API.
     *                              Examples of provided values: "{@code Basic YWRtaW46YWRtaW5wYXNz}"
     *                              and <br/> "{@code Bearer eyJhbG.eyJleHAiOjE2.OlFmLQHXPsuN}"
     * @param cacheKeyProvider      provides a key that would be used as a key to cache the generated authorization value.
     */
    public <T> RestClient withAuth(@NonNull Function<RestClient, String> authorizationProvider,
                                   @NonNull RestClient.AuthorizationKeyProvider<T> cacheKeyProvider) {
        return this
                .withAuthorizationProvider(() -> authorizationProvider.apply(this.noAuth()))
                .withCacheKeyProvider((AuthorizationKeyProvider<Object>) cacheKeyProvider);
    }

    @NotNull
    private HttpEntity<Object> getHttpEntity(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        if (authorizationProvider == NO_OP_TOKEN_GENERATOR)
            return new HttpEntity<>(body, headers);

        String authValue = getToken(httpMethod, url, headers, body);
        headers.addIfAbsent("Authorization", authValue);

        return new HttpEntity<>(body, headers);
    }

    private String getToken(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        val cacheKey = cacheKeyProvider.getKey(httpMethod, url, headers, body);
        if (StringUtils.isBlank(cacheKey))
            return (String) getExpirableToken().value;

        return (String) cache.get(cacheKey, this::getExpirableToken).value;
    }

    private String defaultCacheKey(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        String domain = url.replace("http://", "").replace("https://", "");
        val    index  = domain.indexOf('/');
        domain = domain.substring(0, index);

        return "AUTHORIZATION_" + domain;
    }

    @NotNull
    private ExpirableValue<Object> getExpirableToken() {
        String token = authorizationProvider.get();
        if (token.startsWith("Bearer ")) {
            val bearerToken = new BearerToken(token);
            return new ExpirableValue<>(bearerToken.jwtToken, bearerToken.expiresAt);
        }

        if (token.startsWith("Basic "))
            return new ExpirableValue<>(token, Duration.ofDays(30));

        return new ExpirableValue<>(token, Duration.ZERO);
    }

    @NotNull
    private String getUrl(String path) {
        //noinspection ConstantConditions
        if (path.contains("http://") || path.contains("https://") || microservice == null)
            return path;

        return microservice.getAppEngineBaseUrl(activeProfile) + fixPath(path);
    }

    @NotNull
    private String getRequestSignature(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        String headersStr = headers.toSingleValueMap().entrySet().stream()
                .map(it -> it.getKey() + ": " + it.getValue()).collect(Collectors.joining("\n"));
        if (StringUtils.hasText(headersStr))
            headersStr = "\n" + headersStr;

        String bodyStr = Optional.ofNullable(body).map(jackson::toJson).orElse("");
        if (StringUtils.hasText(bodyStr))
            bodyStr = "\n" + bodyStr;

        return httpMethod + " " + url + headersStr + bodyStr;
    }

    @NotNull
    private String fixPath(String path) {
        if (!path.startsWith("/"))
            return "/" + path;

        return path;
    }

    private <T> ExpirableValue<T> toExpirable(T result) {
        return new ExpirableValue<>(result, expiration);
    }

    @RequiredArgsConstructor
    public static class Request {
        private final RestClient  client;
        private final HttpMethod  method;
        private final String      url;
        private final HttpHeaders httpHeaders = new HttpHeaders();

        @Nullable
        private Object body;

        /**
         * Sets the body of the request.
         */
        public Request body(@Nullable Object body) {
            this.body = body;
            return this;
        }

        /**
         * Sets a header of the request.
         */
        public Request header(String headerName, String value) {
            httpHeaders.set(headerName, value);
            return this;
        }

        /**
         * Adds the provided {@code headers} to the current request headers.
         */
        public Request headers(MultiValueMap<String, String> headers) {
            httpHeaders.addAll(headers);
            return this;
        }

        /**
         * Executes the remote API call and returns the raw response.
         * The {@link Response} could be used to fetch extra information like response headers.
         */
        public Response execute() {
            String key = client.getRequestSignature(method, url, httpHeaders, body);
            return new Response(client.request(method, url, httpHeaders, body), key, client);
        }

        /**
         * Executes the remote API call asynchronously in the {@link ForkJoinPool#commonPool()}.
         *
         * @return The completable future of API call response.
         */
        public CompletableFuture<Response> executeAsync() {
            return CompletableFuture.supplyAsync(this::execute);
        }

        /**
         * Executes the remote API call asynchronously in the {@link ForkJoinPool#commonPool()}.
         *
         * @return The completable future of API call response.
         */
        public <T> CompletableFuture<T> executeAsync(Function<JsonNode, T> responseMapper) {
            return CompletableFuture.supplyAsync(this::execute).thenApply(it -> responseMapper.apply(it.asJsonNode()));
        }

        /**
         * Executes the remote API call asynchronously in the {@link ForkJoinPool#commonPool()}.
         *
         * @return The completable future of API call response.
         */
        public <T> CompletableFuture<T> executeAsync(Class<T> responseType) {
            return CompletableFuture.supplyAsync(this::execute).thenApply(it -> it.into(responseType));
        }

        /**
         * Executes the remote API call asynchronously in the {@link ForkJoinPool#commonPool()}.
         *
         * @return The completable future of API call response.
         */
        public <T> CompletableFuture<T> executeAsync(TypeReference<T> responseType) {
            return CompletableFuture.supplyAsync(this::execute).thenApply(it -> it.into(responseType));
        }

        /**
         * Executes the remote API call asynchronously in the given executor.
         *
         * @param executor the executor to use for asynchronous execution
         * @return The completable future of API call response.
         */
        public CompletableFuture<Response> executeAsync(Executor executor) {
            return CompletableFuture.supplyAsync(this::execute, executor);
        }

        /**
         * Executes the remote API call and returns the result.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         * @throws AppException with {@link ProjectError#REMOTE_SERVICE_FAILED} or the {@link GatewayError} if any error occurs.
         */
        public <T> T execute(Class<T> responseType) {
            return execute().into(responseType);
        }

        /**
         * Executes the remote API call and returns the result.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         * @throws AppException with {@link ProjectError#REMOTE_SERVICE_FAILED} or the {@link GatewayError} if any error occurs.
         */
        public <T> T execute(TypeReference<T> responseType) {
            return execute().into(responseType);
        }

        /**
         * Executes the remote API call and returns the result. If any error occurs, the default value will return.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         */
        public <T> T execute(Class<T> responseType, T defaultValue) {
            return executeOptional(responseType).orElse(defaultValue);
        }

        /**
         * Executes the remote API call and returns the result. If any error occurs, the default value will return.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         */
        public <T> T execute(TypeReference<T> responseType, T defaultValue) {
            return executeOptional(responseType).orElse(defaultValue);
        }

        /**
         * Executes the remote API call and returns an optional result.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         */
        public <T> Optional<T> executeOptional(TypeReference<T> responseType) {
            return execute().intoOptional(responseType);
        }

        /**
         * Executes the remote API call and returns an optional result.
         *
         * @param <T>          Represents the type of the API call response.
         * @param responseType Represents the response type to return;
         */
        public <T> Optional<T> executeOptional(Class<T> responseType) {
            return execute().intoOptional(responseType);
        }
    }

    private AppError toAppError(String requestSignature, ResponseEntity<JsonNode> response) {
        Object details = Maps.of("request", requestSignature, "response", response);
        val    body    = response.getBody();
        if (body == null || !body.isObject())
            return ProjectError.REMOTE_SERVICE_FAILED.details(details);

        if (body.has("httpStatusCode"))
            return jackson.convertValue(body, GatewayError.class);

        int httpStatusCode = response.getStatusCodeValue();
        if (body.has("responseCode")) {
            String errorCode = body.get("errorCode").asText("NO_ERROR_CODE");
            String message   = body.get("responseMessage").asText("NO_RESPONSE_MESSAGE");
            details = body.has("data") && !body.get("data").isNull() ? body.get("data") : details;
            return new GatewayError(errorCode, message, httpStatusCode).details(details);
        }

        if (body.has("errors")) {
            val error     = body.get("errors").iterator().next();
            val errorCode = error.get("error").asText("NO_ERROR_CODE");
            val message   = error.get("message").asText("NO_MESSAGE");

            return new GatewayError(errorCode, message, httpStatusCode).details(details);
        }

        return ProjectError.REMOTE_SERVICE_FAILED.details(details);
    }

    @FunctionalInterface
    public interface AuthorizationKeyProvider<T> {

        /**
         * provides a key that would be used as a key to cache the generated authorization value.
         * If returns null or blank string, it means do not cache at all.
         *
         * @param headers A readonly clone of the request headers.
         */
        @Nullable
        String getKey(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable T body);
    }

    /**
     * Encapsulates the details of API call response.
     * This could be used to get the raw JsonNode response or to fetch extra information like response headers.
     */
    @RequiredArgsConstructor
    public static class Response {
        private final ResponseEntity<JsonNode> responseEntity;
        private final String                   cacheKey;
        private final RestClient               client;

        /**
         * @return The response headers.
         */
        public HttpHeaders getHeaders() {
            return responseEntity.getHeaders();
        }

        /**
         * @return The value of the response header name .
         */
        public Optional<String> getHeader(String headerName) {
            List<String> header = responseEntity.getHeaders().get(headerName);

            return Optional.ofNullable(header).flatMap(it -> it.stream().findFirst());
        }

        /**
         * @return The unwrapped response body as string.
         * In case that the main result is wrapped in the {@code data} parameter it would be unwrapped.
         * Also, the status of {@code responseCode} parameter would be checked.
         */
        public String asString() {
            return asJsonNode().toString();
        }

        /**
         * @return The unwrapped response body as a {@link JsonNode}.
         * In case that the main result is wrapped in the {@code data} parameter it would be unwrapped.
         * Also, the status of {@code responseCode} parameter would be checked.
         */
        public JsonNode asJsonNode() {
            val     body      = requireNonNull(responseEntity.getBody());
            boolean isWrapped = body.has("responseCode");

            if (!isWrapped)
                return body;

            val responseCode = body.get("responseCode").asInt();
            if (responseCode != 200) {
                val errorCode    = body.get("errorCode").asText("NO_ERROR_CODE");
                val message      = body.get("responseMessage").asText("NO_RESPONSE_MESSAGE");
                val details      = Maps.of("data", body.get("data"), "responseCode", responseCode);
                val gatewayError = new GatewayError(errorCode, message, HTTP_BAD_GATEWAY).details(details);
                client.cache.evict(cacheKey);

                throw new AppException(gatewayError);
            }

            return body.get("data");
        }

        /**
         * @return The response HTTP status.
         */
        public HttpStatus getStatusCode() {
            return responseEntity.getStatusCode();
        }

        /**
         * Converts the API call response to a result.
         *
         * @throws AppException with {@link ProjectError#REMOTE_SERVICE_FAILED} or the {@link GatewayError} if any error occurs.
         */
        public <T> T into(Class<T> responseType) {
            return convert(responseType);
        }

        /**
         * Converts the API call response to a result.
         *
         * @throws AppException with {@link ProjectError#REMOTE_SERVICE_FAILED} or the {@link GatewayError} if any error occurs.
         */
        public <T> T into(TypeReference<T> responseType) {
            return convert(responseType.getType());
        }

        /**
         * Converts the API call response to a result. If any error occurs, the default value will return.
         */
        public <T> T into(Class<T> responseType, T defaultValue) {
            return intoOptional(responseType).orElse(defaultValue);
        }

        /**
         * Converts the API call response to a result. If any error occurs, the default value will return.
         */
        public <T> T into(TypeReference<T> responseType, T defaultValue) {
            return intoOptional(responseType).orElse(defaultValue);
        }

        /**
         * Converts the API call response to an optional result.
         */
        public <T> Optional<T> intoOptional(TypeReference<T> responseType) {
            try {
                T result = convert(responseType.getType());

                return Optional.of(result);
            } catch (Exception ex) {
                log.warn("Error on calling the remote API: {}", ex.toString());

                return Optional.empty();
            }
        }

        /**
         * Converts the API call response to an optional result.
         */
        public <T> Optional<T> intoOptional(Class<T> responseType) {
            try {
                T result = convert(responseType);

                return Optional.of(result);
            } catch (Exception ex) {
                log.warn("Error on calling the remote API: {}", ex.toString());

                return Optional.empty();
            }
        }

        private <T> T convert(Type responseType) {
            val isMsResponse = responseType.getTypeName().startsWith(MSResponse.class.getName());
            val body         = requireNonNull(responseEntity.getBody());
            val typeRef = new TypeReference<T>() {
                @Override
                public Type getType() {return responseType;}
            };
            val sourceValue = isMsResponse || !body.has("responseCode") ? body : body.get("data");

            //noinspection unchecked
            T result = responseType == String.class || responseType == CharSequence.class
                       ? (T) sourceValue.toString()
                       : client.jackson.fromJson(requireNonNull(sourceValue), typeRef);

            if (result instanceof MSResponse) {
                val msResponse = (MSResponse<?>) result;
                if (msResponse.responseCode != 200) {
                    val errorCode = Optional.ofNullable(msResponse.errorCode).orElse("NO_ERROR_CODE");
                    val message   = Optional.ofNullable(msResponse.responseMessage).orElse("NO_RESPONSE_MESSAGE");
                    val details = Maps.of("data", msResponse.getData(),
                            "responseCode", msResponse.responseCode);
                    val gatewayError = new GatewayError(errorCode, message, HTTP_BAD_GATEWAY).details(details);
                    client.cache.evict(cacheKey);

                    throw new AppException(gatewayError);
                }
            }

            return result;
        }
    }
}
