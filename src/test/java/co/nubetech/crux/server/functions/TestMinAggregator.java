package co.nubetech.crux.server.functions;

import static org.junit.Assert.*;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;

public class TestMinAggregator {
	
	@Test
	public void testminByteArray() throws CruxException{
		byte[] value1 = Bytes.toBytes(new Long(5));
		MinAggregator min = new MinAggregator();
		assertNull(min.getAggregate());
		min.aggregate(value1);
		assertEquals(5l, min.getAggregate());
		byte[] value2 = Bytes.toBytes(new Long(25));
		min.aggregate(value2);
		assertEquals(5l, min.getAggregate());
		byte[] value3 = Bytes.toBytes(new Long(3));
		min.aggregate(value3);
		assertEquals(3l, ((Long) min.getAggregate()).longValue());
	}

}
