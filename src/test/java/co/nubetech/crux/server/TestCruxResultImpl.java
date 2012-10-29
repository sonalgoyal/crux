package co.nubetech.crux.server;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.TestingUtil;
import co.nubetech.crux.util.CruxException;

public class TestCruxResultImpl {
	
	@Test
	public void testNextNonAggregate() throws CruxException{
		//ceil is applied
		Result result = mock(Result.class);
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		double dbl = 1234567.8d;
		when(result.getRow()).thenReturn(Bytes.toBytes(dbl));
		CruxResultImpl impl = new CruxResultImpl(result, report);
		assertEquals(1234568.0d, (Double) impl.get(0), 0.001d);		
	}

	@Test
	public void testNextAgg() throws CruxException{
		//ceil is applied
		Result result = mock(Result.class);
		Report report = TestingUtil.getReport();
		double dbl = 1234567.8d;
		when(result.getRow()).thenReturn(Bytes.toBytes(dbl));
		CruxResultImpl impl = new CruxResultImpl(result, report);
		assertEquals(1234568.0d, (Double) impl.get(0), 0.001d);		
	}
}
