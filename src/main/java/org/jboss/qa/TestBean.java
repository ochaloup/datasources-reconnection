package org.jboss.qa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

@Stateless
public class TestBean {
    private static final Logger LOGGER = Logger.getLogger(TestBean.class);

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    DataSource ds;

    @Resource
    EJBContext ctx;

    public void go() {
        Connection c = null;

        try {
            c = ds.getConnection();
            Statement statement = c.createStatement();

            LOGGER.debugf("Inserting to table 'test' id to 3, of datasource '%s' and conection '%s'", ds.toString(), c.toString());

            // String nativeCreate = c.nativeSQL("CREATE TABLE test (id int)");
            String nativeInsert = c.nativeSQL("INSERT INTO test (id) VALUES (3)");
            statement.execute(nativeInsert);
            ctx.setRollbackOnly();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot insert to table 'test' id = 3", e);
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e2) {
                    LOGGER.debugf("e2", "Cannot close connection '%s' on creating table test", c);
                }
            }
        }

    }
}