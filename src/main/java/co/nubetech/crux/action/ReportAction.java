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
import co.nubetech.crux.view.ReportView;

public class ReportAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ReportAction.class);

	private ReportDAO report = new ReportDAO();
	private ArrayList<ReportView> reportList = new ArrayList<ReportView>();
	private String errorMsg;

	public ReportAction() {

	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ArrayList<ReportView> getReportList() {
		return reportList;
	}

	public void setReportList(ArrayList<ReportView> reportList) {
		this.reportList = reportList;
	}

	public String displayReportList() {
		long index = 0;
		List<Report> reporArraytList = report.findAll();
		for (Report report : reporArraytList) {
			reportList.add(new ReportView(++index, report));
		}
		logger.debug("reportList: " + reportList);
		logger.debug("ErrorMsg: " + errorMsg);
		if (errorMsg != null) {
			return SUCCESS;
		} else if (reportList.size() > 0) {
			return SUCCESS;
		} else {
			return "report";
		}
	}

}
