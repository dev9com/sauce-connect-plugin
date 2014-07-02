package com.dev9.plugin.sauce;

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
 * @goal start
 * @phase pre-integration-test
 */

public class StartMojo
        extends AbstractSauceConnectMojo {

    @Override
    public void executeGoal()
            throws MojoExecutionException {

        sauceConnectManager.startNewTunnel(logDirectory, true);

        if (sauceConnectManager.isTunnelActive()) {
            getLog().info("Successfully started Sauce Connect tunnel.  View logs in " + logDirectory.getAbsolutePath());
        } else {
            getLog().error("Failed to start Sauce Connect tunnel.  View logs in " + logDirectory.getAbsolutePath());
            throw new MojoExecutionException("Failed to start Sauce Connect tunnel.");
        }
    }
}
