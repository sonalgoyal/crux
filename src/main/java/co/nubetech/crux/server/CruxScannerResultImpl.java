package co.nubetech.crux.server;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Report;

public class CruxScannerResultImpl implements CruxScanner{
	private Result result;
	private Report report;

	public CruxScannerResultImpl(Result result, Report r) {
		this.result = result;
		this.report = r;
	}

	@Override
	public void close() {
		result = null;
	}

	@Override
	public CruxResult next() throws IOException {
		if (result != null) {
			return new CruxResultImpl(result, report);
		}
		return null;
	}


}
