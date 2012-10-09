package co.nubetech.crux.server.aggregate;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxException;

public interface GroupingAggregationProtocol extends CoprocessorProtocol{
	
	public static final long VERSION = 1L;
	
	/**
	 * Each row is a list
	 * multiple rows are in containing list
	 * @param clazz - aggregation to perform
	 * @param groupBy
	 * @param scan
	 * @return
	 * @throws CruxException 
	 */
	public List<List> getAggregates(Scan scan, Report report) throws CruxException;
	
}
