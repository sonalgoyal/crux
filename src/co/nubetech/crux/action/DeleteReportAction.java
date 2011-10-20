/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.ReportView;

public class DeleteReportAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.DeleteReportAction.class);

	private Report report = new Report();
	private ReportDAO reportDAO = new ReportDAO();
	private ArrayList<ReportView> reportList = new ArrayList<ReportView>();

	public DeleteReportAction() {

	}

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public ReportDAO getReportDAO() {
		return reportDAO;
	}

	public void setReportDAO(ReportDAO reportDAO) {
		this.reportDAO = reportDAO;
	}

	public ArrayList<ReportView> getReportList() {
		return reportList;
	}

	public void setReportList(ArrayList<ReportView> reportList) {
		this.reportList = reportList;
	}

	public String deleteReport() {
		logger.debug("Inside Execute and Id: " + report.getId());
		try {
			reportDAO.delete(reportDAO.findById(report.getId()));
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
		}

		long index = 0;
		List<Report> reporArraytList = reportDAO.findAll();
		for (Report report : reporArraytList) {
			reportList.add(new ReportView(++index, report));
		}
		logger.debug("reportList: " + reportList);
		return SUCCESS;

	}

}
