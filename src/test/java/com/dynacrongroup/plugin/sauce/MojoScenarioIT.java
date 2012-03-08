package com.dynacrongroup.plugin.sauce;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dynacrongroup.plugin.sauce.Configuration.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 12:37 PM
 */
public class MojoScenarioIT {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public TestName name = new TestName();


    private static final Logger LOG = LoggerFactory.getLogger(MojoScenarioIT.class);

    @Test
    public void testFullLifeCycle() throws MojoExecutionException {

        StartMojo startMojo = new StartMojo();
        startMojo.sauceLabsUser = getValue(AbstractSauceConnectMojo.SAUCELABS_USER);
        startMojo.sauceLabsKey = getValue(AbstractSauceConnectMojo.SAUCELABS_KEY);
        startMojo.logDirectory = temporaryFolder.getRoot();

        startMojo.execute();

        assertTrue(startMojo.sauceConnectManager.isTunnelActive());

        StopMojo stopMojo = new StopMojo();
        stopMojo.sauceLabsUser = getValue(AbstractSauceConnectMojo.SAUCELABS_USER);
        stopMojo.sauceLabsKey = getValue(AbstractSauceConnectMojo.SAUCELABS_KEY);
        stopMojo.logDirectory = temporaryFolder.getRoot();

        stopMojo.execute();

        assertThat(stopMojo.sauceConnectManager.getTunnelStatus(), is(SauceConnectManager.NO_TUNNEL_STATUS));
    }
}
