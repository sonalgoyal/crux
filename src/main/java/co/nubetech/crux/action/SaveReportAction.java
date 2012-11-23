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
import java.util.Map;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.FilterTypeDAO;
import co.nubetech.crux.dao.FunctionDAO;
import co.nubetech.crux.dao.UserDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Dashboard;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.GroupBy;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.User;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.FilterAliasView;
import co.nubetech.crux.view.GroupBysView;

public class SaveReportAction extends ReportDesignAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.SaveReportAction.class);

	private FilterTypeDAO filterTypeDAO = new FilterTypeDAO();
	private UserDAO userDAO = new UserDAO();
	private GroupBys groupBys = new GroupBys();
	private FunctionDAO functionDAO = new FunctionDAO();

	private ReportType reportTypeObject = new ReportType();
	private User user = new User();
	private ArrayList<String> aliasList = new ArrayList<String>();

	private ArrayList<FilterAliasView> filterList = new ArrayList<FilterAliasView>();
	private ArrayList<GroupBysView> groupBysViewList = new ArrayList<GroupBysView>();

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public ArrayList<FilterAliasView> getFilterList() {
		return filterList;
	}

	public void setFilterList(ArrayList<FilterAliasView> filterList) {
		this.filterList = filterList;
	}

	public ArrayList<String> getAliasList() {
		return aliasList;
	}

	public void setAliasList(ArrayList<String> aliasList) {
		this.aliasList = aliasList;
	}

	public ArrayList<GroupBysView> getGroupBysViewList() {
		return groupBysViewList;
	}

	public void setGroupBysViewList(ArrayList<GroupBysView> groupBysViewList) {
		this.groupBysViewList = groupBysViewList;
	}

	public String saveReport() {
		String returnString = SUCCESS;
		if (populateReport().equals("error")) {
			returnString = "error";
		} else {
			try {
				logger.debug("Report Posted: " + report);
				reportDAO.save(report);
			} catch (CruxException e) {
				error.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				error.setMessage(e.getMessage());
			}
		}
		if (error.isError()) {
			getDesignPage();
			returnString = "error";
		} else {
			displayReportList();
		}
		return returnString;
	}

	protected String populateReport() {
		logger.debug("ReportId: " + report.getId());

		long reportTypeId = 0;

		reportTypeId = getReportTypeId(reportType);
		reportTypeObject = reportTypeDAO.findById(reportTypeId);

		user = userDAO.findById(1l);

		logger.debug("user name: " + user.getName());
		logger.debug("FilterList: " + filterList);
		logger.debug("AxisValues: " + axisValues);

		ArrayList<ReportDesign> designList = new ArrayList<ReportDesign>();
		Mapping mapping = null;
		try {
			mapping = mappingDAO.findById(mappingId);

			Map<String, ColumnAlias> hashColumnAlias = mapping.getColumnAlias();
			Map<String, RowAlias> hashRowAlias = mapping.getRowAlias();
			String[] axisArray = axisValues.split(":");

			for (String axis : axisArray) {
				logger.debug("axis: " + axis);
				if (!axis.equals("")) {
					String[] axisNameValue = axis.split(",");
					if (!axisNameValue[1].equals("")) {
						ReportDesign reportDesign = new ReportDesign();
						reportDesign.setReport(report);

						String aliasFunction = axisNameValue[1];
						String aliasName = getAliasName(aliasFunction);

						reportDesign
								.setReportDesignFunctionList(getReportDesignFunctionList(
										aliasFunction, reportDesign));
						reportDesign.setMappingAxis(axisNameValue[0]);
						if (hashColumnAlias.containsKey(aliasName)) {
							reportDesign.setColumnAlias(hashColumnAlias
									.get(aliasName));

						} else if (hashRowAlias.containsKey(aliasName)) {
							reportDesign.setRowAlias(hashRowAlias
									.get(aliasName));
						}
						designList.add(reportDesign);
						logger.debug("ReportDesign object:" + reportDesign);
					}
				}
			}

			if (addToDashBoard) {
				if (!checkDashBoardOverLoaded(report.getId())) {
					report.setDashboard(getDashboardObject(report.getId()));
				}
			} else {
				report.setDashboard(null);
			}
			report.setDesigns(designList);
			report.setUser(user);
			report.setReportType(reportTypeObject);

			populateRowAndColumnFilter(hashColumnAlias, hashRowAlias);
			populateGroupBys(hashRowAlias);
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
			return "error";
		}
		return SUCCESS;
	}

	public Dashboard getDashboardObject(long id) {
		Dashboard result = null;
		ArrayList<Report> reportList = new ArrayList<Report>(
				reportDAO.findDashboardReports());
		for (Report report : reportList) {
			if (report.getDashboard() != null) {
				if (id == report.getId()) {
					result = report.getDashboard();
				}
			}
		}
		if (result == null) {
			result = new Dashboard();
		}
		return result;
	}

	private String getAliasName(String aliasFunction) {
		String aliasName = null;
		if (aliasFunction.contains("(")) {
			String[] aliasFunctionArray = aliasFunction.split("\\(");
			if (aliasFunctionArray.length > 1) {
				aliasName = aliasFunctionArray[aliasFunctionArray.length - 1]
						.split("\\)")[0];
			} else {
				aliasName = aliasFunctionArray[0];
			}
		} else {
			aliasName = aliasFunction;
		}
		return aliasName;
	}

	private ArrayList<ReportDesignFunction> getReportDesignFunctionList(
			String aliasFunction, ReportDesign reportDesign)
			throws CruxException {
		boolean isRow = false;
		boolean isAggregate = false;
		ArrayList<Function> functionList = new ArrayList<Function>(
				functionDAO.findAll());
		ArrayList<ReportDesignFunction> reportDesignFunctionList = new ArrayList<ReportDesignFunction>();
		if (aliasFunction.contains("(")) {
			String[] functionArray = aliasFunction.split("\\(");
			for (int i = 0; i < functionArray.length - 1; i++) {
				for (Function function : functionList) {
					if (functionArray[i].equalsIgnoreCase(function
							.getFunctionName())) {
						reportDesignFunctionList.add(new ReportDesignFunction(
								reportDesign, function));
						if (function.isAggregate()) {
							isAggregate = true;
						} else {
							isRow = true;
						}
						if (isAggregate && isRow) {
							throw new CruxException(
									"Aggregator functions should be applied to all Alias");
						}
					}
				}
			}
		}
		logger.debug("reportDesignFunctionList:" + reportDesignFunctionList);
		return reportDesignFunctionList;
	}

	private boolean checkDashBoardOverLoaded(long id) throws CruxException {
		boolean result = false;
		int count = 0;
		ArrayList<Report> reportList = new ArrayList<Report>(
				reportDAO.findDashboardReports());
		for (Report report : reportList) {
			if (report.getDashboard() != null) {
				if (id != report.getId()) {
					if (count >= 3) {
						result = true;
						throw new CruxException(
								"Dashboard already have four reports.");
					} else {
						count++;
					}
				}
			}
		}
		return result;
	}

	private void populateRowAndColumnFilter(
			Map<String, ColumnAlias> hashColumnAlias,
			Map<String, RowAlias> hashRowAlias) throws CruxException {
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		ArrayList<ColumnFilter> columnFilterList = new ArrayList<ColumnFilter>();

		for (FilterAliasView aliasView : filterList) {
			if (hashRowAlias.containsKey(aliasView.getAlias())) {
				RowAlias rowAlias = hashRowAlias.get(aliasView.getAlias());
				isValidType(aliasView.getValue(), rowAlias.getValueType());
				rowFilterList.add(new RowAliasFilter(report,
						getFilterType(aliasView.getFilterType()), aliasView
								.getValue(), rowAlias));
			} else if (hashColumnAlias.containsKey(aliasView.getAlias())) {
				ColumnAlias columnAlias = hashColumnAlias.get(aliasView
						.getAlias());
				isValidType(aliasView.getValue(), columnAlias.getValueType());
				columnFilterList.add(new ColumnFilter(report,
						getFilterType(aliasView.getFilterType()), aliasView
								.getValue(), columnAlias));
			}
		}
		report.setColumnFilters(columnFilterList);
		report.setRowAliasFilters(rowFilterList);
	}

	private FilterType getFilterType(String filterName) {
		ArrayList<FilterType> filterTypeList = new ArrayList<FilterType>(
				filterTypeDAO.findAll());
		for (FilterType filterType : filterTypeList) {
			if (filterType.getType().equals(filterName)) {
				return filterType;
			}
		}
		return null;
	}

	private long getReportTypeId(String reportTypeName) {
		long reportTypeId = 0;
		List<ReportType> result = reportTypeDAO.findAll();
		for (ReportType reportTypes : result) {
			logger.debug("reportType is: " + reportTypeName);
			logger.debug("reportTypeObject is: " + reportTypes.getType());
			if (reportTypeName.equals(reportTypes.getType())) {
				reportTypeId = reportTypes.getId();
			}
		}
		return reportTypeId;
	}

	private boolean isValidType(String value, ValueType valueType)
			throws CruxException {
		boolean result = false;
		if (value == null || value.equals("")) {
			result = true;
		} else {
			String valuetypeClassName = valueType.getClassName();
			try {
				if (valuetypeClassName.equals("java.lang.Boolean")) {
					Boolean.parseBoolean(value);
				} else if (valuetypeClassName.equals("java.lang.Integer")) {
					Integer.parseInt(value);
				} else if (valuetypeClassName.equals("java.lang.Long")) {
					Long.parseLong(value);
				} else if (valuetypeClassName.equals("java.lang.Float")) {
					Float.parseFloat(value);
				} else if (valuetypeClassName.equals("java.lang.Double")) {
					Double.parseDouble(value);
				}
				result = true;
			} catch (Exception e) {
				throw new CruxException("Parse exception for fiterType value "
						+ e.getMessage());
			}
		}
		return result;
	}
	
	public void populateGroupBys(Map<String, RowAlias> hashRowAlias) throws CruxException{
		List<GroupBy> groupByList = new ArrayList<GroupBy>();
		for(GroupBysView groupBysView : groupBysViewList){
			if (hashRowAlias.containsKey(groupBysView.getAlias())) {
				RowAlias rowAlias = hashRowAlias.get(groupBysView.getAlias());
				GroupBy groupBy = new GroupBy();
				groupBy.setRowAlias(rowAlias);
				groupBy.setPosition(groupBysView.getIndex());
				groupByList.add(groupBy);			
				logger.debug("groupBy:" + groupBy);
			}
			
		}
		groupBys.setGroupBy(groupByList);
		groupBys.setReport(report);
		logger.debug("groupByListSize:" + groupByList.size());
		
		report.setGroupBys(groupBys);
	}
}
