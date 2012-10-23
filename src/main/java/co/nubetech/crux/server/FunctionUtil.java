package co.nubetech.crux.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.server.aggregate.GroupingAggregationImpl;
import co.nubetech.crux.server.functions.CruxAggregator;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.server.functions.CruxNonAggregator;
import co.nubetech.crux.util.CruxException;

public class FunctionUtil {
	
	private final static Logger logger = Logger.getLogger(FunctionUtil.class);
	
	public static Object getFunctionValue(Stack<CruxFunction> functions) throws CruxException {
		Object returnVal = null;
		for (CruxFunction fn : functions) {
			logger.debug("Trying to find the aggregate function, is it " + fn);
			if (fn.isAggregate()) {
				returnVal = ((CruxAggregator)fn).getAggregate();
				break;
			}			
		}
		logger.debug("Lets get to the final val now");
		for (CruxFunction fn : functions) {
			logger.debug("Trying the non agg function now, is it " + fn);
			if (!fn.isAggregate()) {
				logger.debug("Executing " );
				returnVal = ((CruxNonAggregator)fn).execute(returnVal);				
			}			
		}
		return returnVal;
	}
	
	/**
	 * Apply functions till you hit an aggregate
	 * @param value
	 * @param functions
	 */
	public static void applyAggregateFunctions(byte[] value, Stack<CruxFunction> functions) throws CruxException{
		//pop each function
		//if its an aggregate, get the value
		//else just apply
		//we will apply only till we meet an aggregate
		//CruxFunction fn = null;
		Object intermediateValue = value;
		int size = functions.size() - 1;
		for (CruxFunction fn : functions) {
			logger.debug("Function is " + fn + " and size is " + size);
			if (fn.isAggregate()) {
				logger.debug("applying aggregator " + fn);
				((CruxAggregator) fn).aggregate(intermediateValue);
				break;				
			}
			else {
				logger.debug("applying non aggregator " + fn);
				intermediateValue = ((CruxNonAggregator)fn).execute(value);
			}
		}
	}
	
	/**
	 * Apply functions till you hit an aggregate
	 * @param value
	 * @param functions
	 */
	public static void applyAggregateFunctions(Object obj, Stack<CruxFunction> functions) throws CruxException{
		//pop each function
		//if its an aggregate, get the value
		//else just apply
		//we will apply only till we meet an aggregate
		//CruxFunction fn = null;
		Object intermediateValue = obj;
		int size = functions.size() - 1;
		for (CruxFunction fn : functions) {
			logger.debug("Function is " + fn + " and size is " + size);
			if (fn.isAggregate()) {
				logger.debug("applying aggregator " + fn);
				((CruxAggregator) fn).aggregate(intermediateValue);
				break;				
			}
			else {
				logger.debug("applying non aggregator " + fn);
				intermediateValue = ((CruxNonAggregator)fn).execute(obj);
			}
		}
	}
	
	/**
	 * Get list of values
	 * all aggregations have been performed so far
	 * lets apply other functions on top of them and return the final values
	 * @param report
	 * @param functions
	 * @return
	 * @throws CruxException
	 */
	public static List getFunctionValueList(Report report, List<Stack<CruxFunction>> functions) throws CruxException{
		int index = 0;
		List returnList = new ArrayList();
		for (ReportDesign design: report.getDesigns()) {
			index++;
			//get each value and apply functions
			Stack<CruxFunction> designFn = functions.get(index++);
			returnList.add(FunctionUtil.getFunctionValue(designFn));
		}
		return returnList;
	}

}
