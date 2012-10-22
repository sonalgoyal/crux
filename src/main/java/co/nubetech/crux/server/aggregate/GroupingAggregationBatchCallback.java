package co.nubetech.crux.server.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.client.coprocessor.Batch;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.server.ServerUtil;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationBatchCallback implements Batch.Callback<List<List>>{
	
	public List<List> results;
	public Report report;
	List<Stack<CruxFunction>> functions;	
	
	public GroupingAggregationBatchCallback(Report report) throws CruxException{
		this.report = report;
		results = new ArrayList<List>();
		functions = report.getFunctions();		
	}
	
	/**
	 * See if there is a way to do something with the results as they come by
	 */
	public synchronized void update(byte[] region, byte[] row, List<List> result) {
		try {
			int index = 0;
			for (List resultRow: results) {
				Stack<CruxFunction> designFn = functions.get(index++);
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		} 
	}
	
	public void getAggregates() throws CruxException{
		List<Stack<CruxFunction>> functions = report.getFunctions();
		GroupBys groupBys = report.getGroupBys();
		int index = 0;
		for (List perRegionServer: results) {
			for (ReportDesign design: report.getDesigns()) {
				index++;
				//get each value and apply functions
				Stack<CruxFunction> designFn = functions.get(index++);
				//Object value = 
				//applyNonAggregateFunctions(value, designFn);					 
			}		
		}
	}

}
