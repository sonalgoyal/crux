package co.nubetech.crux.server;

import java.io.IOException;
import java.util.List;

public class CruxScannerListImpl implements CruxScanner{
	
	private List<List> result; 
	private int index;
	
	public CruxScannerListImpl(List<List> result) {
		this.result = result;
		index = 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CruxResult next() throws IOException {
		if (result != null) {
			if (index < result.size()) {
				return new CruxResultListImpl(result.get(index++));
			}
		}
		return null;
	}
	
	

}
