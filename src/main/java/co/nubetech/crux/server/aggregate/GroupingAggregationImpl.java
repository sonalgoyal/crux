package co.nubetech.crux.server.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.server.functions.CruxAggregator;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.server.functions.CruxNonAggregator;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationImpl extends BaseEndpointCoprocessor implements 
	GroupingAggregationProtocol{
	
	private final static Logger logger = Logger.getLogger(GroupingAggregationImpl.class);

	/**
	 * We are here
	 * means that the caller has already decided that
	 * the server needs to be executed
	 * which means at least one of the design functions
	 * is an aggregate
	 * Thsi function is called without a group by
	 */
	@Override
	public List getAggregates(Scan scan, Report report) throws CruxException{
		List returnList = new ArrayList();
		try {
			
			//understand the report
			//see what is the group by
			//get the designs
			//for each design
			//get the col or row alias and the function to be applied.
			//create list of functions to be applied.
			Collection<ReportDesign> designs = report.getDesigns();
			List<Stack<CruxFunction>> functions = getFunctions(report);		
			GroupBys groupBys = report.getGroupBys();
			//open the scan
			//get values of data for each group by
			//put in hash
			//hash key group by objects, hash value List of function applied values.
			//we are keeping everything in memory, as we are assuming that final report holds less data
			//we are going to be dealing with preaggregated data only, umm..lets see..
			//may have to revise this
			 InternalScanner scanner = ((RegionCoprocessorEnvironment) getEnvironment())
				        .getRegion().getScanner(scan);
			 //just simple function application, but an aggregate like min/max is lurking in somewhere
			 //without a groupby ..ouch
			 byte[] value = null;
			 List<KeyValue> results = new ArrayList<KeyValue>();
			 boolean hasMoreRows = false;
			 do {
				 hasMoreRows = scanner.next(results);
				 int index = 0;
				 for (ReportDesign design: designs) {
					 index++;
					 //get each value and apply functions
					 Stack<CruxFunction> designFn = functions.get(index++);
					 //convert and apply
					 //see if the design is on row or column alias
					 Alias alias = getAlias(design);
					 value = getValue(results, alias, report);
					 applyFunctions(value, designFn);					 
				 }					 
			}
			while (hasMoreRows); 	
			//we have applied functions to each row of the scan
			//now lets get final values and populate
			for (ReportDesign design: designs) {
				returnList.add(null);
			}			
		}
		catch(Exception e) {
			throw new CruxException("Error processing aggregates " + e);
		}
		
		return returnList;
	}
	
	/**
	 * Apply functions
	 * @param value
	 * @param functions
	 */
	public void applyFunctions(byte[] value, Stack<CruxFunction> functions) throws CruxException{
		//pop each function
		//if its an aggregate, get the value
		//else just apply
		//we will apply only till we meet an aggregate
		CruxFunction fn = null;
		Object intermediateValue = value;
		while ((fn = functions.pop()) != null) {
			applyFunction(value, fn);
		}
	}
	
	public void applyFunction(Object value, CruxFunction function) throws CruxException {
		if (function instanceof CruxAggregator) {
			((CruxAggregator) function).aggregate(value);			
		}
		else {
			((CruxNonAggregator)function).execute(value);
		}
	}
	/**
	 * This is not the most optimized pience of code
	 * we should go to the byte buffers
	 * lets clean this later once the functionality works end to end
	 * @param results
	 * @param alias
	 * @return
	 */
	protected byte[] getValue(List<KeyValue> results, Alias alias, Report report) {
		byte[] value = null;
		if (alias instanceof RowAlias) {
			value = results.get(0).getKey();
		}
		else {
			ColumnAlias colAlias = (ColumnAlias) alias;
			String family = colAlias.getColumnFamily();
			String qualifier = colAlias.getQualifier();
			byte[] familyBytes = family.getBytes();
			byte[] qualifierBytes = qualifier.getBytes();
			for (KeyValue kv: results) {
				if (kv.getFamily().equals(familyBytes)) {
					if (kv.getQualifier().equals(qualifierBytes)) {
						value = kv.getValue();
						break;
					}
				}
			}
		}
		return value;
	}
	
	protected Alias getAlias(ReportDesign design) {
		Alias alias = design.getRowAlias();
		 if (alias == null) {
			 alias = design.getColumnAlias(); 
		 }
		 return alias;
	}
	
	
	
	protected List<Stack<CruxFunction>> getFunctions(Report report) throws CruxException{
		
		List<Stack<CruxFunction>> aggregators = new ArrayList<Stack<CruxFunction>>();
		try {
			for (ReportDesign design: report.getDesigns()) {
				logger.debug("Finding functions for design: " + design);
				Collection<ReportDesignFunction> functions = design.getReportDesignFunctionList();
				Stack<CruxFunction> functionStack = new Stack<CruxFunction>();
				if (functions != null) {
					for (ReportDesignFunction function: functions) {
						logger.debug("Creating function class for " + function);
						functionStack.push((CruxFunction) Class.forName(function.getFunction().getFunctionClass()).
								newInstance());
					}
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
