/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import org.apache.camel.PropertyInject;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring/camel-context.xml"})
public class ServiceApplication { // NOSONAR

    public int i;

    @PropertyInject("api.base.path")
    private String apiPath;

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        final ServletRegistrationBean servlet = new ServletRegistrationBean(new CamelHttpTransportServlet(), apiPath.endsWith("/") ? apiPath + "*" : apiPath + "/*");
        servlet.setName("CamelServlet");
	String s;
	System.out.println("Hello World 2");
        return servlet;
    }
}
