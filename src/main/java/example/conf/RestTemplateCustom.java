package example.conf;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestTemplateCustom implements ClientHttpRequestInterceptor {
    private static Logger LOGGER = LoggerFactory.getLogger(RestTemplateCustom.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        logRequestDetails(request);
        return execution.execute(request, body);
    }

    private void logRequestDetails(HttpRequest request) {
        LOGGER.info("Headers: {}", request.getHeaders());
        LOGGER.info("Request Method: {}", request.getMethod());
        LOGGER.info("Request URI: {}", request.getURI());
    }
}*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class RestTemplateCustom implements ClientHttpRequestInterceptor {
    private static Logger LOGGER = LoggerFactory.getLogger(RestTemplateCustom.class);

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        logRequestDetails(request, body);
//        ClientHttpResponse response = execution.execute(request, body);
//        traceResponse(response);
        return execution.execute(request, body);
    }

    private void logRequestDetails(HttpRequest request, byte[] body) {
        LOGGER.info("===========================request begin================================================");
        LOGGER.info("Headers: {}", request.getHeaders());
        LOGGER.info("Request Method: {}", request.getMethod());
        LOGGER.info("Request URI: {}", request.getURI());
        LOGGER.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
        LOGGER.info("==========================request end================================================");

    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        LOGGER.info("============================response begin==========================================");
        LOGGER.debug("Status code  : {}", response.getStatusCode());
        LOGGER.debug("Status text  : {}", response.getStatusText());
        LOGGER.debug("Headers      : {}", response.getHeaders());
        LOGGER.debug("Response body: {}", inputStringBuilder.toString());
        LOGGER.info("=======================response end=================================================");
    }
}

