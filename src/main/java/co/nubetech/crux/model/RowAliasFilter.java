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

public class RowAliasFilter extends BaseFilter {

	private RowAlias rowAlias;

	public RowAliasFilter() {

	}

	public RowAliasFilter(Report report, FilterType filterType, String value,
			RowAlias rowAlias) {
		this.report = report;
		this.filterType = filterType;
		this.value = value;
		this.rowAlias = rowAlias;
	}

	public RowAlias getRowAlias() {
		return rowAlias;
	}

	public void setRowAlias(RowAlias rowAlias) {
		this.rowAlias = rowAlias;
	}

	@Override
	public String toString() {
		return "RowAliasFilter [RowAlias=" + rowAlias + ", id=" + id + ", report="
				+ ((report == null) ? 0 : report.getId()) + ", filterType=" + filterType + ", value="
				+ value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((rowAlias == null) ? 0 : rowAlias.hashCode());
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
		RowAliasFilter other = (RowAliasFilter) obj;
		if (rowAlias == null) {
			if (other.rowAlias != null)
				return false;
		} else if (!rowAlias.equals(other.rowAlias))
			return false;
		return true;
	}
}
