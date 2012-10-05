package co.nubetech.crux.server.aggregate;

public interface CruxAggregator {
	
	/*
	 * this method is invoked for each value
	 */
	public void aggregate(byte[] o);
	
	/*
	 * invoked in the end to get the result
	 */
	public Object getResult();

}
