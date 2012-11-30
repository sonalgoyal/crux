package co.nubetech.crux.server.functions;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;

public class TestSumFloatAggregator {
	
	@Test
	public void testSumByteArray() throws CruxException{
		byte[] value1 = Bytes.toBytes(new Float(5.25f));
		SumFloatAggregator summer = new SumFloatAggregator();
		summer.aggregate(value1);
		byte[] value2 = Bytes.toBytes(new Float(25.25f));
		summer.aggregate(value2);
		assertEquals(30.50d, summer.getAggregate());
		byte[] value3 = Bytes.toBytes(new Float(0f));
		summer.aggregate(value3);
		assertEquals(30.50d, summer.getAggregate());
	}

}
