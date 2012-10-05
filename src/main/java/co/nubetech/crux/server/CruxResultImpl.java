package co.nubetech.crux.server;

import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Report;

public class CruxResultImpl implements CruxResult{
	
	private Result[] result;
	private Report report;
	
	protected CruxResultImpl(Result[] result, Report r) {
		this.result = result;
		this.report = r;
	}
	
	protected CruxResultImpl(Result r, Report report) {
		this.result = new Result[] {r};
		this.report = report;
	}
	
	public Object get(int index) {
		//TODO
		return null;
	}

}
