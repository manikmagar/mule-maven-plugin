/*
 * Mule ESB Maven Tools
 * <p>
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * <p>
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tools.api.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.mule.tools.api.packager.PackagerFolders.REPOSITORY;
import static org.mule.tools.api.classloader.model.util.DefaultMavenRepositoryLayoutUtils.getFormattedFileName;
import static org.mule.tools.api.classloader.model.util.DefaultMavenRepositoryLayoutUtils.getFormattedOutputDirectory;
import static org.mule.tools.api.classloader.model.util.DefaultMavenRepositoryLayoutUtils.getPomFileName;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.mule.tools.api.ContentGenerator;
import org.mule.tools.api.classloader.model.ClassLoaderModel;
import org.mule.tools.api.packager.PackagingType;

public class ArtifactInstaller {

  private static final String POM_FILE_NAME = "pom.xml";
  private Log log;

  public ArtifactInstaller(Log log) {
    this.log = log;
  }

  public void installArtifact(File repositoryFile, Artifact artifact, Optional<ClassLoaderModel> classLoaderModel)
      throws MojoExecutionException {
    checkArgument(artifact != null, "Artifact to be installed should not be null");
    File artifactFolderDestination = getFormattedOutputDirectory(repositoryFile, artifact);

    if (!artifactFolderDestination.exists()) {
      artifactFolderDestination.mkdirs();
    }

    try {
      generateArtifactFile(artifact, artifactFolderDestination, repositoryFile);
      generateDependencyDescriptorFile(artifact, artifactFolderDestination, classLoaderModel);
    } catch (IOException e) {
      throw new MojoExecutionException(
                                       format("There was a problem while copying the artifact [%s] file [%s] to the application local repository",
                                              artifact.toString(), artifact.getFile().getAbsolutePath()),
                                       e);
    }
  }

  private void generateArtifactFile(Artifact artifact, File artifactFolderDestination, File repositoryFile) throws IOException {
    String artifactFilename = getFormattedFileName(artifact);

    File destinationArtifactFile = new File(artifactFolderDestination, artifactFilename);
    log.info(format("Adding artifact <%s%s>",
                    REPOSITORY,
                    destinationArtifactFile.getAbsolutePath()
                        .replaceFirst(Pattern.quote(repositoryFile.getAbsolutePath()),
                                      "")));

    copyFile(artifact.getFile(), destinationArtifactFile);
  }

  private void generateDependencyDescriptorFile(Artifact artifact, File artifactFolderDestination,
                                                Optional<ClassLoaderModel> classLoaderModel)
      throws IOException {
    if (classLoaderModel.isPresent()) {
      ContentGenerator.createClassLoaderModelJsonFile(classLoaderModel.get(), artifactFolderDestination);
    } else {
      generatePomFile(artifact, artifactFolderDestination);
    }
  }

  private void generatePomFile(Artifact artifact, File artifactFolderDestination) throws IOException {
    String artifactPomFilename = getPomFileName(artifact);
    File srcPomFile = new File(artifact.getFile().getParent(), artifactPomFilename);
    File destinationPomFile = new File(artifactFolderDestination, artifactPomFilename);
    if (!srcPomFile.exists()) {
      srcPomFile = new File(artifact.getFile().getParent(), POM_FILE_NAME);
    }
    copyFile(srcPomFile, destinationPomFile);
  }
}
