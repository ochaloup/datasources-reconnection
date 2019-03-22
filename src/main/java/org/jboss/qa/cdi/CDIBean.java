package org.jboss.qa.cdi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.qa.TestBean;

@RequestScoped
public class CDIBean {
    private static final Logger LOG = Logger.getLogger(CDIBean.class);

    @Inject
    UserTransaction txn;
    
    @Inject
    TransactionManager tm;

    @Inject
    TransactionSynchronizationRegistry registry;

    @Inject
    CDIBean self;

    @Inject
    TestBean bean;

    @Transactional(TxType.REQUIRED)
    public void goWithMe() {
        try {
            LOG.infof("transaction manager '%s' and status '%s', registry is '%s'", tm, tm.getStatus(), registry);
        } catch (SystemException e) {
            LOG.error("Cannot get status of transaction", e);
        }

        self.goWithoutTxn();
    }

    public void goWithoutTxn() {
        try {
            LOG.infof("transaction manager '%s' and status '%s', registry is '%s'", tm, tm.getStatus(), registry);
            txn.setTransactionTimeout(0);
            txn.begin();
            bean.go();
            txn.commit();
        } catch (Exception e) {
            LOG.errorf(e, "Cannot begin and commit transaction '%s'", txn);
            try {
                txn.rollback();
            } catch (Exception re) {
                LOG.debugf(re, "Cannot rollback transaction '%s'", txn);
            }
        }
    }
}
