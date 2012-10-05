package co.nubetech.crux.server.functions;

import co.nubetech.crux.util.CruxException;

public interface CruxAggregator extends CruxFunction{
	
	/*
	 * this method is invoked for each value
	 */
	public void aggregate(byte[] o) throws CruxException;
	
	public Object getAggregate();

}
