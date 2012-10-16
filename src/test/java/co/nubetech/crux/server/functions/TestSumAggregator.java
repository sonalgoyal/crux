package co.nubetech.crux.server.functions;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;
import static org.junit.Assert.*;

public class TestSumAggregator {
	
	@Test
	public void testSumByteArray() throws CruxException{
		byte[] value1 = Bytes.toBytes(new Double(5.25d));
		SumAggregator summer = new SumAggregator();
		summer.aggregate(value1);
		byte[] value2 = Bytes.toBytes(new Double(25.25d));
		summer.aggregate(value2);
		assertEquals(30.50d, summer.getAggregate());
		byte[] value3 = Bytes.toBytes(new Double(0d));
		summer.aggregate(value3);
		assertEquals(30.50d, summer.getAggregate());
	}

}
