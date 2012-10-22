package co.nubetech.crux.server.aggregate;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.Batch;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxException;

public class GroupingAggregationBatch implements Batch.Call<GroupingAggregationProtocol, List<List>>{
	
	Scan scan;
	Report report;
	
	public GroupingAggregationBatch(Scan scan, Report report) {
		this.scan = scan;
		this.report = report;
	}
	
	@Override
    public List<List> call(GroupingAggregationProtocol instance) throws IOException {
      try {
    	  List<List> result = instance.getAggregates(scan, report);
    	  return result;
      }
      catch(CruxException e) {
    	  e.printStackTrace();
      }
      return null;
    }
	
	

}
