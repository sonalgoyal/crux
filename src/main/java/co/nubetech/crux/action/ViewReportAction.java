/**
 * 
Copyright 2011 Nube Technologies
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
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.CruxResult;
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
	private List<ArrayList<Cell>> dataList = new ArrayList<ArrayList<Cell>>();
	private MappingDAO mappingDAO;

	public ViewReportAction() {
		mappingDAO = new MappingDAO();
	}

	public List<ArrayList<Cell>> getDataList() {
		return dataList;
	}

	public void setDataList(List<ArrayList<Cell>> dataList) {
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

	public List<ArrayList<Cell>> getData(Report report, Mapping mapping) {
		List<ArrayList<Cell>> dataList = new ArrayList<ArrayList<Cell>>();
		CruxScanner cruxScanner = null;
		try {
			Connection conn = mapping.getConnection();
			HBaseFacade hbaseFacade = this.getHBaseFacade();

			logger.debug("About to get data for Report:" + report);
			cruxScanner = hbaseFacade.execute(conn, report, mapping);
			logger.debug("Data fetched from HBaseFacade");

			List<ReportDesign> reportDesignList = report.getDesigns();
			
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
				int designSize = reportDesignList.size();
				CruxResult result = null;
				while ((result = cruxScanner.next()) != null) {
					dataList.add(getCellList(reportDesignList, result));
				}
				logger.debug("DataList is populated closing scanner");
			} 
			else {
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
			List<ReportDesign> reportDesignList, CruxResult r) throws CruxException {
		ArrayList<Cell> cellList = new ArrayList<Cell>();
		int i = 0;
		for (ReportDesign reportDesign : reportDesignList) {
			if (reportDesign.getRowAlias() != null) {
				RowAlias rowAlias = reportDesign.getRowAlias();
				cellList.add(new Cell(rowAlias.getAlias(), r.get(i)));
			} else if (reportDesign.getColumnAlias() != null) {
				ColumnAlias columnAlias = reportDesign.getColumnAlias();
				cellList.add(new Cell(columnAlias.getAlias(),
						r.get(i)));
			}
			i++;
		}
		return cellList;
	}

	/*
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
	}*/

	
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
