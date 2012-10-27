package co.nubetech.crux.server;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;

public class CruxResultImpl implements CruxResult{
	Result result;
	Report report;
	
	public CruxResultImpl(Result result, Report report) {
		this.result = result;
		this.report = report;
	}

	@Override
	public Object get(int index) throws CruxException{
		List<Stack<CruxFunction>> functions = report.getFunctions();
		
		if (report != null && result != null) {
			Collection<ReportDesign> designs = report.getDesigns();
			if (designs != null) {
				if (index < designs.size()) {
					ReportDesign design = report.getDesigns().get(index);
					FunctionUtil.applyAggregateFunctions(
							ServerUtil.getValue(result, ServerUtil.getAlias(design)), functions.get(index));					
				}
			}
		}		
		return null;
	}

}
