/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.inmarsat.iice.transformers.serializers.JsonSerializer;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleRestServiceSimulationTest {

    @LocalServerPort
    private int assignedPort;

    private ClientAndServer simulatedExternalService;
    private final JsonSerializer serializer = new JsonSerializer(true, true);

    // Notes:
    // Update your src/test/resources/app.properties file to use the http://127.0.0.1:12345 url when you consume your external service.
    // Now if you use Unirest you can call into your service and exercise its routes against sample responses that you configure here.
    // This test will initialize the *external* service, then start *your* service, allowing you to write automated black-box tests against your service.

    @Before
    public void beforeTest() {
        simulatedExternalService = ClientAndServer.startClientAndServer(12345);

        assertNotNull(simulatedExternalService);
        assertTrue(simulatedExternalService.isRunning());

        simulatedExternalService.when(HttpRequest.request().withMethod("GET").withPath("/example/hello"))
                .respond(HttpResponse.response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody("{ \"status\": \"200\", \"message\": \"hello\" }"));
    }

    @After
    public void afterTest() {
        simulatedExternalService.stop();
    }

    @Test
    public void consumeExternalService() throws UnirestException {

        // This is not a valid test, just a demo to prove that the simulated service can be consumed. Update your configuration so that your
        // service can consume it and you can perform black box tests against your own API with controlled responses from the external service.

        final Map<String, String> headers = ImmutableMap.<String, String>builder()
                .put("accept", "application/json")
                .put("Content-Type", "application/json")
                .put("Accept-Encoding", "gzip,compress,deflate,br,identity")
                .build();

        final com.mashape.unirest.http.HttpResponse<String> response = Unirest.get("http://127.0.0.1:12345/example/hello")
                .headers(headers)
                .asString();

        assertNotNull(response);
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));

        final JsonNode jsonNode = serializer.toJsonNode(response.getBody());

        assertFalse(jsonNode.findValue("message").isNull());
        assertThat(jsonNode.findValue("message").asText(), is("hello"));
    }
}
