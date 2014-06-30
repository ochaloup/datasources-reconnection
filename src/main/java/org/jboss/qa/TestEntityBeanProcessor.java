package org.jboss.qa;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;
import org.jboss.qa.jpa.TestEntityHelperLocal;
import org.jboss.qa.jpa.TestEntityHelperRemote;

@Stateless
@Remote(TestEntityBeanProcessorRemote.class)
public class TestEntityBeanProcessor implements TestEntityBeanProcessorRemote {
  private static final Logger log = Logger.getLogger(TestEntityBeanProcessorRemote.class);
  
  @EJB
  TestEntityHelperRemote entityHelperRemote;
  
  @EJB
  TestEntityHelperLocal entityHelperLocal;
  
  public void doStuffRemote(String idKey) {
    log.info("doStuffRemote");
    entityHelperRemote.initTestEntity(idKey, 42);
    entityHelperRemote.updateTestEntity(idKey);
  }
  
  public void doStuffLocal(String idKey) {
    log.info("doStuffLocal");
    entityHelperLocal.initTestEntity(idKey, 42);
    entityHelperLocal.updateTestEntity(idKey);
  }
}
