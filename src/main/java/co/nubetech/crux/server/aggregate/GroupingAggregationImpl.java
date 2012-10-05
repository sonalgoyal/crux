package co.nubetech.crux.server.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;

public class GroupingAggregationImpl extends BaseEndpointCoprocessor implements 
	GroupingAggregationProtocol{

	@Override
	public List<List> getAggregates(Scan scan, Report report) {
		List<List> returnList = new ArrayList<List>();
		//understand the report
		//see what is the group by
		//get the designs
		//for each design
		//get the col or row alias and the function to be applied.
		//create list of functions to be applied.
		//for now, lets do only one function per alias
				
		//open the scan
		//get values of data for each group by
		//put in hash
		//hash key group by objects, hash value List of function applied values.
		//we are keeping everything in memory, as we are assuming that final report holds less data
		//we are going to be dealing with preaggregated data only, umm..lets see..
		//may have to revise this
		
		
		return returnList;
	}
	
	protected List<Stack<CruxAggregator>> getAggregators(Report report) {
		List<Stack<CruxAggregator>> aggregators = new ArrayList<Stack<CruxAggregator>>();
		for (ReportDesign design: report.getDesigns()) {
			Collection<ReportDesignFunction> functions = design.getReportDesignFunctionList();
			
		}
		return aggregators;
	}
	
	

}
