package co.nubetech.crux.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;

public class PreviewAction extends SaveReportAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.PreviewAction.class);

	private ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();

	public ArrayList<ArrayList> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<ArrayList> dataList) {
		this.dataList = dataList;
	}

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public String findPreviewReportData() {
		try {
			String toReturn = populateReport();
			if (!toReturn.equals("error")) {
				ViewReportAction viewReportAction = new ViewReportAction();
				logger.debug("GetData:" + report + " " + mappingId);
				dataList = viewReportAction.getData(report,
						mappingDAO.findById(mappingId));
			}
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
			return "error";
		}
		return SUCCESS;
	}
}
