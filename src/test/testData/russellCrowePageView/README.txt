Instruction to populate data in hbase for Russell Crowe page views.
-------------------------------------------------------------------
1. Open HBase shell and create table as following:-
	create 'russellCrowePageView', 'pageView'	
2. Now copy Russell_Crowe.txt in some directory.
3. Run PopulateDataSet.java with argument having path of the directory which contains the Russell_Crowe.txt.