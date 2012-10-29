package co.nubetech.crux.server;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Report;

public class CruxScannerResultImpl implements CruxScanner{
	private Result result;
	private Report report;
	private boolean served;

	public CruxScannerResultImpl(Result result, Report r) {
		this.result = result;
		this.report = r;
		served = false;
	}

	@Override
	public void close() {
		result = null;
		served = false;
	}

	@Override
	public CruxResult next() throws IOException {
		if (result != null && report != null && !served) {
			served = true;
			return new CruxResultImpl(result, report);
		}
		return null;
	}


}
