package co.nubetech.crux.server.functions;

import org.apache.hadoop.hbase.util.Bytes;

import co.nubetech.crux.util.CruxException;


public class SumAggregator extends FunctionBase implements CruxAggregator{
	
	private double sum;
	
	public SumAggregator(){
		sum = 0;
	}
	
	/*
	 * this method is invoked for each value
	 */
	@Override
	public void aggregate(Object o) throws CruxException{
		if (o instanceof byte[]) {
			Double dbl = Bytes.toDouble((byte[])o);
			sum += dbl;
		}
		else {
			Double dbl = (Double) o;
			sum += dbl;
		}		
	}
	
	/*
	 * invoked in the end to get the result
	 */
	@Override
	public Object getAggregate() {
		return sum;
	}

}
