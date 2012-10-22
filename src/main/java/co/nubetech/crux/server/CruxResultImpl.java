package co.nubetech.crux.server;

import java.util.Collection;

import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;

public class CruxResultImpl implements CruxResult{
	Result result;
	Report report;
	
	public CruxResultImpl(Result result, Report report) {
		this.result = result;
		this.report = report;
	}

	@Override
	public Object get(int index) {
		// TODO Auto-generated method stub
		if (report != null && result != null) {
			Collection<ReportDesign> designs = report.getDesigns();
			if (designs != null) {
				if (index < designs.size()) {
					ReportDesign design = report.getDesignAtIndex(index);
					Object data = ServerUtil.getValue(result, ServerUtil.getAlias(design), report);
					return data;
				}
			}
		}		
		return null;
	}

}
