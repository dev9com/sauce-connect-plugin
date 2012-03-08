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
public class StopMojoTest {

    private static final Logger LOG = LoggerFactory.getLogger(StopMojoTest.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public TestName name = new TestName();

    @Test
    public void tryExecute() throws MojoExecutionException {
        LOG.info("starting {}", name.getMethodName());
        SauceConnectManager mockScm = mock(SauceConnectManager.class);
        when(mockScm.getTunnelStatus()).thenReturn(SauceConnectManager.NO_TUNNEL_STATUS);

        StopMojo stopMojo = new StopMojo();
        stopMojo.sauceConnectManager = mockScm;
        stopMojo.logDirectory = temporaryFolder.getRoot();

        stopMojo.executeGoal();

        verify(mockScm).stopCurrentTunnel(true);
        verify(mockScm).getTunnelStatus();
    }

    @Test( expected = MojoExecutionException.class)
    public void tryFailure() throws MojoExecutionException {
        LOG.info("starting {}", name.getMethodName());
        SauceConnectManager mockScm = mock(SauceConnectManager.class);
        when(mockScm.getTunnelStatus()).thenReturn(SauceConnectManager.RUNNING_STATUS);


        StopMojo stopMojo = new StopMojo();
        stopMojo.sauceConnectManager = mockScm;
        stopMojo.logDirectory = temporaryFolder.getRoot();

        stopMojo.executeGoal();

        verify(mockScm).stopCurrentTunnel(true);
        verify(mockScm).getTunnelStatus();
    }
}
