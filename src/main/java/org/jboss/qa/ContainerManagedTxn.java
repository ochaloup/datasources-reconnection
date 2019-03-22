package org.jboss.qa;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.jboss.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ContainerManagedTxn {
    private static final Logger LOG = Logger.getLogger(ContainerManagedTxn.class);

    @EJB
    BeanManagedTxn beantxn;

    @Resource(lookup = "java:/TransactionManager")
    TransactionManager tm;

    @Resource
    TransactionSynchronizationRegistry registry;

    public void go() {
        System.out.println("container managed...");

        try {
            LOG.infof("transaction manager '%s' and status '%s', registry is '%s'", tm, tm.getStatus(), registry);
        } catch (SystemException e) {
            LOG.error("Cannot get status of transaction", e);
        }

        beantxn.go();
    }
}
