package org.jboss.qa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

@Singleton
@Startup
public class TestBeanTableCreator {
    private static final Logger LOGGER = Logger.getLogger(TestBeanTableCreator.class);

    @Resource(lookup = "java:jboss/datasources/ExampleDS")
    DataSource ds;

    @PostConstruct
    public void initCreateTestTable() {
        Connection c = null;

        try {
            c = ds.getConnection();
            Statement statement = c.createStatement();

            LOGGER.debugf("Creating table 'test of datasource '%s' and conection '%s'", ds.toString(), c.toString());

            String nativeCreate = c.nativeSQL("CREATE TABLE test (id int)");
            statement.execute(nativeCreate);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create table 'test'", e);
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
