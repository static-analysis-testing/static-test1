/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import com.inmarsat.esb.processors.AuthenticationProcessor;
import com.inmarsat.esb.processors.ExceptionProcessor;
import com.inmarsat.esb.processors.ExampleRestProcessor;
import com.inmarsat.esb.processors.ExampleTimerProcessor;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

// Reference:
// http://camel.apache.org
// http://www.baeldung.com/apache-camel-intro
// http://www.baeldung.com/camel-integration-patterns
// http://www.baeldung.com/apache-camel-spring-boot

@Component
@SuppressWarnings({"squid:S1192"})
@ServletComponentScan(basePackages = "com.inmarsat.esb")
public class ServiceRoutes extends RouteBuilder { // NOSONAR

    @PropertyInject("api.base.path")
    private String apiPath;

    @PropertyInject("api.host")
    private String apiHost;

    @PropertyInject("api.port")
    private Integer apiPort;

    @PropertyInject("api.title")
    private String apiTitle;

    @PropertyInject("api.version")
    private String apiVersion;

    @PropertyInject("api.swagger.path")
    private String swaggerPath;

    @PropertyInject("api.description")
    private String apiDescription;

    @PropertyInject("api.contact.name")
    private String apiContactName;

    @PropertyInject("api.contact.email")
    private String apiContactEmail;

    @Autowired
    private ExceptionProcessor exceptionProcessor;

    @Autowired
    private AuthenticationProcessor authenticationProcessor;

    @Autowired
    private ExampleTimerProcessor exampleTimerProcessor;

    @Autowired
    private ExampleRestProcessor exampleRestProcessor;

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .bindingMode(RestBindingMode.json)
                .component("servlet")
                .contextPath(apiPath)
                .host(apiHost)
                .port(apiPort)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Headers", "Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers")
                .skipBindingOnErrorCode(false)
                // Swagger Configuration:
                .apiContextPath(swaggerPath)
                .apiContextRouteId("swagger")
                .apiProperty("cors", "true")
                .apiProperty("api.title", apiTitle)
                .apiProperty("api.version", apiVersion)
                .apiProperty("api.description", apiDescription)
                .apiProperty("api.contact.name", apiContactName)
                .apiProperty("api.contact.email", apiContactEmail)
                .apiProperty("base.path", apiPath);

        // Automatic Exception Handling
        // https://github.com/Inmarsat-itcloudservices/IICE-Commons/tree/develop/commons-exceptions
        // This is a demonstration of a pattern that has been used successfully in other services to centralize exception
        // handling and standardize REST+JSON responses. You are not required to use this pattern but it is included with
        // the hope that it saves you time and reduces the amount of code that has to be written. See ExceptionProcessor.java
        // for the other half of this pattern.

        onException(Exception.class)
                .handled(true)
                .to("direct:exception");

        from("direct:exception")
                .process(exceptionProcessor)
                .end();

        // Define a Sample Timer Route
        // This logs "Hello World!" in the service logs.
        // Demonstrates how to stub out a processor to handle incoming requests and how to authenticate incoming requests.

        from("timer://foo?fixedRate=true&period=6000&repeatCount=20")
                .routeId(ExampleTimerProcessor.class.getSimpleName())
                .to("direct:inmarsatTimerRoute");

        from("direct:inmarsatTimerRoute")
                .streamCaching()
                .process(exampleTimerProcessor)
                .end();

        // Define a Sample REST Route
        // This defines standard GET/POST/PUT/PATCH/DELETE methods and passes control to a single processor
        // Demonstrates how to stub out a processor to handle incoming requests and how to authenticate incoming requests.

        rest("/sample")
                .consumes("application/json")
                .produces("application/json")
                .get()
                        .description("defines a get all operation on the Sample model")
                        .outType(String.class)
                        .to("direct:sampleRestRoute")
                .get("/{id}")
                        .description("defines a get by id operation on the Sample model")
                        .outType(String.class)
                        .to("direct:sampleRestRoute");

        from("direct:sampleRestRoute")
                .streamCaching()
                .process(authenticationProcessor)
                .process(exampleRestProcessor)
                .end();

    }
}