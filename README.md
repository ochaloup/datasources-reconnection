This is to be mean as a verification program for JCA connection validation feature.


1) You need to PostgreSQL driver being copied under name 'jdbc-driver.jar' in $JBOSS_HOME/standalone/deployments directory
2) copy some of the *-ds.xml to WEB-INF: cp src/main/templates/<something>-ds.xml src/main/webapp/WEB-INF/
3) mvn clean compile war:war
4) copy target/datasources-reconnection-*.war to $JBOSS_HOME/standalone/deployments/datasources-reconnection.war
5) cd $JBOSS_HOME
6) ./bin/standalone.sh
7) go to http://localhost:8080/datasources-reconnection/
8) stop database
9) go to http://localhost:8080/datasources-reconnection/
10) start database
11) go to http://localhost:8080/datasources-reconnection/

If database goes down and server tries do a request on it then after the database comes back to live the server 
is filled with invalid connections in connection pool and stays with exception that connection can't be established.

If you do have just simple datasource then step 11 will mean that you will get connection exception despite the fact that database is up.

As solution check https://access.redhat.com/site/solutions/156103
Generally you can

1) Flush the connection pool of datasource
/deployment=datasources-reconnection.war/subsystem=datasources/data-source=java\:jboss\/datasources\/TestDS:read-resource()

2) Define datasource for being validated on match (<validate-on-match>). see postgres-validateonmatch-ds.xml

4) Define datasource for being validate by background check (<background-validation>). see postgres-backgroundcheck-ds.xml


