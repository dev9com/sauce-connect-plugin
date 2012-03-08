package com.dynacrongroup.plugin.sauce;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Goal which starts a new sauce connect tunnel
 *
 * @goal stop
 * @phase post-integration-test
 */

public class StopMojo
        extends AbstractSauceConnectMojo {

    @Override
    public void executeGoal()
            throws MojoExecutionException {

        sauceConnectManager.stopCurrentTunnel(true);

        if (sauceConnectManager.getTunnelStatus().equalsIgnoreCase(SauceConnectManager.NO_TUNNEL_STATUS)) {
            getLog().info("Successfully stopped Sauce Connect tunnel.  View logs in " + logDirectory.getAbsolutePath());
        } else {
            getLog().error("Failed to stop Sauce Connect tunnel.  View logs in " + logDirectory.getAbsolutePath());
            throw new MojoExecutionException("Failed to stop Sauce Connect tunnel.");
        }
    }
}
