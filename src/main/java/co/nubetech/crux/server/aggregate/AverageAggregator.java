package co.nubetech.crux.server.aggregate;

import org.apache.hadoop.hbase.util.Bytes;

public class AverageAggregator implements CruxAggregator{
	
	private int count;
	private double sum;
	
	public AverageAggregator(){
		count = 0;
		sum = 0;
	}
	
	/*
	 * this method is invoked for each value
	 */
	public void aggregate(byte[] o) {
		Double dbl = Bytes.toDouble(o);
		count++;
		sum += dbl;
	}
	
	/*
	 * invoked in the end to get the result
	 */
	public Object getResult() {
		if (count != 0) {
			return sum/count;
		}
		else {
			return null;
		}
	}

	
	

}
