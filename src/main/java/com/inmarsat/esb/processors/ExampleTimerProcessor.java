/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb.processors;

import com.inmarsat.iice.exceptions.InmarsatException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExampleTimerProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws InmarsatException {
        logger.info("Hello World!");
    }
}