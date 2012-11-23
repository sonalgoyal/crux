package co.nubetech.crux.view;

import org.apache.log4j.Logger;

import co.nubetech.crux.model.RowAlias;

public class GroupBysView {
	
	private final static Logger logger = Logger
		.getLogger(co.nubetech.crux.view.GroupBysView.class);
	
	private int index;
	private long id;
	private String alias;
	
	public GroupBysView(){
		
	}
	
	public GroupBysView(int index, RowAlias rowAlias){
		this.index = index;
		this.id = rowAlias.getId();
		this.alias = rowAlias.getAlias();
		
		logger.debug("GroupByView :");
	
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return "GroupBysView [index=" + index + ", id=" + id + ", alias="
				+ alias + "]";
	}
	
}
