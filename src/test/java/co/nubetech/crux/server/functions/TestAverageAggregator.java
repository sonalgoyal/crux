package co.nubetech.crux.server.functions;

import static org.junit.Assert.*;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;

public class TestAverageAggregator {
	
	@Test
	public void testAverageByteArray() throws CruxException{
		byte[] value1 = Bytes.toBytes(new Double(5.25d));
		AverageAggregator avg = new AverageAggregator();
		avg.aggregate(value1);
		byte[] value2 = Bytes.toBytes(new Double(25.25d));
		avg.aggregate(value2);
		assertEquals(15.25d, avg.getAggregate());
		byte[] value3 = Bytes.toBytes(new Double(0d));
		avg.aggregate(value3);
		assertEquals(10.17d, ((Double) avg.getAggregate()).doubleValue(), 0.01d);
	}

}
