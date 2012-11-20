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

import java.io.Serializable;
import java.util.List;

public class GroupBys implements Serializable{
	
	private long id;	
	private List<GroupBy> groupBy;	
	private Report report;
	
	public GroupBys(long id, List<GroupBy> groupBy, Report report) {
		super();
		this.id = id;
		this.groupBy = groupBy;
		this.report = report;
	}

	public GroupBys() {		
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the groupBy
	 */
	public List<GroupBy> getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy the groupBy to set
	 */
	public void setGroupBy(List<GroupBy> groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * @return the report
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(Report report) {
		this.report = report;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupBy == null) ? 0 : groupBy.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((report == null) ? 0 : report.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GroupBys)) {
			return false;
		}
		GroupBys other = (GroupBys) obj;
		if (groupBy == null) {
			if (other.groupBy != null) {
				return false;
			}
		} else if (!groupBy.equals(other.groupBy)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (report == null) {
			if (other.report != null) {
				return false;
			}
		} else if (!report.equals(other.report)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupBys [id=" + id + ", groupBy=" + groupBy + ", report="
				+ report + "]";
	}
	
	

		
}
