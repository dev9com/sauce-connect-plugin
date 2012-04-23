package com.dynacrongroup.plugin.sauce;

import com.dynacrongroup.webtest.sauce.SauceREST;
import org.apache.maven.plugin.logging.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 9:02 AM
 */
public class SauceConnectManagerTest {

    SauceConnectManager scm = new SauceConnectManager("user", "key", mock(Log.class));

    private static final Logger LOG = LoggerFactory.getLogger(SauceConnectManagerTest.class);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public TestName name = new TestName();

    @Test
    public void inActiveTest() {
        LOG.info("starting {}", name.getMethodName());

        SauceREST mockRest = mock(SauceREST.class);
        when(mockRest.getAllTunnels()).thenReturn(new JSONArray());

        scm.sauceRest = mockRest;

        assertThat(scm.isTunnelActive(), is(false));
    }

    @Test
    public void runningTest() {
        LOG.info("starting {}", name.getMethodName());

        SauceREST mockRest = mock(SauceREST.class);
        when(mockRest.getAllTunnels()).thenReturn((JSONArray)JSONValue.parse("[\"tunnelId\"]"));
        when(mockRest.getTunnelStatus("tunnelId")).thenReturn((JSONObject)JSONValue.parse("{\"status\":\"running\"}"));

        scm.sauceRest = mockRest;

        assertThat(scm.isTunnelActive(), is(true));
    }

    @Test
    public void bootingTest() {
        LOG.info("starting {}", name.getMethodName());

        SauceREST mockRest = mock(SauceREST.class);
        when(mockRest.getAllTunnels()).thenReturn((JSONArray)JSONValue.parse("[\"tunnelId\"]"));
        when(mockRest.getTunnelStatus("tunnelId")).thenReturn((JSONObject)JSONValue.parse("{\"status\":\"booting\"}"));

        scm.sauceRest = mockRest;

        assertThat(scm.isTunnelActive(), is(false));
    }

    @Test
    public void getStatusTest() {
        LOG.info("starting {}", name.getMethodName());

        SauceREST mockRest = mock(SauceREST.class);
        when(mockRest.getAllTunnels()).thenReturn((JSONArray)JSONValue.parse("[\"tunnelId\"]"));
        when(mockRest.getTunnelStatus("tunnelId")).thenReturn((JSONObject)JSONValue.parse("{\"status\":\"fake\"}"));

        scm.sauceRest = mockRest;

        assertThat(scm.getTunnelStatus(), is("fake"));
    }
}
