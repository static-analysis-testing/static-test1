/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb.processors;

import com.inmarsat.esb.models.Sample;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ExampleRestProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) {
        logger.info("processed GET: Hello World!");
        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK.value());
        exchange.getOut().setBody(new Sample(0, "GET", "Hello World!"));
    }
}