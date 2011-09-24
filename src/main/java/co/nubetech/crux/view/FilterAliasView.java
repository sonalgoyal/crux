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

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.RowAliasFilter;

public class FilterAliasView {
	private String alias;
	private String filterType;
	private String value;

	public FilterAliasView() {

	}

	public FilterAliasView(RowAliasFilter rowFilter) {
		alias = rowFilter.getRowAlias().getAlias();
		filterType = rowFilter.getFilterType().getType();
		value = rowFilter.getValue();
	}

	public FilterAliasView(ColumnFilter columnFilter) {
		alias = columnFilter.getColumnAlias().getAlias();
		filterType = columnFilter.getFilterType().getType();
		value = columnFilter.getValue();
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FilterAliasView [alias=" + alias + ", filterType=" + filterType
				+ ", value=" + value + "]";
	}

}
