This is to be mean as a verification program for JCA connection validation feature.


<ol>
<li>You need to PostgreSQL driver being copied under name 'jdbc-driver.jar' in $JBOSS_HOME/standalone/deployments directory</li>
<li>copy some of the *-ds.xml to WEB-INF: cp src/main/templates/<something>-ds.xml src/main/webapp/WEB-INF/</li>
<li>mvn clean compile war:war</li>
<li>copy target/datasources-reconnection-*.war to $JBOSS_HOME/standalone/deployments/datasources-reconnection.war</li>
<li>cd $JBOSS_HOME</li>
<li>./bin/standalone.sh</li>
<li>go to http://localhost:8080/datasources-reconnection/</li>
<li>stop database</li>
<li>go to http://localhost:8080/datasources-reconnection/</li>
<li>start database</li>
<li>go to http://localhost:8080/datasources-reconnection/</li>
</ol>

If database goes down and server tries do a request on it then after the database comes back to live the server 
is filled with invalid connections in connection pool and stays with exception that connection can't be established.

If you do have just simple datasource then step 11 will mean that you will get connection exception despite the fact that database is up.

As solution check https://access.redhat.com/site/solutions/156103
Generally you can

<b>1)</b> Flush the connection pool of datasource (after database is restarted - step 10)
/deployment=datasources-reconnection.war/subsystem=datasources/data-source=java\:jboss\/datasources\/TestDS:read-resource()

<b>2)</b> Define datasource for being validated on match (&lt;validate-on-match&gt;). see postgres-validateonmatch-ds.xml

<b>3)</b> Define datasource for being validate by background check (&lt;background-validation&gt;). see postgres-backgroundcheck-ds.xml


