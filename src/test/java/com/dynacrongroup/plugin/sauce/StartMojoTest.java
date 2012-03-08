package com.dynacrongroup.plugin.sauce;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: yurodivuie
 * Date: 3/7/12
 * Time: 4:19 PM
 */
public class StartMojoTest {

    private static final Logger LOG = LoggerFactory.getLogger(StartMojoTest.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public TestName name = new TestName();

    @Test
    public void tryExecute() throws MojoExecutionException {
        LOG.info("starting {}", name.getMethodName());
        SauceConnectManager mockScm = mock(SauceConnectManager.class);
        when(mockScm.isTunnelActive()).thenReturn(true);

        StartMojo startMojo = new StartMojo();
        startMojo.sauceConnectManager = mockScm;
        startMojo.logDirectory = temporaryFolder.getRoot();

        startMojo.executeGoal();

        verify(mockScm).startNewTunnel(temporaryFolder.getRoot(), true);
        verify(mockScm).isTunnelActive();
    }

    @Test( expected = MojoExecutionException.class)
    public void tryFailure() throws MojoExecutionException {
        LOG.info("starting {}", name.getMethodName());
        SauceConnectManager mockScm = mock(SauceConnectManager.class);
        when(mockScm.isTunnelActive()).thenReturn(false);

        StartMojo startMojo = new StartMojo();
        startMojo.sauceConnectManager = mockScm;
        startMojo.logDirectory = temporaryFolder.getRoot();

        startMojo.executeGoal();

        verify(mockScm).startNewTunnel(temporaryFolder.getRoot(), true);
        verify(mockScm).isTunnelActive();
    }
}
