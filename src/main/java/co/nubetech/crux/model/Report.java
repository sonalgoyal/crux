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
import java.util.Collection;

public class Report {

	private long id;
	private User user;
	private String name;
	private ReportType reportType;
	private short dashboardType;
	private Collection<ReportDesign> designs = new ArrayList<ReportDesign>();
	private Collection<RowAliasFilter> rowAliasFilters = new ArrayList<RowAliasFilter>();
	private Collection<ColumnFilter> columnFilters = new ArrayList<ColumnFilter>();

	public Report() {

	}

	public Report(User user, String name, ReportType reportType,
			short isDashBoard) {
		super();
		this.user = user;
		this.name = name;
		this.reportType = reportType;
		this.dashboardType = isDashBoard;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Collection<RowAliasFilter> getRowAliasFilters() {
		return rowAliasFilters;
	}

	public void setRowAliasFilters(Collection<RowAliasFilter> rowAliasFilters) {
		this.rowAliasFilters = rowAliasFilters;
	}

	public Collection<ColumnFilter> getColumnFilters() {
		return columnFilters;
	}

	public void setColumnFilters(Collection<ColumnFilter> columnFilters) {
		this.columnFilters = columnFilters;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<ReportDesign> getDesigns() {
		return designs;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public void setDesigns(Collection<ReportDesign> designs) {
		this.designs = designs;
	}

	public void addDesign(ReportDesign design) {
		design.setReport(this);
		designs.add(design);
	}

	
	public short getDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(short dashboardType) {
		this.dashboardType = dashboardType;
	}

	

	@Override
	public String toString() {
		return "Report [id=" + id + ", user=" + user + ", name=" + name
				+ ", reportType=" + reportType + ", isDashBoard=" + dashboardType
				+ ", designs=" + designs + ", rowAliasFilters="
				+ rowAliasFilters + ", columnFilters=" + columnFilters + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnFilters == null) ? 0 : columnFilters.hashCode());
		result = prime * result + ((designs == null) ? 0 : designs.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + dashboardType;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((reportType == null) ? 0 : reportType.hashCode());
		result = prime * result
				+ ((rowAliasFilters == null) ? 0 : rowAliasFilters.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Report other = (Report) obj;
		if (columnFilters == null) {
			if (other.columnFilters != null)
				return false;
		} else if (!columnFilters.equals(other.columnFilters))
			return false;
		if (designs == null) {
			if (other.designs != null)
				return false;
		} else if (!designs.equals(other.designs))
			return false;
		if (id != other.id)
			return false;
		if (dashboardType != other.dashboardType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reportType == null) {
			if (other.reportType != null)
				return false;
		} else if (!reportType.equals(other.reportType))
			return false;
		if (rowAliasFilters == null) {
			if (other.rowAliasFilters != null)
				return false;
		} else if (!rowAliasFilters.equals(other.rowAliasFilters))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	
}
