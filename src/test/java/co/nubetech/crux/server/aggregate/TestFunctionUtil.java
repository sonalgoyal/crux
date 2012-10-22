package co.nubetech.crux.server.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Stack;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.coprocessor.CoprocessorHost;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.Util;
import co.nubetech.crux.server.FunctionUtil;
import co.nubetech.crux.server.functions.Ceil;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.server.functions.SumAggregator;
import co.nubetech.crux.server.functions.UpperCase;
import co.nubetech.crux.util.CruxException;

public class TestFunctionUtil {
		
	@Test
	public void testApplyFunctionsAggregateFirst() throws CruxException{
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		SumAggregator summer = new SumAggregator();
		xFnStack.push(summer);
		xFnStack.push(new Ceil());
		byte[] value = Bytes.toBytes(new Double(54.5d));
		FunctionUtil.applyAggregateFunctions(value, xFnStack);
		byte[] value1 = Bytes.toBytes(new Double(5.25d));
		FunctionUtil.applyAggregateFunctions(value1, xFnStack);
		assertEquals(59.75d, summer.getAggregate());
	}
	
	@Test
	public void testApplyFunctionsNonAggregateFirst() throws CruxException{
		GroupingAggregationImpl impl = new GroupingAggregationImpl();
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		SumAggregator summer = new SumAggregator();
		xFnStack.push(new Ceil());
		xFnStack.push(summer);
		byte[] value = Bytes.toBytes(new Double(54.5d));
		FunctionUtil.applyAggregateFunctions(value, xFnStack);
		byte[] value1 = Bytes.toBytes(new Double(5.25d));
		FunctionUtil.applyAggregateFunctions(value1, xFnStack);
		assertEquals(61d, summer.getAggregate());
	}
	
	@Test
	public void testApplyFunctionsAllNonAggregate() throws CruxException{
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		xFnStack.push(new UpperCase());
		byte[] value = Bytes.toBytes(new String("rowKey"));
		FunctionUtil.applyAggregateFunctions(value, xFnStack);		
	}
	
	@Test
	public void testApplyFunctionsOnlyAggregate() throws CruxException{
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		SumAggregator summer = new SumAggregator();
		xFnStack.push(summer);
		byte[] value = Bytes.toBytes(new Double(54.5d));
		FunctionUtil.applyAggregateFunctions(value, xFnStack);
		byte[] value1 = Bytes.toBytes(new Double(5.25d));
		FunctionUtil.applyAggregateFunctions(value1, xFnStack);
		assertEquals(59.75d, summer.getAggregate());
	}
	
	@Test
	public void testGetValueNonAggregateFirst() throws CruxException{
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		SumAggregator summer = new SumAggregator();
		summer.aggregate(Bytes.toBytes(new Double(54.5d)));
		xFnStack.push(new Ceil());
		xFnStack.push(summer);
		assertEquals(55d, FunctionUtil.getFunctionValue(xFnStack));
	}
	
	@Test
	public void testGetValueAggregateFirst() throws CruxException{
		Stack<CruxFunction> xFnStack = new Stack<CruxFunction>();
		SumAggregator summer = new SumAggregator();
		summer.aggregate(Bytes.toBytes(new Double(54.5d)));
		xFnStack.push(summer);
		xFnStack.push(new Ceil());
		assertEquals(55d, FunctionUtil.getFunctionValue(xFnStack));
	}
	
	

}
