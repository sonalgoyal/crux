package co.nubetech.crux.server.functions;

import co.nubetech.crux.util.CruxException;

public interface CruxNonAggregator {
	
	/*
	 * this method is invoked for each value
	 */
	public Object execute(byte[] o) throws CruxException;

}
