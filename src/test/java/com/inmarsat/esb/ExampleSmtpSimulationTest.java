/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleSmtpSimulationTest {

    @LocalServerPort
    private int assignedPort;

    private Wiser smtpServer;

    // Notes:
    // Update your src/test/resources/app.properties file to use 127.0.0.1:25 when you consume your smtp service.
    // This test will initialize the *external* service, then start *your* service, allowing you to write automated black-box tests against your service.

    @Before
    public void beforeTest() {
        smtpServer = new Wiser();
        smtpServer.setPort(12345);
        smtpServer.setHostname("127.0.0.1");
        smtpServer.start();
    }

    @After
    public void afterTest() {
        smtpServer.stop();
    }

    @Test
    public void consumeSmtpService() throws IOException, MessagingException {

        // This is not a valid test, just a demo to prove that the simulated smtp service can be consumed. Update your configuration so that your
        // service can consume it and you can perform black box tests against your own API with controlled responses from the smtp service.

        final Email email = new Email();
        email.addRecipient("aaa", "aaa@inmarsat.com", Message.RecipientType.TO);
        email.setFromAddress("noreply", "noreply@inmarsat.com");
        email.setSubject("Just Testing...");
        email.setText("testing testing testing");

        final Mailer mailer = new Mailer("127.0.0.1", 12345, "username", "password");
        mailer.sendMail(email);

        assertEquals(1, smtpServer.getMessages().size());
        assertEquals("Just Testing...", smtpServer.getMessages().iterator().next().getMimeMessage().getSubject());
    }
}
