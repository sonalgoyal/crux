package co.nubetech.crux.server.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.server.FunctionUtil;
import co.nubetech.crux.server.ServerUtil;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationBatchCallback implements Batch.Callback<List<List>>{
	private final static Logger logger = Logger.getLogger(GroupingAggregationBatchCallback.class);
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
			if (report.getGroupBys() == null) {
				for (List resultRow: results) {
					Stack<CruxFunction> designFn = functions.get(index++);
					for (Object val: resultRow) {
						FunctionUtil.applyAggregateFunctions(val, designFn);
					}
				}
			}
			else {
				logger.debug("group by is not supported so far");
			}
		}
		catch(Throwable e) {
			e.printStackTrace();
		} 
	}
	
	public List<List> getAggregates() throws CruxException{
		List<Stack<CruxFunction>> functions = report.getFunctions();
		GroupBys groupBys = report.getGroupBys();
		if (groupBys == null) {
			return FunctionUtil.getAggregatedFunctionValueList(report, functions);
		}
		else {
			return null;
		}
		
	}

}
