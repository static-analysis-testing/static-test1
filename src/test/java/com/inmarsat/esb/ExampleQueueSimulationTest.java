/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleQueueSimulationTest {

    @LocalServerPort
    private int assignedPort;

    private BrokerService simulatedBroker;

    // Notes:
    // Update your src/test/resources/app.properties file to use the tcp://127.0.0.1:61616 url when you consume your external broker's queue.
    // Now if you use Unirest you can call into your service and exercise its routes against sample responses that you configure here.
    // This test will initialize the *external* broker, then start *your* service, allowing you to write automated black-box tests against your service.

    @Before
    public void beforeTest() throws Exception {

        final PolicyMap policyMap = new PolicyMap();
        final PolicyEntry policy = new PolicyEntry();
        policy.setConsumersBeforeDispatchStarts(2);
        policy.setTimeBeforeDispatchStarts(1000);
        policyMap.setDefaultEntry(policy);

        simulatedBroker = new BrokerService();
        simulatedBroker.setPersistent(false);
        simulatedBroker.setUseJmx(false);
        simulatedBroker.setDestinationPolicy(policyMap);
        simulatedBroker.addConnector("tcp://127.0.0.1:61616");
        simulatedBroker.start();
    }

    @After
    public void afterTest() throws Exception {
        simulatedBroker.stop();
    }

    @Test
    public void consumeExternalQueue() throws JMSException {

        // This is not a valid test, just a demo to prove that the simulated queue can be consumed. Update your configuration so that your
        // service can consume it and you can perform black box tests against your own API with controlled responses from the external broker.

        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616?jms.prefetchPolicy.all=1");
        connectionFactory.setUseCompression(true);

        final Connection connection = connectionFactory.createConnection();
        connection.start();

        // Producer: produce data for the queue
        final Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue producerQueue = producerSession.createQueue("example-queue");
        final MessageProducer messageProducer = producerSession.createProducer(producerQueue);
        messageProducer.send(producerSession.createTextMessage("hello"));

        // Consumer: consume data from the queue
        final Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Queue consumerQueue = consumerSession.createQueue("example-queue");
        final MessageConsumer messageConsumer = consumerSession.createConsumer(consumerQueue);
        final Message response = messageConsumer.receive();
        final TextMessage textMessage = (TextMessage) response;

        assertThat(textMessage.getText(), is("hello"));
    }
}
