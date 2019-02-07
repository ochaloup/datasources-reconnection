package org.jboss.qa;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ContainerManagedTxn {
    @EJB
    BeanManagedTxn beantxn;

    public void go() {
        System.out.println("container managed...");
        beantxn.go();
    }
}
