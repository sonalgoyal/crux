package co.nubetech.crux.server.functions;

import static org.junit.Assert.*;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;

public class TestMaxAggregator {
	
	@Test
	public void testMaxByteArray() throws CruxException{
		byte[] value1 = Bytes.toBytes(new Long(5));
		MaxAggregator max = new MaxAggregator();
		assertNull(max.getAggregate());
		max.aggregate(value1);
		assertEquals(5l, max.getAggregate());
		byte[] value2 = Bytes.toBytes(new Long(25));
		max.aggregate(value2);
		assertEquals(25l, max.getAggregate());
		byte[] value3 = Bytes.toBytes(new Long(3));
		max.aggregate(value3);
		assertEquals(25l, ((Long) max.getAggregate()).longValue());
	}

}
