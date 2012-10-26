package co.nubetech.crux.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.TestingUtil;

public class TestCruxResultImpl {
	
	@Test
	public void testNext() {
		Result result = mock(Result.class);
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		//when(result.)
		
	}

}
