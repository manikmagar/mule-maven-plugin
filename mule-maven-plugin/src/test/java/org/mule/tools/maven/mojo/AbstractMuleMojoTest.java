/*
 * Mule ESB Maven Tools
 * <p>
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * <p>
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.maven.mojo;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mule.tools.artifact.archiver.api.PackageBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;

public class AbstractMuleMojoTest {
    protected static final String LIB = "lib";
    protected static final String MULE = "mule";
    protected static final String MUNIT = "munit";
    protected static final String POM_XML = "pom.xml";
    protected static final String PLUGINS = "plugins";
    protected static final String META_INF = "META-INF";
    protected static final String MULE_SRC = "mule-src";
    protected static final String TEST_MULE = "test-mule";
    protected static final String PACKAGE_NAME = "packageName";
    protected static final String MULE_CONFIG_XML = "mule-config.xml";
    protected static final String ARTIFACT_FILE_NAME = "artifactFile";
    protected static final String MUNIT_TEST_FILE_NAME = "munit-test.xml";
    protected static final String MULE_APP_PROPERTIES = "mule-app.properties";
    protected static final String PROJECT_ARTIFACT_ID = "project-artifact-id";
    protected static final String MULE_DEPLOY_PROPERTIES = "mule-deploy.properties";
    protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    protected Build buildMock;
    protected File munitFolder;
    protected File testMuleFolder;
    protected File destinationFile;
    protected File munitSourceFolder;
    protected MavenProject projectMock;
    protected File muleSourceFolderMock;
    protected MavenProjectHelper projectHelper;
    protected PackageBuilder packageBuilderMock;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public TemporaryFolder buildTemporaryFolder = new TemporaryFolder();

    @Before
    public void createTemporaryFolder() throws IOException {
        temporaryFolder.create();
        System.setOut(new PrintStream(outContent));
    }
}
