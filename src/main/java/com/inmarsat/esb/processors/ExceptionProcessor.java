/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb.processors;

import com.inmarsat.iice.exceptions.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Automatic Exception Handling
// https://github.com/Inmarsat-itcloudservices/IICE-Commons/tree/develop/commons-exceptions
// This is a demonstration of a pattern that has been used successfully in other services to centralize exception
// handling and standardize REST+JSON responses. You are not required to use this pattern but it is included with
// the hope that it saves you time and reduces the amount of code that has to be written. See ServiceRoutes.java
// for the other half of this pattern.

@Component
public class ExceptionProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(Exchange exchange) throws InmarsatException {

        final InmarsatException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, InmarsatException.class);
        final InmarsatExceptionTranslator translator = new InmarsatExceptionTranslator(exception);
        logger.error(translator.getErrorMessage());

        exchange.getOut().setHeader(Exchange.HTTP_RESPONSE_CODE, translator.getHttpStatus());
        exchange.getOut().setBody(translator.getHttpResponse());
    }
}