package co.nubetech.crux.server.functions;

import co.nubetech.crux.util.CruxException;

public interface CruxNonAggregator extends CruxFunction{
	
	/*
	 * this method is invoked for each value
	 */
	public Object execute(Object o) throws CruxException;

}
