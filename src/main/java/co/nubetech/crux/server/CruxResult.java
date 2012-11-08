package co.nubetech.crux.server;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.util.CruxException;

public interface CruxResult {
	
	public Object get(int index) throws CruxException;	
	
}
