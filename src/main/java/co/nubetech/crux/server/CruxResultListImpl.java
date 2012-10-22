package co.nubetech.crux.server;

import java.util.List;


public class CruxResultListImpl implements CruxResult{
	
	private List row;
	
	public CruxResultListImpl(List list) {
		this.row = list;
	}
	
	public Object get(int index) {
		if (row != null) {
			return row.get(index);
		}
		return null;
	}

}
