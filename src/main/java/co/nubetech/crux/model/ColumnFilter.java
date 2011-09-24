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

public class ColumnFilter extends BaseFilter {

	private ColumnAlias columnAlias;

	public ColumnFilter() {

	}

	public ColumnFilter(Report report, FilterType filterType, String value,
			ColumnAlias columnAlias) {
		this.report = report;
		this.filterType = filterType;
		this.value = value;
		this.columnAlias = columnAlias;
	}

	public ColumnAlias getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(ColumnAlias columnAlias) {
		this.columnAlias = columnAlias;
	}

	@Override
	public String toString() {
		return "ColumnFilter [columnAlias=" + columnAlias + ", id=" + id
				+ ", report=" + ((report == null) ? 0 : report.getId()) + ", filterType=" + filterType
				+ ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((columnAlias == null) ? 0 : columnAlias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnFilter other = (ColumnFilter) obj;
		if (columnAlias == null) {
			if (other.columnAlias != null)
				return false;
		} else if (!columnAlias.equals(other.columnAlias))
			return false;
		return true;
	}
}
