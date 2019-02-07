package org.jboss.qa.cdi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;

@RequestScoped
public class CDIBean {
    private static final Logger LOG = Logger.getLogger(CDIBean.class);

    @Inject
    UserTransaction txn;
    
    @Inject
    TransactionManager tm;

    @Transactional(TxType.REQUIRED)
    public void goWithMe() {
        try {
            tm.rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            LOG.errorf(e, "rollback failed");
        }
        try {
            tm.setRollbackOnly();
        } catch (IllegalStateException | SystemException e) {
            LOG.errorf(e, "set rollback only failed");
        }
    }
}
