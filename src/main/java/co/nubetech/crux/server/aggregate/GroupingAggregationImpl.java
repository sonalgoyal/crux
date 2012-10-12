package co.nubetech.crux.server.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.server.functions.Conversion;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationImpl extends BaseEndpointCoprocessor implements 
	GroupingAggregationProtocol{
	
	private final static Logger logger = Logger.getLogger(GroupingAggregationImpl.class);


	@Override
	public List<List> getAggregates(Scan scan, Report report) throws CruxException{
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
	
	protected List<Stack<CruxFunction>> getAggregators(Report report) throws CruxException{
		
		List<Stack<CruxFunction>> aggregators = new ArrayList<Stack<CruxFunction>>();
		try {
			for (ReportDesign design: report.getDesigns()) {
				logger.debug("Finding functions for design: " + design);
				Collection<ReportDesignFunction> functions = design.getReportDesignFunctionList();
				Stack<CruxFunction> functionStack = new Stack<CruxFunction>();
				for (ReportDesignFunction function: functions) {
					functionStack.push((CruxFunction) Class.forName(function.getFunction().getFunctionClass()).
							newInstance());
				}
				aggregators.add(functionStack);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new CruxException("Unable to generate the functions " + e);
		}
		return aggregators;
	}
	
	

}
