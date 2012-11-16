package co.nubetech.crux.server.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.TestingUtil;
import co.nubetech.crux.server.functions.AverageAggregator;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.server.functions.SumAggregator;
import co.nubetech.crux.util.CruxException;

public class TestGABatchCallback {
	
	@Test
	public void testUpdate() throws CruxException{
		Report report = TestingUtil.getReportNoGroupBy();
		GroupingAggregationBatchCallback callback = new GroupingAggregationBatchCallback(report);
		
		List<List> regionServer1 = new ArrayList<List>();
		ArrayList regionServerValues = new ArrayList();
		regionServerValues.add(1234d);
		regionServerValues.add(12.34d);
		regionServerValues.add(123.4d);
		regionServer1.add(regionServerValues);
		callback.update(null, null, regionServer1);	
		
		List<List> returnList = callback.getAggregates();
		//assertEquals(1, returnList.size());
		//assertEquals(3, returnList.get(0).size());
		List result = returnList.get(0);
		for (Object r: result) {
			System.out.println("After first update, our values are " + r);
			
		}
		
		List<List> regionServer2 = new ArrayList<List>();
		ArrayList regionServer2Values = new ArrayList();
		regionServer2Values.add(1244d);
		regionServer2Values.add(12.04d);
		regionServer2Values.add(123.04d);
		regionServer2.add(regionServer2Values);
		callback.update(null, null, regionServer2);
		
		returnList = callback.getAggregates();
		assertEquals(1, returnList.size());
		assertEquals(3, returnList.get(0).size());
		result = returnList.get(0);
		for (Object r: result) {
			System.out.println(r);
		}
		assertEquals(2478d, (Double) result.get(0), 0.001d);
		assertEquals(13d, (Double) result.get(1), 0.001d);
		assertEquals(123l, ((Long) result.get(2)).longValue());
		
		
	}
	
	

}
