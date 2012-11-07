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
	private List<List> results;
	private Report report;
	private List<Stack<CruxFunction>> functions;	
	
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
			
			if (report.getGroupBys() == null) {
				logger.debug("Updating for " + region);
				for (List resultRow: result) {
					int index = 0;
					for (Object val: resultRow) {
						Stack<CruxFunction> designFn = functions.get(index++);
						logger.debug("Applying aggregate fn for " + val);
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
		GroupBys groupBys = report.getGroupBys();
		if (groupBys == null) {
			List returnList = new ArrayList();
			returnList.add(FunctionUtil.getAggregatedFunctionValueList(report, functions));
			return returnList;
		}
		else {
			return null;
		}
		
	}

}
