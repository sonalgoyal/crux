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
package co.nubetech.crux.model;

import java.util.ArrayList;
import java.util.List;

public class ReportDesign {

	private long id;
	private Report report;
	private ColumnAlias columnAlias;
	private RowAlias rowAlias;
	private String mappingAxis;
	private List<ReportDesignFunction> reportDesignFunctionList = new ArrayList<ReportDesignFunction>();

	public ReportDesign() {

	}

	public ReportDesign(Report report, ColumnAlias columnAlias,
			String mappingAxis,
			ArrayList<ReportDesignFunction> reportDesignFunctionList) {
		super();
		this.report = report;
		this.columnAlias = columnAlias;
		this.mappingAxis = mappingAxis;
		this.reportDesignFunctionList = reportDesignFunctionList;
	}

	public ReportDesign(Report report, RowAlias rowAlias, String mappingAxis,
			ArrayList<ReportDesignFunction> reportDesignFunctionList) {
		super();
		this.report = report;
		this.rowAlias = rowAlias;
		this.mappingAxis = mappingAxis;
		this.reportDesignFunctionList = reportDesignFunctionList;
	}

	public RowAlias getRowAlias() {
		return rowAlias;
	}

	public void setRowAlias(RowAlias rowAlias) {
		this.rowAlias = rowAlias;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public ColumnAlias getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(ColumnAlias columnAlias) {
		this.columnAlias = columnAlias;
	}

	public String getMappingAxis() {
		return mappingAxis;
	}

	public void setMappingAxis(String mappingAxis) {
		this.mappingAxis = mappingAxis;
	}
	
	public List<ReportDesignFunction> getReportDesignFunctionList() {
		return reportDesignFunctionList;
	}

	public void setReportDesignFunctionList(
			List<ReportDesignFunction> reportDesignFunctionList) {
		this.reportDesignFunctionList = reportDesignFunctionList;
	}



	@Override
	public String toString() {
		return "ReportDesign [id=" + id + ", report=" + report.getId()
				+ ", columnAlias=" + columnAlias + ", rowAlias=" + rowAlias
				+ ", mappingAxis=" + mappingAxis
				+ ", reportDesignFunctionList=" + reportDesignFunctionList
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnAlias == null) ? 0 : columnAlias.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((mappingAxis == null) ? 0 : mappingAxis.hashCode());
		result = prime * result + ((report == null) ? 0 : new Long(report.getId()).hashCode());
		result = prime
				* result
				+ ((reportDesignFunctionList == null) ? 0
						: reportDesignFunctionList.hashCode());
		result = prime * result
				+ ((rowAlias == null) ? 0 : rowAlias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportDesign other = (ReportDesign) obj;
		if (columnAlias == null) {
			if (other.columnAlias != null)
				return false;
		} else if (!columnAlias.equals(other.columnAlias))
			return false;
		if (id != other.id)
			return false;
		if (mappingAxis == null) {
			if (other.mappingAxis != null)
				return false;
		} else if (!mappingAxis.equals(other.mappingAxis))
			return false;
		if (report == null) {
			if (other.report != null)
				return false;
		} else if (!(report.getId()==other.report.getId()))
			return false;
		if (reportDesignFunctionList == null) {
			if (other.reportDesignFunctionList != null)
				return false;
		} else if (!reportDesignFunctionList
				.equals(other.reportDesignFunctionList))
			return false;
		if (rowAlias == null) {
			if (other.rowAlias != null)
				return false;
		} else if (!rowAlias.equals(other.rowAlias))
			return false;
		return true;
	}

}
