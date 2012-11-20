package co.nubetech.crux.server.aggregate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.ipc.ProtocolSignature;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.server.FunctionUtil;
import co.nubetech.crux.server.ServerUtil;
import co.nubetech.crux.server.functions.CruxAggregator;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.server.functions.CruxNonAggregator;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationImpl extends BaseEndpointCoprocessor implements 
	GroupingAggregationProtocol{
	
	private final static Logger logger = Logger.getLogger(GroupingAggregationImpl.class);
	public static final long VERSION = 1L;
	
	@Override
	public ProtocolSignature getProtocolSignature(
	    String protocol, long version, int clientMethodsHashCode)
	    throws IOException {
	    if (GroupingAggregationImpl.class.getName().equals(protocol)) {
	    	return new ProtocolSignature(GroupingAggregationImpl.VERSION, null);
		}
		throw new IOException("Unknown protocol: " + protocol);
	  }

	/**
	 * We are here
	 * means that the caller has already decided that
	 * the server needs to be executed
	 * which means at least one of the design functions
	 * is an aggregate
	 *  
	 */
	@Override
	public List<List> getAggregates(Scan scan, Report report) throws CruxException{
		List<List> returnList = new ArrayList<List>();
		InternalScanner scanner = null;
		try {
			//understand the report
			//see what is the group by
			//get the designs
			//for each design
			//get the col or row alias and the function to be applied.
			//create list of functions to be applied.
			Collection<ReportDesign> designs = report.getDesigns();
			List<Stack<CruxFunction>> functions = report.getFunctions();		
			GroupBys groupBys = report.getGroupBys();
			//open the scan
			//get values of data for each group by
			//put in hash
			//hash key group by objects, hash value List of function applied values.
			//we are keeping everything in memory, as we are assuming that final report holds less data
			//we are going to be dealing with preaggregated data only, umm..lets see..
			//may have to revise this
			if (groupBys == null) {
				 scanner = ((RegionCoprocessorEnvironment) getEnvironment())
					        .getRegion().getScanner(scan);
				 //just simple function application, but an aggregate like min/max is lurking in somewhere
				 //without a groupby ..ouch
				 byte[] value = null;
				 List<KeyValue> results = new ArrayList<KeyValue>();
				 boolean hasMoreRows = false;
				 do {
					 hasMoreRows = scanner.next(results);
					 logger.debug("Rowkey is " + results.get(0).getRow());
					 int index = 0;
					 for (ReportDesign design: designs) {
						 //get each value and apply functions
						 Stack<CruxFunction> designFn = functions.get(index++);
						 //convert and apply
						 //see if the design is on row or column alias
						 Alias alias = ServerUtil.getAlias(design);
						 value = ServerUtil.getValue(results, alias);
						 FunctionUtil.applyAggregateFunctions(value, designFn);					 
					 }					 
				}
				while (hasMoreRows); 	
				//we have applied functions to each row of the scan
				//now lets get final aggregated values and populate
				returnList.add(FunctionUtil.getSemiAggregatedFunctionValueList(report, functions));
			}
			else {
				//handle group bys
				throw new CruxException("Unsupported stuff so far..to come soon :");	
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new CruxException("Error processing aggregates " + e);
		}
		finally {
		      if (scanner != null) {
		    	  try {
		    		  scanner.close();
		    	  }
		    	  catch(IOException io) {
		    		  throw new CruxException("Error closing scanner", io);
		    	  }
		      }
		}
		logger.info("Value from this region is "
		        + ((RegionCoprocessorEnvironment) getEnvironment()).getRegion()
		            .getRegionNameAsString() + ": " + returnList);
		for (List list: returnList) {
			logger.debug("New Row begins");
			for (Object row: list) {
				logger.debug("Row is " + row);				
			}			
		}
		
		return returnList;
	}
	
		
		
}
