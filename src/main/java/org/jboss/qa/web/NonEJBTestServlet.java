package org.jboss.qa.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.jboss.qa.jpa.TestEntity;


@WebServlet(name="NonEjbServlet", urlPatterns={"/nonejb"})
public class NonEJBTestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Inject
    UserTransaction txn2;

    @Resource(lookup = "java:comp/UserTransaction")
    UserTransaction txn;

    @Inject
    TransactionManager tm;

    @PersistenceContext(unitName = "jbossts-crashrec")
    private EntityManager em;

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    DataSource ds;
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<h4>TestServlet NON EJB at your service!</h4>");
        out.println("<p>before transaciton: finding entity with id 'myid': " + em.find(TestEntity.class, "myid"));
        try {
            txn.begin();

            out.print("<p>txn is: " + txn + ", tm: " + tm);

            em.persist(new TestEntity("myid", 8));

            Connection conn = ds.getConnection();
            String nativeInsert = conn.nativeSQL("INSERT INTO datasourcetestjpa (id,a) VALUES ('insertid', 4)");
            Statement statement = conn.createStatement();
            statement.execute(nativeInsert);

            txn.rollback();

            out.println("<p>finding entity with id 'myid': " + em.find(TestEntity.class, "myid"));
            out.println("<p>finding entity with id 'insertid': " + em.find(TestEntity.class, "insertid"));
        } catch (NotSupportedException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

