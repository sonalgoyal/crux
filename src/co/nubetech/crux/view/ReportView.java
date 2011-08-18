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
package co.nubetech.crux.view;

import co.nubetech.crux.model.Report;

public class ReportView {

	private long index;
	private long id;
	private String name;
	private String reportType;

	public ReportView() {
	}

	public ReportView(long index, Report report) {
		this.index = index;
		id = report.getId();
		name = report.getName();
		reportType = report.getReportType().getType();
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getIndex() {
		return index;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Override
	public String toString() {
		return "ReportView [index=" + index + ", id=" + id + ", name=" + name
				+ ", reportType=" + reportType + "]";
	}

}
