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

import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.view.FilterAliasView;
import co.nubetech.crux.view.ReportView;

public class ViewReportListAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ViewReportListAction.class);

	protected ArrayList<FilterAliasView> filterViewList = new ArrayList<FilterAliasView>();
	protected ReportDAO reportDAO = new ReportDAO();
	private ArrayList<ReportView> reportList = new ArrayList<ReportView>();
	protected ArrayList<Mapping> mappingList;
	protected MappingDAO mappingDAO = new MappingDAO();
	
	public ViewReportListAction() {

	}

	public ArrayList<ReportView> getReportList() {
		return reportList;
	}

	public void setReportList(ArrayList<ReportView> reportList) {
		this.reportList = reportList;
	}

	public ArrayList<Mapping> getMappingList() {
		return mappingList;
	}

	public void setMappingList(ArrayList<Mapping> mappingList) {
		this.mappingList = mappingList;
	}
	
	public ArrayList<FilterAliasView> getFilterViewList() {
		return filterViewList;
	}

	public void setFilterViewList(ArrayList<FilterAliasView> filterViewList) {
		this.filterViewList = filterViewList;
	}
	public String displayReportList() {
		long index = 0;
		List<Report> reporArraytList = reportDAO.findAll();
		for (Report report : reporArraytList) {
			reportList.add(new ReportView(++index, report));
		}
		logger.debug("reportList: " + reportList);
		if (reportList.size() > 0) {
			return SUCCESS;
		} else {
			mappingList = new ReportDesignAction().populateMappingList(mappingDAO,mappingList);
			logger.debug("MappingList: " + mappingList);
			return "report";			
		}
	}
}
