package co.nubetech.crux.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Report;

public class DashBoardAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.DashBoardAction.class);

	private ReportDAO reportDAO = new ReportDAO();
	private ArrayList<Long> reportIdList = new ArrayList<Long>();

	public DashBoardAction() {
	}

	public ArrayList<Long> getReportIdList() {
		return reportIdList;
	}

	public void setReportIdList(ArrayList<Long> reportIdList) {
		this.reportIdList = reportIdList;
	}

	public String getDashBoard() {
		boolean ifExists = false;
		ArrayList<Report> reportList = new ArrayList<Report>(
				reportDAO.findAll());
		for (Report report : reportList) {
			if (report.getDashboardType() == (short) 1) {
				reportIdList.add(report.getId());
				ifExists = true;
			}
		}

		logger.debug("ReportIdList: "+reportIdList);
		if (!ifExists) {
			error.setMessage("Dasboard is empty. Please set report on dashboard");
		}		
		return SUCCESS;
	}

}
