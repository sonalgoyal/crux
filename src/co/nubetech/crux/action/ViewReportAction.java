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

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.FilterAliasView;

public class ViewReportAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ViewReportAction.class);

	private ReportDAO reportDAO = new ReportDAO();
	private Report report = new Report();
	private String axisValues = new String();
	private String chartType = new String();
	private ArrayList<FilterAliasView> filterList = new ArrayList<FilterAliasView>();
	private long mappingId;

	public ViewReportAction() {

	}

	public long getMappingId() {
		return mappingId;
	}

	public void setMappingId(long mappingId) {
		this.mappingId = mappingId;
	}

	public ArrayList<FilterAliasView> getFilterList() {
		return filterList;
	}

	public void setFilterList(ArrayList<FilterAliasView> filterList) {
		this.filterList = filterList;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getAxisValues() {
		return axisValues;
	}

	public void setAxisValues(String axisValues) {
		this.axisValues = axisValues;
	}

	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public String viewReport() {
		logger.debug("Report Id: " + report.getId());
		try {
			report = reportDAO.findById(report.getId());
		} catch (CruxException e) {
			error.setMessage("Error: " + e.getMessage());
			return "error";
		}
		logger.debug("Report To View: " + report);
		ArrayList<ReportDesign> reportDesignList = new ArrayList<ReportDesign>(
				report.getDesigns());
		for (ReportDesign reportDesign : reportDesignList) {
			String alias = (reportDesign.getColumnAlias() != null) ? reportDesign
					.getColumnAlias().getAlias() : reportDesign.getRowAlias()
					.getAlias();
			axisValues = axisValues + reportDesign.getMappingAxis() + ","
					+ alias + ":";
		}
		mappingId = (reportDesignList.get(0).getColumnAlias() != null) ? reportDesignList
				.get(0).getColumnAlias().getMapping().getId()
				: reportDesignList.get(0).getRowAlias().getMapping().getId();
		chartType = report.getReportType().getType();
		getFilterAliasView();
		logger.debug("xAxis: " + axisValues + " chartType: " + chartType);

		return SUCCESS;
	}

	private void getFilterAliasView() {
		ArrayList<ColumnFilter> columnFilterList = new ArrayList<ColumnFilter>(
				report.getColumnFilters());
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>(
				report.getRowAliasFilters());

		for (ColumnFilter columnFilter : columnFilterList) {
			filterList.add(new FilterAliasView(columnFilter));
		}
		for (RowAliasFilter rowFilter : rowFilterList) {
			filterList.add(new FilterAliasView(rowFilter));
		}
	}
}
