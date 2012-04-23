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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

abstract class AbstractSauceConnectMojo
        extends AbstractMojo {

    public static final String SAUCELABS_USER = "SAUCELABS_USER";
    public static final String SAUCELABS_KEY = "SAUCELABS_KEY";

    SauceConnectManager sauceConnectManager = null;

    /**
     * Location to run Sauce Connect.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    File logDirectory;

    /**
     * Skip migration actions.
     *
     * @parameter expression="${sauce-connect.skip}" default-value="false"
     */
    boolean skip;


    /**
     * Sauce Labs user name.  Can be pulled from the environment or system properties, alternately.
     *
     * @parameter expression="${sauce-connect.user}"
     */
    String sauceLabsUser;


    /**
     * Sauce Labs key.  Can be pulled from the environment or system properties, alternately.
     *
     * @parameter expression="${sauce-connect.key}"
     */
    String sauceLabsKey;

    public void execute()
            throws MojoExecutionException {
        if (!skip) {
            initialize();
            executeGoal();
        }
    }

    abstract void executeGoal() throws MojoExecutionException;

    private void initialize() throws MojoExecutionException {
        if (sauceLabsUser == null) {
            sauceLabsUser = Configuration.getValue(SAUCELABS_USER);
        }
        if (sauceLabsKey == null) {
            sauceLabsKey = Configuration.getValue(SAUCELABS_KEY);
        }

        if (sauceLabsUser == null || sauceLabsKey == null) {
            throw new MojoExecutionException(String.format("Failed to initialize.  Please provide " +
                    "configuration properties sauce-connect.user and sauce-connect.key or a system " +
                    "or environment property set to %s and %s.", SAUCELABS_USER, SAUCELABS_KEY));
        }

        sauceConnectManager = new SauceConnectManager(sauceLabsUser, sauceLabsKey, getLog());
    }
}

