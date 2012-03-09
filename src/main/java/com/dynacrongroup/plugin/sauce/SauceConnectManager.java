package com.dynacrongroup.plugin.sauce;

import com.dynacrongroup.webtest.sauce.SauceREST;
import com.saucelabs.sauceconnect.SauceConnect;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * User: yurodivuie
 * Date: 3/8/12
 * Time: 8:54 AM
 */
public class SauceConnectManager {

    public static final String RUNNING_STATUS = "running";
    public static final String NO_TUNNEL_STATUS = "none";
    public static final String MULTIPLE_TUNNEL_STATUS = "multiple";
    public static final String INDETERMINATE_STATUS = "indeterminate";

    private static final Logger LOG = LoggerFactory.getLogger(SauceConnectManager.class);

    String user;
    String key;
    SauceREST sauceRest;

    public SauceConnectManager(String user, String key) {
        this.user = user;
        this.key = key;
        this.sauceRest = new SauceREST(user, key);
    }

    public Boolean isTunnelActive() {
        return getTunnelStatus().equals(RUNNING_STATUS);
    }

    public String getTunnelStatus() {
        String status;
        JSONArray tunnels = sauceRest.getAllTunnels();

        if (tunnels == null || tunnels.isEmpty()) {
            status = NO_TUNNEL_STATUS;
        }
        else if (tunnels.size() > 1 ) {
            status = MULTIPLE_TUNNEL_STATUS;
        }
        else {

            JSONObject tunnelStatus = sauceRest.getTunnelStatus((String) tunnels.get(0));
            if (tunnelStatus != null) {
                LOG.trace("Tunnel status: " + tunnelStatus.toJSONString());
                status = (String)tunnelStatus.get("status");
            }
            else {
                status = INDETERMINATE_STATUS;
            }
        }
        return status;
    }

    public void startNewTunnel(File LOGDirectory, Boolean blockUntilReady) {
        Boolean newTunnel = false;
        if (!isTunnelActive()) {
            ProcessBuilder builder = new ProcessBuilder(new String[]{"java", "-jar", getSauceConnectJar(), user, key});
            builder.directory(LOGDirectory);
            try {
                Process process = builder.start();

                // Read the first line in case of obvious failures.
                LOG.info(new BufferedReader(new InputStreamReader(process.getInputStream())).readLine());

                newTunnel = true;
            } catch (IOException e) {
                LOG.error("Error when starting tunnel: " + e.getMessage());
            }
        }

        if (blockUntilReady && newTunnel) {
            if (waitForTunnelStatus(RUNNING_STATUS, 120 )) {
                LOG.debug("Successfully started tunnel");
            }
            else {
                LOG.warn("Failed to start tunnel before timeout was reached.");
            }
        }
    }

    public void stopCurrentTunnel(Boolean blockUntilStopped) {
        JSONArray allTunnels = sauceRest.getAllTunnels();

        if (allTunnels != null) {
            for (Object tunnelId : allTunnels) {
                sauceRest.deleteTunnel((String)tunnelId);
            }
        }
        if (blockUntilStopped) {
            if (waitForTunnelStatus(NO_TUNNEL_STATUS, 120 )) {
                LOG.debug("Successfully stopped tunnel");
            }
            else {
                LOG.warn("Failed to stop tunnel before timeout was reached.");
            }
        }
    }

    private Boolean waitForTunnelStatus(String expectedStatus, Integer maxWaitSeconds) {

        int interval = 5;
        String status = getTunnelStatus();

        for (int waitSeconds = 0;
             waitSeconds < maxWaitSeconds && !status.equalsIgnoreCase(expectedStatus);
             waitSeconds += interval) {

            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                LOG.error("Thread interrupted: " + e.getMessage());
            }
            status = getTunnelStatus();
            LOG.debug("current status is " + status);
        }
        return status.equalsIgnoreCase(expectedStatus);
    }

    private String getSauceConnectJar() {
        Class context = SauceConnect.class;
        String separator = System.getProperty("file.separator", "/");
        URL location = context.getResource(separator + context.getName().replace(".", separator)
                + ".class");
        String jarPath = location.getPath();
        jarPath = jarPath.substring(jarPath.indexOf(":") + 1, jarPath.indexOf("!"));
        try {
            jarPath = URLDecoder.decode(jarPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage());
        }

        LOG.debug("Jar path is " + jarPath);
        return jarPath;
    }

}
