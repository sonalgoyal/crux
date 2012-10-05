package co.nubetech.crux.server.aggregate;

import org.apache.hadoop.hbase.util.Bytes;

public class SumAggregator implements CruxAggregator{
	
	private double sum;
	
	public SumAggregator(){
		sum = 0;
	}
	
	/*
	 * this method is invoked for each value
	 */
	public void aggregate(byte[] o) {
		Double dbl = Bytes.toDouble(o);
		sum += dbl;
	}
	
	/*
	 * invoked in the end to get the result
	 */
	public Object getResult() {
		return sum;
	}

}
