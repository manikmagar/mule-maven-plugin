/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.deployment.arm;

import org.junit.Before;
import org.junit.Test;
import org.mule.tools.client.standalone.exception.DeploymentException;
import org.mule.tools.deployment.artifact.DomainDeployer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ArmDomainDeployerTest {

  private ArmDomainDeployer armDomainDeployer;
  private DomainDeployer domainDeployerMock;

  @Before
  public void setUp() {
    domainDeployerMock = mock(DomainDeployer.class);
    armDomainDeployer = new ArmDomainDeployer(domainDeployerMock);
  }

  @Test
  public void deployTest() throws DeploymentException {
    armDomainDeployer.deploy();

    verify(domainDeployerMock, times(1)).deployDomain();
    verify(domainDeployerMock, times(0)).undeployDomain();
  }

  @Test
  public void undeployTest() throws DeploymentException {
    armDomainDeployer.undeploy();

    verify(domainDeployerMock, times(1)).undeployDomain();
    verify(domainDeployerMock, times(0)).deployDomain();
  }

}
