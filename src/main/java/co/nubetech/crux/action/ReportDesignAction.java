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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.FunctionTypeMappingDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportTypeDAO;
import co.nubetech.crux.dao.ValueFilterTypeDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.FunctionTypeMapping;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueFilterType;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.DimensionAndMeasureView;
import co.nubetech.crux.view.FilterAliasView;
import co.nubetech.crux.view.FunctionView;

public class ReportDesignAction extends ViewReportListAction {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ReportDesignAction.class);

	protected ReportTypeDAO reportTypeDAO = new ReportTypeDAO();
	private ValueFilterTypeDAO valueFilterTypeDAO = new ValueFilterTypeDAO();
	private ArrayList<ReportType> reportTypeList;
	private ArrayList<FilterType> filterTypeList;
	protected long mappingId;
	protected Report report = new Report();
	private String edit = "false";
	protected String reportType;
	protected String axisValues = new String();
	private String aliasName;
	protected boolean addToDashBoard;
	private ArrayList<FilterAliasView> filterViewList = new ArrayList<FilterAliasView>();
	private ArrayList<DimensionAndMeasureView> dimensionAndMeasureViewList = new ArrayList<DimensionAndMeasureView>();
	private ArrayList<FunctionView> functionViewList = new ArrayList<FunctionView>();
	protected FunctionTypeMappingDAO functionTypeMappingDAO = new FunctionTypeMappingDAO();

	public ReportDesignAction() {

	}

	public String getAliasName() {
		return aliasName;
	}

	public ArrayList<FunctionView> getFunctionViewList() {
		return functionViewList;
	}

	public void setFunctionViewList(ArrayList<FunctionView> functionViewList) {
		this.functionViewList = functionViewList;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public ArrayList<FilterType> getFilterTypeList() {
		return filterTypeList;
	}

	public void setFilterTypeList(ArrayList<FilterType> filterTypeList) {
		this.filterTypeList = filterTypeList;
	}

	public ArrayList<ReportType> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(ArrayList<ReportType> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public long getMappingId() {
		return mappingId;
	}

	public void setMappingId(long mappingId) {
		this.mappingId = mappingId;
	}

	public String getAxisValues() {
		return axisValues;
	}

	public void setAxisValues(String axisValues) {
		this.axisValues = axisValues;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getDesignPage() {
		mappingList = populateMappingList(mappingDAO, mappingList);
		return SUCCESS;
	}

	public ArrayList<Mapping> populateMappingList(MappingDAO mappingDAO,
			ArrayList<Mapping> mappingList) {
		mappingList = new ArrayList<Mapping>(mappingDAO.findAll());
		logger.debug("GetDesignPage:" + mappingDAO + ":" + mappingList);
		return mappingList;
	}

	public ArrayList<FilterAliasView> getFilterViewList() {
		return filterViewList;
	}

	public void setFilterViewList(ArrayList<FilterAliasView> filterViewList) {
		this.filterViewList = filterViewList;
	}

	public boolean isAddToDashBoard() {
		return addToDashBoard;
	}

	public void setAddToDashBoard(boolean dashBoard) {
		this.addToDashBoard = dashBoard;
	}

	public ArrayList<DimensionAndMeasureView> getDimensionAndMeasureViewList() {
		return dimensionAndMeasureViewList;
	}

	public void setDimensionAndMeasureViewList(
			ArrayList<DimensionAndMeasureView> dimensionAndMeasureViewList) {
		this.dimensionAndMeasureViewList = dimensionAndMeasureViewList;
	}

	public String populateDimensionAndMeasureList() {
		mappingList = populateMappingList(mappingDAO, mappingList);
		Map<String, ColumnAlias> columnAlias = null;
		Map<String, RowAlias> rowAlias = null;
		logger.debug("Mapping Id: " + mappingId);

		if (mappingId != 0) {

			try {
				columnAlias = mappingDAO.findById(mappingId).getColumnAlias();
				rowAlias = mappingDAO.findById(mappingId).getRowAlias();
			} catch (CruxException e) {
				error.setMessage("Error: " + e.getMessage());
			}

			if (columnAlias != null) {
				Set<Entry<String, ColumnAlias>> mapSet = columnAlias.entrySet();
				for (Map.Entry<String, ColumnAlias> entry : mapSet) {
					dimensionAndMeasureViewList
							.add(new DimensionAndMeasureView(entry.getValue()));
				}
			}

			if (rowAlias != null) {
				Set<Entry<String, RowAlias>> mapSet = rowAlias.entrySet();
				for (Map.Entry<String, RowAlias> entry : mapSet) {
					dimensionAndMeasureViewList
							.add(new DimensionAndMeasureView(entry.getValue()));
				}
			}
		}
		reportTypeList = new ArrayList<ReportType>(reportTypeDAO.findAll());
		ArrayList<FunctionTypeMapping> funcTypeMappingList = new ArrayList<FunctionTypeMapping>(
				functionTypeMappingDAO.findAll());
		for (FunctionTypeMapping funTypeMapping : funcTypeMappingList) {
			functionViewList.add(new FunctionView(funTypeMapping));
		}
		return SUCCESS;
	}

	public String populateFilterType() {
		Map<String, ColumnAlias> columnAlias = null;
		Map<String, RowAlias> rowAlias = null;
		long valueTypeId = 0;
		filterTypeList = new ArrayList<FilterType>();
		if (mappingId != 0) {
			try {
				columnAlias = mappingDAO.findById(mappingId).getColumnAlias();
				rowAlias = mappingDAO.findById(mappingId).getRowAlias();
			} catch (CruxException e) {
				error.setMessage(e.getMessage());
				return SUCCESS;
			}

			if (columnAlias.containsKey(aliasName)) {
				valueTypeId = columnAlias.get(aliasName).getValueType().getId();
			} else if (rowAlias.containsKey(aliasName)) {
				valueTypeId = rowAlias.get(aliasName).getValueType().getId();
			} else {
				error.setMessage("Cannot find alias selected in database");
				return SUCCESS;
			}

			ArrayList<ValueFilterType> valueFilterTypeList = new ArrayList<ValueFilterType>(
					valueFilterTypeDAO.findAll());
			for (ValueFilterType valueFilterType : valueFilterTypeList) {
				if (valueFilterType.getValueType().getId() == valueTypeId) {
					filterTypeList.add(valueFilterType.getFilterType());
				}
			}

		}
		return SUCCESS;
	}

	public String editReport() {
		String returnString = SUCCESS;
		try {
			report = reportDAO.findById(report.getId());

			logger.debug("Report to edit: " + report);
			ArrayList<ReportDesign> reportDesignList = new ArrayList<ReportDesign>(
					report.getDesigns());

			for (ReportDesign reportDesign : reportDesignList) {
				String alias = (reportDesign.getColumnAlias() != null) ? reportDesign
						.getColumnAlias().getAlias() : reportDesign
						.getRowAlias().getAlias();
					ArrayList<ReportDesignFunction> functionList = new ArrayList<ReportDesignFunction>(reportDesign.getReportDesignFunctionList());
					for (ReportDesignFunction function :functionList){
						alias = function.getFunction().getFunctionName()+"("+alias+")";
					}
				axisValues = axisValues + reportDesign.getMappingAxis() + ","
						+ alias + ":";
			}
			if (reportDesignList.size() > 0) {
				mappingId = (reportDesignList.get(0).getColumnAlias() != null) ? reportDesignList
						.get(0).getColumnAlias().getMapping().getId()
						: reportDesignList.get(0).getRowAlias().getMapping()
								.getId();
				;
			}
			reportType = report.getReportType().getType();
			if (report.getDashboardType() == (short) 1) {
				addToDashBoard = true;
			} else {
				addToDashBoard = false;
			}
			mappingList = populateMappingList(mappingDAO, mappingList);
			getFilterAliasView();
			edit = "true";
			logger.debug("Mapping Id: " + mappingId + "," + reportType);
		} catch (CruxException e) {
			error.setMessage("Error: " + e.getMessage());
			displayReportList();
			returnString = "error";
		}
		return returnString;
	}

	public void getFilterAliasView() {
		ArrayList<ColumnFilter> columnFilterList = new ArrayList<ColumnFilter>(
				report.getColumnFilters());
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>(
				report.getRowAliasFilters());

		for (ColumnFilter columnFilter : columnFilterList) {
			filterViewList.add(new FilterAliasView(columnFilter));
		}
		for (RowAliasFilter rowFilter : rowFilterList) {
			filterViewList.add(new FilterAliasView(rowFilter));
		}
	}
}
