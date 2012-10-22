package co.nubetech.crux.server;

import java.io.IOException;

import org.apache.hadoop.hbase.client.ResultScanner;

import co.nubetech.crux.model.Report;

public class CruxScannerResultScannerImpl implements CruxScanner{
	
	private ResultScanner scanner;
	private Report report;

	public CruxScannerResultScannerImpl(ResultScanner result, Report r) {
		this.scanner = result;
		this.report = r;
	}

	@Override
	public void close() {
		if (scanner != null) {
			scanner.close();
		}
	}

	@Override
	public CruxResult next() throws IOException {
		if (scanner != null) {
			return new CruxResultImpl(scanner.next(), report);
		}
		return null;
	}

}
