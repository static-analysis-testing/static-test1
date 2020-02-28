/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import com.google.common.collect.ImmutableMap;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleBlackBoxTest {

    @LocalServerPort
    private int assignedPort;

    @Test
    public void sampleBlackBoxTest() throws UnirestException {

        // This is just a demo of how to setup the black-box test and consume your own service.

        final Map<String, String> headers = ImmutableMap.<String, String>builder()
                .put("accept", "application/json")
                .put("Content-Type", "application/json")
                .put("Accept-Encoding", "gzip,compress,deflate,br,identity")
                .build();

        final HttpResponse<String> response = Unirest.get("http://127.0.0.1:" + assignedPort + "/api/sample")
                .headers(headers)
                .asString();

        assertThat(response.getStatus(), is(HttpStatus.OK.value()));

        // do something useful here...
    }
}