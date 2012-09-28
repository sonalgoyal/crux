package co.nubetech.crux.view;

import org.apache.log4j.Logger;

import co.nubetech.crux.model.Analysis;

public class AnalysisView {
	
	private final static Logger logger = Logger
	.getLogger(co.nubetech.crux.view.AnalysisView.class);	
	
	private long index;
	private long id;
	private String name;
	private String text;
	
	public AnalysisView(){
	}
	
	public AnalysisView(long index, Analysis analysis){
		this.index = index;
		this.id = analysis.getId();
		this.name = analysis.getName();
		this.text = analysis.getText();

		logger.debug("Analysis view: ");
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	

}
