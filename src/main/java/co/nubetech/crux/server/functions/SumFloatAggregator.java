package co.nubetech.crux.server.functions;

import org.apache.hadoop.hbase.util.Bytes;

import co.nubetech.crux.util.CruxException;


public class SumFloatAggregator extends FunctionBase implements CruxAggregator{
	
	private double sum;
	
	public SumFloatAggregator(){
		sum = 0;
	}
	
	/*
	 * this method is invoked for each value
	 */
	@Override
	public void aggregate(Object o) throws CruxException{
		if (o instanceof byte[]) {
			Float dbl = Bytes.toFloat((byte[])o);
			sum += dbl;
		}
		else {
			Float dbl = (Float) o;
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

	@Override
	public boolean isAggregate() {
		return true;
	}

}
