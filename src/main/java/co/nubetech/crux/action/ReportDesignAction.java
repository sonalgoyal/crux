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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.FunctionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportTypeDAO;
import co.nubetech.crux.dao.RowAliasDAO;
import co.nubetech.crux.dao.ValueFilterTypeDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.FunctionTypeMapping;
import co.nubetech.crux.model.GroupBy;
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
import co.nubetech.crux.view.GroupBysView;

public class ReportDesignAction extends ViewReportListAction {

	private static final long serialVersionUID = 1L;

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ReportDesignAction.class);

	protected ReportTypeDAO reportTypeDAO = new ReportTypeDAO();
	private ValueFilterTypeDAO valueFilterTypeDAO = new ValueFilterTypeDAO();
	private RowAliasDAO rowAliasDAO = new RowAliasDAO();
	private ArrayList<ReportType> reportTypeList;
	private ArrayList<FilterType> filterTypeList;
	protected long mappingId;
	protected Report report = new Report();
	private String edit = "false";
	protected String reportType;
	protected String axisValues = new String();
	private String aliasName;
	protected boolean addToDashBoard;
	private ArrayList<DimensionAndMeasureView> dimensionAndMeasureViewList = new ArrayList<DimensionAndMeasureView>();
	private ArrayList<FunctionView> functionViewList = new ArrayList<FunctionView>();
	private ArrayList<RowAlias> rowAliasList;
	protected FunctionDAO functionDAO = new FunctionDAO();

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

	public ReportTypeDAO getReportTypeDAO() {
		return reportTypeDAO;
	}

	public void setReportTypeDAO(ReportTypeDAO reportTypeDAO) {
		this.reportTypeDAO = reportTypeDAO;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public ValueFilterTypeDAO getValueFilterTypeDAO() {
		return valueFilterTypeDAO;
	}

	public void setValueFilterTypeDAO(ValueFilterTypeDAO valueFilterTypeDAO) {
		this.valueFilterTypeDAO = valueFilterTypeDAO;
	}

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public FunctionDAO getFunctionDAO() {
		return functionDAO;
	}

	public void setFunctionDAO(
			FunctionDAO functionDAO) {
		this.functionDAO = functionDAO;
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

	
	public ArrayList<GroupBysView> getGroupByViewList() {
		return groupByViewList;
	}

	public void setGroupByViewList(ArrayList<GroupBysView> groupByViewList) {
		this.groupByViewList = groupByViewList;
	}

	public ArrayList<RowAlias> getRowAliasList() {
		return rowAliasList;
	}

	public void setRowAliasList(ArrayList<RowAlias> rowAliasList) {
		this.rowAliasList = rowAliasList;
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

	public RowAliasDAO getRowAliasDAO() {
		return rowAliasDAO;
	}

	public void setRowAliasDAO(RowAliasDAO rowAliasDAO) {
		this.rowAliasDAO = rowAliasDAO;
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
		List<Function> functions = functionDAO.findAll();
		for (Function func : functions) {
			functionViewList.add(new FunctionView(func));
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
			if (report.getDashboard() != null) {
				addToDashBoard = true;
			} else {
				addToDashBoard = false;
			}
			mappingList = populateMappingList(mappingDAO, mappingList);
			getFilterAliasView();
			getGroupByView();
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
	
	public String populateGroupBy() {
	     Map<String, RowAlias> rowAlias = null;
	     rowAliasList = new ArrayList<RowAlias>();
	     		if (mappingId != 0) {
				  try {
					 Iterator<RowAlias> iterator = mappingDAO.findById(mappingId).getRowAlias().values().iterator();
					 logger.debug("RowAlias :"+rowAlias);
					 while (iterator.hasNext()) {
			        	 rowAliasList.add(iterator.next());
			         }
				  } catch (CruxException e) {
					error.setMessage(e.getMessage());
					return SUCCESS;
				    }
	     	}
	     	Iterator<RowAlias> itr = rowAliasList.iterator();
	     	while(itr.hasNext()){
	     	    
	     		logger.debug("RowAlias List"+itr.next());
	     	}
	     		
	     	return SUCCESS; 
	}
	
	public void getGroupByView() throws CruxException {
		   int index = 0;
		   try{
		     ArrayList<GroupBy> groupByList = new ArrayList<GroupBy>(report.getGroupBys().getGroupBy());
		     for(GroupBy groupBy : groupByList){
		       groupByViewList.add(new GroupBysView(++index, groupBy.getRowAlias()));  
		     }
		   }catch(Exception e){
			   error.setMessage(e.getMessage());
		   }
	}
}
