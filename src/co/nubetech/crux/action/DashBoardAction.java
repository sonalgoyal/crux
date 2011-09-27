package co.nubetech.crux.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.DashboardDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;

public class DashBoardAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.DashBoardAction.class);

	private ReportDAO reportDAO = new ReportDAO();
	private DashboardDAO dashboardDAO = new DashboardDAO();
	private ArrayList<Report> reportListForDashBoard = new ArrayList<Report>();
	private ArrayList<String> dashboardInfo = new ArrayList<String>();

	public DashBoardAction() {
	}

	public CruxError getError() {
		return error;
	}

	public ReportDAO getReportDAO() {
		return reportDAO;
	}

	public void setReportDAO(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}

	public DashboardDAO getDashboardDAO() {
		return dashboardDAO;
	}

	public void setDashboardDAO(DashboardDAO dashboardDAO) {
		this.dashboardDAO = dashboardDAO;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public ArrayList<Report> getReportListForDashBoard() {
		return reportListForDashBoard;
	}

	public void setReportListForDashBoard(
			ArrayList<Report> reportListForDashBoard) {
		this.reportListForDashBoard = reportListForDashBoard;
	}

	public ArrayList<String> getDashboardInfo() {
		return dashboardInfo;
	}

	public void setDashboardInfo(ArrayList<String> dashBoardInfo) {
		this.dashboardInfo = dashBoardInfo;
	}

	public String getDashBoard() {
		boolean ifExists = false;
		ArrayList<Report> reportList = new ArrayList<Report>(
				reportDAO.findDashboardReports());
		for (Report report : reportList) {
			reportListForDashBoard.add(report);
			ifExists = true;
		}
		logger.debug("reportListForDashBoard: " + reportListForDashBoard);
		if (!ifExists) {
			error.setMessage("Dasboard is empty. Please set report on dashboard");
		}
		return SUCCESS;
	}

	// here dashBoard info is a array list of string returned from dashboard.jsp
	// here format of each string is dash+dashboardId:index:column
	public String saveDashBoard() {
		logger.debug("dashBoardInfo:" + dashboardInfo);
		for (String info : dashboardInfo) {
			String[] details = info.split(":");
			Dashboard dashboard = dashboardDAO.findById(new Long(details[0]));
			dashboard.setIndex(Integer.parseInt(details[1]));
			dashboard.setColumn(Integer.parseInt(details[2]));
			try {
				dashboardDAO.save(dashboard);
			} catch (CruxException e) {
				error.setMessage("Error: " + e.getMessage());
			}
		}
		return SUCCESS;
	}
}
