package com.dynacrongroup.plugin.sauce;

import com.dynacrongroup.webtest.util.ConfigurationValue;
import org.apache.maven.plugin.logging.Log;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 9:02 AM
 */
public class SauceConnectManagerIT {


    private SauceConnectManager stm = new SauceConnectManager(ConfigurationValue.getConfigurationValue("SAUCELABS_USER", null),
            ConfigurationValue.getConfigurationValue("SAUCELABS_KEY", null), mock(Log.class));

    private static final Logger LOG = LoggerFactory.getLogger(SauceConnectManagerIT.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public TestName name = new TestName();

    @Test
    public void lifecycleTest() {
        LOG.info("starting {}", name.getMethodName());

        if (!stm.isTunnelActive()) {
            LOG.info("No tunnel present; starting now for test.");
            stm.startNewTunnel(temporaryFolder.getRoot(), true);
        }

        stm.stopCurrentTunnel(true);
        assertThat("Tunnel should have stopped by now", stm.getTunnelStatus(), equalTo(SauceConnectManager.NO_TUNNEL_STATUS));
    }

}