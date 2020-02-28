/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb.processors;

import com.inmarsat.iice.connectors.PingFederateConnector;
import com.inmarsat.iice.connectors.SsoConnector;
import com.inmarsat.iice.exceptions.InmarsatException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.PropertyInject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Reference:
// https://github.com/Inmarsat-itcloudservices/IICE-Commons/tree/develop/commons-connectors

@Component
public class AuthenticationProcessor implements Processor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PropertyInject("sso.enabled")
    private Boolean enabled;

    @PropertyInject("sso.client.id")
    private String clientId;

    @PropertyInject("sso.client.secret")
    private String clientSecret;

    @PropertyInject("sso.url")
    private String url;

    @Override
    public void process(Exchange exchange) throws InmarsatException {

        if (enabled) {
            final SsoConnector sso = new PingFederateConnector(url, clientId, clientSecret);
            final String accessToken = sso.extractToken(exchange.getIn().getHeader("Authorization", "", String.class));
            sso.validateToken(accessToken); // throws Http401UnauthorizedException when token is invalid
        } else {
            logger.warn("authentication checks are disabled");
        }
    }
}
