/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zapodot.junit.db.EmbeddedDatabaseRule;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleDatabaseSimulationTest {

    @LocalServerPort
    private int assignedPort;

    // Notes:
    // Update your src/test/resources/app.properties file to use the JDBC url jdbc:h2:mem:example.
    // Now if you use Unirest you can call into your service and exercise its routes against sample data in your mock database...
    // This test will initialize the database first, then start your service, allowing you to write automated black-box tests against your service.

    @Rule
    public EmbeddedDatabaseRule database = EmbeddedDatabaseRule.builder()
            .withName("example")
            .withMode(EmbeddedDatabaseRule.CompatibilityMode.MySQL)
            .withInitialSqlFromResource("classpath:example/example.sql")
            .build();

    @Test
    public void consumeExternalDatabase() throws ClassNotFoundException, SQLException {

        // This is not a valid test, just a demo to prove that the simulated database can be consumed. Update your configuration so that your
        // service can consume it and you can perform black box tests against your own API with controlled responses from the external database.

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Statement statement = database.getConnection().createStatement()) {
            assertNotNull(statement);
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM example WHERE id = 1;")) {
                while (resultSet.next()) {
                    assertThat(resultSet.getInt("id"), is(1));
                    assertThat(resultSet.getString("name"), is("aaa bbbbbb"));
                    assertThat(resultSet.getString("message"), is("aaa bbb ccc ddd eee fff ggg"));
                }
            }
        }
    }
}
