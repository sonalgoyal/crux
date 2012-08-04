CRUX is a reporting application for HBase. Crux has been tested against
-
- CDH4
- Cloudera's distribution. CDH3 - Hadoop 0.20.2-CDH3u1
- Apache HBase 0.92.1
- Apache HBase 0.90.3 on Apache Hadoop 0.20.2 with Hadoop append.

Crux license 
-
- Crux license is Apache License v2

Why HBase ?
-
- HBase provides a low latency columnar storage for big data. HBase fits perfectly with the Hadoop stack, using HDFS for storage and providing out of the box support for Map Reduce. Data can be ingested into HBase from a traditional Map Reduce application, Pig or Hive, Cascading, Flume, Scribe, Hiho or Sqoop. Data can also be imported using HBase bulk loader

Why Crux ?
-
- Once you have collected your data in HBase, there is a need to expose it to business and technical users of your organization. 
The size of the data as well as the unstructured format makes it difficult to use a traditional reporting application with it. Crux uses native HBase integration to help you query your data. Crux has a web based report designer and viewer, making report creation and sharing easier. Crux comes with built in comparators for long, short, int, double, float, string and boolean datatypes and can create tables, graphs, scatter plots and other visuals for your data. Simple as well as composite rowkeys are supported via mapping Row Key Aliases. One can define filters on the row keys and perform get operations and range scans.Crux works with your schema and your data, there is no predefined schema for you to fit your data in.

Crux Design
-
- Crux uses the HBase Java client API, which is a fully featured way to access HBase. There are other clients available for HBase, for example Rest, Thrift and Avro. At the time of writing Crux, these clients do not expose the complete conditional querying capability needed by Crux. Then there are batch clients like Map Reduce, Hive handler, Pig and Cascading. These are great for performing batch analysis using HBase data. However, a reporting application needs faster response time than the batch nature of these. Crux thus uses the HBase Java Client API.

- Crux also uses mySQL to store the mapping of HBase schemas, connections and reports. The front end is built using Ajax, Dojo, Struts, with Hibernate. Crux uses open source software and comes with Apache License.

Crux Mailing List, Issue Reporting and Support :
-
- Join http://groups.google.com/group/cruxUsers to discuss crux features, issues and to seek help. You can also report bugs and request features at https://github.com/sonalgoyal/crux/issues.
Crux has built in help pages in the web interface. More info can also be found at http://www.nubetech.co/weblog.

Crux Documentation and user guide 
-
- Crux features, guides and news is available at http://nubetech.co/category/crux-2. Besides this, Crux has an extensive inbuilt guide per page to help you create your reports effortlessly.The mailing list is also a good source of information about Crux.

Using Crux
-
- Using Crux, one can query HBase tables and create reports to  analyze results. 
- To do this, there are a few simple steps.

Prerequisies:
-
  - A running HBase
  - A running MySQL instance
  - A servlet container like Tomcat.
  - Java installed. We used JDK 1.6
  - Maven

Once you have the prerequisite
-
a. Create database for crux in MySQL

 - create databse crux;
 - use crux;
 - Run the schema (crux/db/schema.sql) file in MySQL. On mysql prompt, 
 - source ${CRUX_HOME}/db/schema.sql
 - This creates the schema required for saving the report definitions.

b. Build crux(See instructions to build crux with Maven). 

c. Copy crux.jar to ${HBASE_HOME}/lib or edit ${HBASE_HOME}/conf/hbase-env.sh and add the jars location to the file.

 - For example,
 - # Extra Java CLASSPATH elements Optional
 - # export HBASE_CLASSPATH=
 - export HBASE_CLASSPATH="/home/crux/target/crux.jar"
 - Restart hbase 
 - Go to Hbase home/bin and then enter start-hbase.sh 
 - Then start hbase shell. 
 - This is needed as Crux has built in filters which work on the   server side to select the data you choose. 

d. Drop the war in tomcat/webapps and start tomcat by going to tomcat home/bin and enter startup.sh  
 
 - Alternatively, just run mvn jetty:run in Crux home.

e. Go to http://localhost:8080/crux and define your connection, mapping and report.

Instructions to build crux with Mave
-
- A. Update hibernate.properties(crux/) with your MySQL host, port, dbname, testDbName, user and password.
- B. Download struts2-fullhibernatecore-plugin-2.2.2-GA.jar from http://code.google.com/p/full-hibernate-plugin-for-struts2/downloads/detail?name=struts2-fullhibernatecore-plugin-2.2.2-GA.jar&can=2&q=
	and add to your local repository by executing command given below.
	mvn install:install-file -DgroupId=com.google.code -DartifactId=struts2-fullhibernatecore-plugin -Dversion=2.2.2-GA -Dpackaging=jar 
	-Dfile=< path/to/struts2-fullhibernatecore-plugin-2.2.2-GA.jar >
- C. Crux can be built against HBase 0.90.3(default) or against HBase 0.92.1. 
- Crux artifacts crux.war and crux.jar are created in crux/target/
- To build and create war against 0.90.3:
- Go to the base directory where pom.xml is located and enter
- mvn install -DskipTests(in order to skip tests) or mvn install to run tests and create war

For CDH4
-
- mvn -Dcdh4 install

Instructions to run test cases of crux with Maven
-
- mvn test

Instructions to set up the dev environment in Eclipse
-
- Add the Maven dependencies through the Eclipse m2e plugin and you should be good to go. 
- Please refer to committer's checklist.txt for further details 

Crux Limitations
-
- Crux is an HBase application, so the schema and the querying has to be designed accordingly.
- As far as possible, try to create row filters with  equals/greater than equals/less than 
- so as to leverage HBase's Get and Range Scan operations.  

Sample data
-
- Crux comes with sample data - you can refer testData/BseStock/README.txt for downloading BSE stock data for given list of scrips and populating hbase with it.
