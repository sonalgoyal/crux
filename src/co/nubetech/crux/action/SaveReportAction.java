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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.common.functions.Conversion;
import co.nubetech.crux.common.functions.SubByteArray;
import co.nubetech.crux.dao.FilterTypeDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.dao.ReportTypeDAO;
import co.nubetech.crux.dao.UserDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.User;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.CruxScanner;
import co.nubetech.crux.server.HBaseFacade;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.Cell;
import co.nubetech.crux.view.FilterAliasView;

public class SaveReportAction extends CruxAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.SaveReportAction.class);

	private FilterTypeDAO filterTypeDAO = new FilterTypeDAO();
	private ReportDAO reportDAO = new ReportDAO();
	private ReportTypeDAO reportTypeDAO = new ReportTypeDAO();
	private UserDAO userDAO = new UserDAO();
	private MappingDAO mappingDAO = new MappingDAO();
	private String axisValues;

	private ReportType reportTypeObject = new ReportType();
	private User user = new User();
	private Report report = new Report();
	private String reportType;
	private long mappingId;
	private ArrayList<String> aliasList = new ArrayList<String>();
	private ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();

	private ArrayList<FilterAliasView> filterList = new ArrayList<FilterAliasView>();

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public Report getReport() {
		return report;
	}

	public ArrayList<FilterAliasView> getFilterList() {
		return filterList;
	}

	public void setFilterList(ArrayList<FilterAliasView> filterList) {
		this.filterList = filterList;
	}

	public long getMappingId() {
		return mappingId;
	}

	public void setMappingId(long mappingId) {
		this.mappingId = mappingId;
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

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public ArrayList<String> getAliasList() {
		return aliasList;
	}

	public void setAliasList(ArrayList<String> aliasList) {
		this.aliasList = aliasList;
	}

	public ArrayList<ArrayList> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<ArrayList> dataList) {
		this.dataList = dataList;
	}

	public String saveReport() {
		if (populateReport().equals("error")) {
			return "error";
		}
		logger.debug("Report Posted: " + report);
		try {
			reportDAO.save(report);
		} catch (CruxException e) {
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			error.setMessage("Something Wrong has happened");
		}
		if (error.isError()) {
			return "error";
		}
		return SUCCESS;
	}

	public String getDataAction() {
		try {
			if (report.getId() == 0 || axisValues != null) {
				String toReturn = populateReport();
				if (toReturn.equals("error")) {
					return SUCCESS;
				}
			} else {
				report = reportDAO.findById(report.getId());
				Mapping mapping = null;
				mapping = mappingDAO.findById(mappingId);
				Map<String, ColumnAlias> hashColumnAlias = mapping
						.getColumnAlias();
				Map<String, RowAlias> hashRowAlias = mapping.getRowAlias();
				populateRowAndColumnFilter(hashColumnAlias, hashRowAlias);
				ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>(
						report.getRowAliasFilters());
				ArrayList<ColumnFilter> columnFilterList = new ArrayList<ColumnFilter>(
						report.getColumnFilters());
				for (RowAliasFilter rowFilter : rowFilterList) {
					isValidType(rowFilter.getValue(), rowFilter.getRowAlias()
							.getValueType());
				}
				for (ColumnFilter columnFilter : columnFilterList) {
					isValidType(columnFilter.getValue(), columnFilter
							.getColumnAlias().getValueType());
				}
			}
			getData();
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
			return SUCCESS;
		}
		return SUCCESS;
	}

	public String getData() {
		dataList.clear();

		Mapping mapping = null;
		CruxScanner cruxScanner = null;
		try {
			mapping = mappingDAO.findById(mappingId);

			Connection conn = mapping.getConnection();
			HBaseFacade hbaseFacade = this.getHBaseFacade();
			
			logger.debug("About to get data for Report:" + report);
			cruxScanner = hbaseFacade.execute(conn, report, mapping);
			logger.debug("Data fetched from HBaseFacade");
			Result result = null;
			ArrayList<ReportDesign> reportDesignList = new ArrayList<ReportDesign>(
					report.getDesigns());

			if (cruxScanner != null) {
				logger.debug("About to create dataList");
				while ((result = cruxScanner.next()) != null) {
					if (!result.isEmpty()) {
						dataList.add(getCellList(reportDesignList, result));
					}
				}
				logger.debug("DataList is populated closing scanner");
				
			} else {
				error.setMessage("Cannot determine result.");
			}

		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
		} finally {
			if (cruxScanner != null) {
				cruxScanner.close();
			}
		}
		return SUCCESS;
	}

	private ArrayList<Cell> getCellList(
			ArrayList<ReportDesign> reportDesignList, Result result)
			throws CruxException {

		byte[] rowKey = result.getRow();
		if (rowKey == null) {
			throw new CruxException("The result set is empty.");
		}
		ArrayList<Cell> cellList = new ArrayList<Cell>();

		for (ReportDesign reportDesign : reportDesignList) {
			if (reportDesign.getRowAlias() != null) {
				RowAlias rowAlias = reportDesign.getRowAlias();

				SubByteArray sub = new SubByteArray(
						new HashMap<String, String>());
				sub.setProperty(sub.LENGTH_PROPERTY,
						getRowAliasLength(rowAlias, rowKey.length) + "");
				sub.setProperty(sub.OFFSET_PROPERTY, getOffset(rowAlias) + "");
				byte[] value = sub.execute(rowKey);
				Conversion conversion = new Conversion(
						new HashMap<String, String>());
				conversion.setProperty(conversion.CLASS_NAME_PROPERTY, rowAlias
						.getValueType().getClassName());
				cellList.add(new Cell(rowAlias.getAlias(), conversion
						.execute(value)));
			} else if (reportDesign.getColumnAlias() != null) {
				ColumnAlias columnAlias = reportDesign.getColumnAlias();
				byte[] value = result.getValue(
						Bytes.toBytes(columnAlias.getColumnFamily()),
						Bytes.toBytes(columnAlias.getQualifier()));
				if (value != null) {
					Conversion conversion = new Conversion(
							new HashMap<String, String>());
					conversion.setProperty(conversion.CLASS_NAME_PROPERTY,
							columnAlias.getValueType().getClassName());
					cellList.add(new Cell(columnAlias.getAlias(), conversion
							.execute(value)));
				}
			}
		}
		return cellList;
	}

	protected static int getOffset(RowAlias rowAlias) {
		Mapping mapping = rowAlias.getMapping();
		int offset = 0;
		Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		for (String alias : rowAliases.keySet()) {
			if (alias.equals(rowAlias.getAlias())) {
				break;
			} else {
				offset += rowAliases.get(alias).getLength();
			}
		}
		return offset;
	}

	protected static int getRowAliasLength(RowAlias rowAlias, int length) {
		if (!(rowAlias.getLength() == null || rowAlias.getLength() == 0)) {
			length = rowAlias.getLength().intValue();
		}
		return length;
	}

	private String populateReport() {
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
						if (hashColumnAlias.containsKey(axisNameValue[1])) {
							designList.add(new ReportDesign(report,
									hashColumnAlias.get(axisNameValue[1]),
									axisNameValue[0]));
						} else if (hashRowAlias.containsKey(axisNameValue[1])) {
							designList.add(new ReportDesign(report,
									hashRowAlias.get(axisNameValue[1]),
									axisNameValue[0]));
						}
					}
				}
			}
			report.setDesigns(designList);
			report.setUser(user);
			report.setReportType(reportTypeObject);

			populateRowAndColumnFilter(hashColumnAlias, hashRowAlias);
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
			return "error";
		}
		return SUCCESS;
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
			if (reportTypeName.contains(reportTypes.getType())) {
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
}
