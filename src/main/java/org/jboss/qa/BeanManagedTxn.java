package org.jboss.qa;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.qa.jpa.TestEntityHelperLocal;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BeanManagedTxn {
    @Inject
    private UserTransaction txn;

    @EJB
    TestEntityHelperLocal entityHelperLocal;

    public void go() {
        System.out.println("bean managed...");
        try {
            txn.begin();
            entityHelperLocal.initTestEntity("somethighsomethig", 33);
            txn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
