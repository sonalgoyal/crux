package co.nubetech.crux.server.aggregate;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.GroupBys;

public interface GroupingAggregationProtocol extends CoprocessorProtocol{
	
	public static final long VERSION = 1L;
	
	public Map<Alias,List> getAggregates(Class clazz, GroupBys groupBy, Scan scan);
	
}
