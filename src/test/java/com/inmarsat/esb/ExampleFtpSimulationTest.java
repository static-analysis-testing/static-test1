/*
 * Copyright © 2018, Inmarsat Global Ltd.
 * This file cannot be copied and/or distributed outside Inmarsat without the express permission given by
 * Inmarsat Legal Affairs.  All permission requests should be requested via LegalCompliance@inmarsat.com.
 */

package com.inmarsat.esb;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ExampleFtpSimulationTest {

    @LocalServerPort
    private int assignedPort;

    private FakeFtpServer ftpServer;

    // Notes:
    // Update your src/test/resources/app.properties file to use 127.0.0.1 when you consume your ftp service.
    // This test will initialize the *external* service, then start *your* service, allowing you to write automated black-box tests against your service.

    @Before
    public void beforeTest() {
        final FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/tmpfile.txt", "abc123"));

        ftpServer = new FakeFtpServer();
        ftpServer.setServerControlPort(12345);
        ftpServer.addUserAccount(new UserAccount("someuser", "somepassword", "/"));
        ftpServer.setFileSystem(fileSystem);
        ftpServer.start();
    }

    @After
    public void afterTest() {
        ftpServer.stop();
    }

    @Test
    public void consumeFtpService() throws IOException {

        // This is not a valid test, just a demo to prove that the simulated ftp service can be consumed. Update your configuration so that your
        // service can consume it and you can perform black box tests against your own API with controlled responses from the ftp service.

        final FTPClient ftpClient = new FTPClient();
        ftpClient.connect("127.0.0.1", 12345);
        ftpClient.login("someuser", "somepassword");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.retrieveFile("/tmpfile.txt", outputStream);
        ftpClient.disconnect();
    }
}
