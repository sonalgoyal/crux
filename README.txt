Crux is a reporting application for HBase. Crux has been tested against

1. Apache HBase 0.90.3 on Apache Hadoop 0.20.2 with Hadoop append.
2. Cloudera's distribution. CDH3 - Hadoop 0.20.2-CDH3u1


Crux license
------------
Crux license is Apache License v2


Why HBase?
----------
HBase provides a low latency columnar storage for big data. HBase fits perfectly with the Hadoop stack, using HDFS for storage and
providing out of the box support for Map Reduce. Data can be ingested into HBase from a traditional Map Reduce application, 
Pig or Hive, Cascading, Flume, Scribe, Hiho or Sqoop. Data can also be imported using HBase bulk loader. 


Why Crux?
---------
Once you have collected your data in HBase, there is a need to expose it to business and technical users of your organization. 
The size of the data as well as the unstructured format makes it difficult to use a traditional
reporting application with it. Crux uses native HBase integration to help you query your data. Crux has a web based report designer and viewer,
making report creation and sharing easier. Crux comes with built in comparators for long, short, int, double, float, string and boolean datatypes
and can create tables, graphs, scatter plots and other visuals for your data. 
Simple as well as composite rowkeys are supported via mapping Row Key Aliases. 
One can define filters on the row keys and perform get operations and range scans. 
Crux works with your schema and your data, there is no predefined schema for you to fit your data in. 


Crux Design
-----------
Crux uses the HBase Java client API, which is a fully featured way to access HBase. 
There are other clients available for HBase, for example Rest, Thrift and Avro. At the time of writing Crux, these clients
do not expose the complete conditional querying capability needed by Crux. 
Then there are batch clients like Map Reduce, Hive handler, Pig and Cascading. 
These are great for performing batch analysis using HBase data. 
However, a reporting application needs faster response time than the batch nature of these. 
Crux thus uses the HBase Java Client API. 

Crux also uses mySQL to store the mapping of HBase schemas, connections and reports. 
The front end is built using Ajax, Dojo, Struts, with Hibernate. Crux uses open source software and comes with Apache License.


Crux Mailing List, Issue Reporting and Support
----------------------------------------------
Join http://groups.google.com/group/cruxUsers to discuss crux features, issues and to seek help.
You can also report bugs and request features at https://github.com/sonalgoyal/crux/issues.
Crux has built in help pages in the web interface. More info can also be found at http://www.nubetech.co/weblog

Crux Documentation and user guide
---------------------------------
Crux features, guides and news is available at http://nubetech.co/category/crux-2. 
Besides this, Crux has an extensive inbuilt guide per page to help you create your reports effortlessly.
The mailing list is also a good source of information about Crux.

Using Crux
---------- 
Using Crux, one can query HBase tables and create reports to analyze results. 
To do this, there are a few simple steps.

Prerequisies:
- A running HBase
- A running MySQL instance
- A servlet container like Tomcat.
- Java installed. We used JDK 1.6
- Ant

Once you have the prerequisites

a. Build crux(See instructions to build crux with ant). 
The zip file has the crux war, schema and crux.jar.
b. Run the schema file in MySQL. On mysql prompt, source <path to file>/schema.sql
This creates the schema required for saving the report definitions.  
c. Copy crux.jar to HBase Home/lib or edit HBase home/conf/hbase-env.sh and add the jars location to the file. 
For example,

# Extra Java CLASSPATH elements.  Optional.
# export HBASE_CLASSPATH=
export HBASE_CLASSPATH="/home/nube/crux.jar"

Restart hbase. 
This is needed as Crux has built in filters which work on the server side to select the data you choose. 

d. Drop the war in tomcat/webapps and start tomcat.  
e. Go to http://machine:8080/crux and define your connection, mapping and report.

Instructions to build crux with ANT
-----------------------------------
A. Update hibernate.cfg.xml with your MySQL connection.url, connection.username and connection.password.
B. Build and create zip:
	ant deploy
	(crux-version.zip is created in crux/deploy/)
	

Instructions to run test cases of crux with ANT
-----------------------------------------------
ant test


Instructions to set up the dev environment in Eclipse
-------------------------------------------------
After copying crux in your eclipse project, please add all jars from crux/lib_test/ and
crux/lib_test/run_time/ to your eclipse java build path libraries.


Crux Limitations
----------------
Crux is an HBase application, so the schema and the querying has to be designed accordingly.
As far as possible, try to create row filters with  equals/greater than equals/less than 
so as to leverage HBase's Get and Range Scan operations.  

Sample data
-----------
Crux comes with sample data - you can refer to testData/BseStock/README.txt for downloading BSE stock data for given list of scrips
and populating hbase with it.
