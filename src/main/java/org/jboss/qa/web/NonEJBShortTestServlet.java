package org.jboss.qa.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;
import org.jboss.qa.jpa.TestEntity;


@WebServlet(name="NonEjbServlet", urlPatterns={"/nonejb2"})
public class NonEJBShortTestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(NonEJBShortTestServlet.class);
    
    @Inject
    UserTransaction txn;

    @PersistenceContext(unitName = "jbossts-crashrec")
    private EntityManager em;
       
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                txn.begin();

                em.persist(new TestEntity("myid", 8));
                
                txn.commit();
            } catch (NotSupportedException beginExceptions) {
                LOG.errorf(beginExceptions, "Cannot start transaction '%s' because of '%s'",
                        txn, beginExceptions.getMessage());
            } catch (SecurityException | IllegalStateException | RollbackException 
                    | HeuristicMixedException | HeuristicRollbackException commitExceptions) {
                LOG.errorf(commitExceptions, "Cannot commit transaction '%s' because of '%s'",
                        txn,commitExceptions.getMessage());
            } catch (SystemException systemException) {
                LOG.errorf(systemException, "Unexpected error condition on work with transaction: '%s'", txn);
            }

    }
}

