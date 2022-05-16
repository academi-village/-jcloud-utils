package com.imageanalysis.commons.util.dynamikax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.imageanalysis.commons.errors.AppError;
import com.imageanalysis.commons.errors.AppException;
import com.imageanalysis.commons.errors.GatewayError;
import com.imageanalysis.commons.errors.ProjectError;
import com.imageanalysis.commons.util.Jackson;
import com.imageanalysis.commons.util.cache.Cache;
import com.imageanalysis.commons.util.cache.ExpirableValue;
import com.imageanalysis.commons.util.cache.InMemoryCache;
import com.imageanalysis.commons.util.dynamikax.imagingproject.MsImagingProjectClient;
import com.imageanalysis.commons.util.dynamikax.msuser.MsUserClient;
import com.imageanalysis.commons.util.java.Maps;
import com.imageanalysis.commons.util.jooq.StopWatch;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
 * Look at the {@link MsImagingProjectClient} as a reference usage example.
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
@ConditionalOnBean(MsUserClient.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class RestClient {
    private final Supplier<String> NO_OP_TOKEN_GENERATOR = () -> null;

    private final Profile      activeProfile;
    private final RestTemplate restTemplate;
    private final Environment  env;
    private final MsUserClient msUserClient;
    private final Jackson      jackson;

    private final Cache<ExpirableValue<Object>> cache = new InMemoryCache<>();
    private       Duration                      expiration;
    private       Supplier<String>              tokenGenerator;
    private       Microservice                  microservice;

    /**
     * Create a request with {@link HttpMethod#GET} method.
     *
     * @param path              The path of the request. For Dynamika microservices in combination with {@link #microservice}
     *                          it could be only the path of the service API. For example: {@code /api/visit-config/{visitConfigId}}
     *                          But it also could be a full URL to call third-party APIs. For example: {@code https://connect.radiobotics.io/api/v1/analysis/study}
     * @param uriVariableValues The map of URI variables. Used to replace URI template variables with the values from an array.
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
     * Creates a new clone of the current client with the customized token generator.
     */
    public RestClient with(@NonNull TokenType tokenType, @NonNull Function<RestClient, String> tokenGenerator) {
        RestClient clientWithoutAuth = this.noAuth();
        return this.withTokenGenerator(() -> tokenType.name + tokenGenerator.apply(clientWithoutAuth));
    }

    /**
     * Creates a new clone of the current client without authorization.
     */
    public RestClient noAuth() {
        return this.withTokenGenerator(NO_OP_TOKEN_GENERATOR);
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
        if (isCacheEnabled())
            //noinspection ConstantConditions,unchecked
            return (ResponseEntity<JsonNode>) cache.get(key, () ->
                    toExpirable(doRequest(httpMethod, url, headers, body))).value;

        return doRequest(httpMethod, url, headers, body);
    }

    private ResponseEntity<JsonNode> doRequest(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        val    stopWatch        = new StopWatch();
        String requestSignature = getRequestSignature(httpMethod, url, headers, body);
        try {
            val entity   = getHttpEntity(url, headers, body);
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

    private AppError toAppError(String requestSignature, ResponseEntity<JsonNode> response) {
        Object details = Maps.of("request", requestSignature, "response", response);
        val    body    = response.getBody();
        if (body == null || !body.isObject())
            return ProjectError.REMOTE_SERVICE_FAILED.details(details);

        if (body.has("httpStatusCode"))
            return jackson.convertValue(body, GatewayError.class);

        if (body.has("responseCode")) {
            String errorCode = body.get("errorCode").asText("NO_ERROR_CODE");
            String message   = body.get("responseMessage").asText("NO_RESPONSE_MESSAGE");
            details = body.has("data") && !body.get("data").isNull() ? body.get("data") : details;
            return new GatewayError(errorCode, message)
                    .setHttpStatusCode(response.getStatusCodeValue())
                    .details(details);
        }

        if (body.has("errors")) {
            val error     = body.get("errors").iterator().next();
            val errorCode = error.get("error").asText("NOT_ERROR_CODE");
            val message   = error.get("message").asText("NO_MESSAGE");

            return new GatewayError(errorCode, message)
                    .setHttpStatusCode(response.getStatusCodeValue())
                    .details(details);
        }

        return ProjectError.REMOTE_SERVICE_FAILED.details(details);
    }

    @NotNull
    private HttpEntity<Object> getHttpEntity(String url, HttpHeaders headers, @Nullable Object body) {

        if (tokenGenerator != NO_OP_TOKEN_GENERATOR)
            //noinspection ConstantConditions
            headers.addIfAbsent("Authorization",
                    tokenGenerator != null ? getToken(url) : "Bearer " + msUserClient.getJwtToken());

        return new HttpEntity<>(body, headers);
    }

    private String getToken(String url) {
        var domain = url.replace("http://", "").replace("https://", "");
        val index  = domain.indexOf('/');
        domain = domain.substring(0, index);

        //noinspection ConstantConditions
        return (String) cache.get("TOKEN_" + domain, this::getExpirableToken).value;
    }

    @NotNull
    private ExpirableValue<Object> getExpirableToken() {
        String token = tokenGenerator.get();
        if (token.startsWith("Bearer ")) {
            val bearerToken = new BearerToken(token);
            val expiresAt   = bearerToken.expiresAt.minusSeconds(120);
            return new ExpirableValue<>(bearerToken.jwtToken, expiresAt);
        }

        return new ExpirableValue<>(token, Duration.ofDays(30));
    }

    @NotNull
    private String getUrl(String path) {
        //noinspection ConstantConditions
        if (path.contains("http://") || path.contains("https://") || microservice == null)
            return path;

        return getBaseUrl(microservice) + fixPath(path);
    }

    @NotNull
    private String getRequestSignature(HttpMethod httpMethod, String url, HttpHeaders headers, @Nullable Object body) {
        var headersStr = headers.toSingleValueMap().entrySet().stream()
                .map(it -> it.getKey() + ": " + it.getValue()).collect(Collectors.joining("\n"));
        if (StringUtils.hasText(headersStr))
            headersStr = "\n" + headersStr;

        var bodyStr = Optional.ofNullable(body).map(jackson::toJson).orElse("");
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

    @NotNull
    private String getBaseUrl(Microservice microservice) {
        String defaultBaseUrl = microservice.getAppEngineBaseUrl(activeProfile);

        return env.getProperty(microservice.getMsName() + ".base-url", defaultBaseUrl);
    }

    private <T> ExpirableValue<T> toExpirable(T result) {
        return new ExpirableValue<>(result, expiration);
    }

    @RequiredArgsConstructor
    public enum TokenType {
        BASIC("Basic "),
        BEARER("Bearer ");

        public final String name;
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
            return new Response(client.request(method, url, httpHeaders, body), client.jackson);
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

    @RequiredArgsConstructor
    public static class Response {
        private final ResponseEntity<JsonNode> responseEntity;
        private final Jackson                  jackson;

        /**
         * @return The response headers.
         */
        public HttpHeaders headers() {
            return responseEntity.getHeaders();
        }

        /**
         * @return The response header of the given header name.
         */
        public Optional<String> header(String headerName) {
            List<String> header = responseEntity.getHeaders().get(headerName);

            return Optional.ofNullable(header).flatMap(it -> it.stream().findFirst());
        }

        /**
         * @return The response body as a map.
         */
        public JsonNode bodyAsJsonNode() {
            return requireNonNull(responseEntity.getBody());
        }

        /**
         * @return The response HTTP status.
         */
        public HttpStatus statusCode() {
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

            val result = jackson.fromJson(requireNonNull(sourceValue), typeRef);
            if (result instanceof MSResponse) {
                val msResponse = (MSResponse<?>) result;
                if (msResponse.responseCode != 200) {
                    val errorCode = Optional.ofNullable(msResponse.errorCode).orElse("NO_ERROR_CODE");
                    val message   = Optional.ofNullable(msResponse.responseMessage).orElse("NO_RESPONSE_MESSAGE");
                    val details = Maps.of("data", msResponse.getData(),
                            "responseCode", msResponse.responseCode);
                    val gatewayError = new GatewayError(errorCode, message)
                            .setHttpStatusCode(HTTP_BAD_GATEWAY)
                            .details(details);

                    throw new AppException(gatewayError);
                }
            }

            return result;
        }
    }
}
