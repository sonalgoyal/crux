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
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.functions.Conversion;
import co.nubetech.crux.functions.SubByteArray;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.CruxScanner;
import co.nubetech.crux.server.HBaseFacade;
import co.nubetech.crux.util.CruxError;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.view.Cell;
import co.nubetech.crux.view.FilterAliasView;

public class ViewReportAction extends ViewReportListAction {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.ViewReportAction.class);

	private Report report = new Report();
	private String axisValues = new String();
	private ArrayList<FilterAliasView> filterList = new ArrayList<FilterAliasView>();
	private long mappingId;
	private ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();
	private MappingDAO mappingDAO;

	public ViewReportAction() {
		mappingDAO = new MappingDAO();
	}

	public ArrayList<ArrayList> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<ArrayList> dataList) {
		this.dataList = dataList;
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

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public String viewReport() {
		String returnString = SUCCESS;
		logger.debug("Report Id: " + report.getId());
		try {
			report = reportDAO.findById(report.getId());

			logger.debug("Report To View: " + report);
			ArrayList<ReportDesign> reportDesignList = new ArrayList<ReportDesign>(
					report.getDesigns());
			mappingId = (reportDesignList.get(0).getColumnAlias() != null) ? reportDesignList
					.get(0).getColumnAlias().getMapping().getId()
					: reportDesignList.get(0).getRowAlias().getMapping()
							.getId();
		
			getFilterAliasView();
			logger.debug("xAxis: " + axisValues);

		} catch (CruxException e) {
			error.setMessage("Error: " + e.getMessage());
			displayReportList();
			returnString = "error";
		}
		return returnString;
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

	public String findReportData() {
		try {
			report = reportDAO.findById(report.getId());
			ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>(
					report.getRowAliasFilters());
			ArrayList<ColumnFilter> columnFilterList = new ArrayList<ColumnFilter>(
					report.getColumnFilters());
			setValueToRowAndColumnFilter(rowFilterList, columnFilterList);

			for (RowAliasFilter rowFilter : rowFilterList) {
				isValidType(rowFilter.getValue(), rowFilter.getRowAlias()
						.getValueType());
			}
			for (ColumnFilter columnFilter : columnFilterList) {
				isValidType(columnFilter.getValue(), columnFilter
						.getColumnAlias().getValueType());
			}
			logger.debug("Calling getData function in ViewReportAction.class");
			dataList = getData(report, mappingDAO.findById(mappingId));
			logger.debug("Returning Success");
		} catch (CruxException e) {
			e.printStackTrace();
			error.setMessage(e.getMessage());
			return SUCCESS;
		}
		return SUCCESS;
	}

	public ArrayList<ArrayList> getData(Report report, Mapping mapping) {
		ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();
		CruxScanner cruxScanner = null;
		try {
			Connection conn = mapping.getConnection();
			HBaseFacade hbaseFacade = this.getHBaseFacade();

			logger.debug("About to get data for Report:" + report);
			cruxScanner = hbaseFacade.execute(conn, report, mapping);
			logger.debug("Data fetched from HBaseFacade");
			Result result = null;
			ArrayList<ReportDesign> reportDesignList = new ArrayList<ReportDesign>(
					report.getDesigns());
			
			for (ReportDesign reportDesign : reportDesignList) {
				String alias = (reportDesign.getColumnAlias() != null) ? reportDesign
						.getColumnAlias().getAlias() : reportDesign
						.getRowAlias().getAlias();
				axisValues = axisValues + reportDesign.getMappingAxis() + ","
						+ alias + ":";
				logger.debug("AxisValues:"+axisValues);
			}

			if (cruxScanner != null) {
				logger.debug("About to create dataList");
				ArrayList<Conversion> conversionList = getConversionList(reportDesignList);
				ArrayList<SubByteArray> subByteArrayList = getSubByteArrayList(reportDesignList);
				while ((result = cruxScanner.next()) != null) {
					if (!result.isEmpty()) {
						dataList.add(getCellList(reportDesignList, result,
								conversionList, subByteArrayList));
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
		return dataList;
	}

	private ArrayList<Cell> getCellList(
			ArrayList<ReportDesign> reportDesignList, Result result,
			ArrayList<Conversion> conversionList,
			ArrayList<SubByteArray> subByteArrayList) throws CruxException {

		byte[] rowKey = result.getRow();
		if (rowKey == null) {
			throw new CruxException("The result set is empty.");
		}
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		int i = 0;
		int j = 0;
		for (ReportDesign reportDesign : reportDesignList) {
			if (reportDesign.getRowAlias() != null) {
				RowAlias rowAlias = reportDesign.getRowAlias();
				byte[] value = subByteArrayList.get(j).execute(rowKey);
				cellList.add(new Cell(rowAlias.getAlias(), conversionList
						.get(i).execute(value)));
				j++;
			} else if (reportDesign.getColumnAlias() != null) {
				ColumnAlias columnAlias = reportDesign.getColumnAlias();
				byte[] value = result.getValue(
						Bytes.toBytes(columnAlias.getColumnFamily()),
						Bytes.toBytes(columnAlias.getQualifier()));
				if (value != null) {
					cellList.add(new Cell(columnAlias.getAlias(),
							conversionList.get(i).execute(value)));
				}
			}
			i++;
		}
		return cellList;
	}

	private ArrayList<SubByteArray> getSubByteArrayList(
			ArrayList<ReportDesign> reportDesignList) {
		ArrayList<SubByteArray> subByteArrayList = new ArrayList<SubByteArray>();
		for (ReportDesign reportDesign : reportDesignList) {
			if (reportDesign.getRowAlias() != null) {
				RowAlias rowAlias = reportDesign.getRowAlias();

				HashMap<String, String> properties = new HashMap<String, String>();
				properties.put("offset", getOffset(rowAlias) + "");
				properties.put("length", getRowAliasLength(rowAlias) + "");

				subByteArrayList.add(new SubByteArray(properties));
			}
		}
		return subByteArrayList;
	}

	private ArrayList<Conversion> getConversionList(
			ArrayList<ReportDesign> reportDesignList) {
		ArrayList<Conversion> conversionList = new ArrayList<Conversion>();
		for (ReportDesign reportDesign : reportDesignList) {
			if (reportDesign.getRowAlias() != null) {
				RowAlias rowAlias = reportDesign.getRowAlias();

				HashMap<String, String> properties = new HashMap<String, String>();
				properties.put("class.name", rowAlias.getValueType()
						.getClassName());

				conversionList.add(new Conversion(properties));

			} else if (reportDesign.getColumnAlias() != null) {
				ColumnAlias columnAlias = reportDesign.getColumnAlias();
				HashMap<String, String> properties = new HashMap<String, String>();
				properties.put("class.name", columnAlias.getValueType()
						.getClassName());
				conversionList.add(new Conversion(properties));
			}
		}

		return conversionList;
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

	protected static int getRowAliasLength(RowAlias rowAlias) {
		int length = 0;
		if (!(rowAlias.getLength() == null || rowAlias.getLength() == 0)) {
			length = rowAlias.getLength().intValue();
		}
		return length;
	}

	private void setValueToRowAndColumnFilter(
			ArrayList<RowAliasFilter> rowFilterList,
			ArrayList<ColumnFilter> columnFilterList) {
		for (RowAliasFilter rowFilter : rowFilterList) {
			rowFilter.setValue(getValueForFilters(rowFilter.getRowAlias()
					.getAlias(), rowFilter.getFilterType().getType()));
			mappingDAO.session.setReadOnly(rowFilter, true);
		}
		for (ColumnFilter columnFilter : columnFilterList) {
			columnFilter.setValue(getValueForFilters(columnFilter
					.getColumnAlias().getAlias(), columnFilter.getFilterType()
					.getType()));
			mappingDAO.session.setReadOnly(columnFilter, true);
		}
	}

	private String getValueForFilters(String alias, String filterType) {
		String result = "";
		for (FilterAliasView aliasView : filterList) {
			if (aliasView.getAlias().equals(alias)) {
				if (aliasView.getFilterType().equals(filterType)) {
					result = aliasView.getValue();
					break;
				}
			}
		}
		return result;
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
