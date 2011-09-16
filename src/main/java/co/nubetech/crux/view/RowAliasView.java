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

import co.nubetech.crux.model.RowAlias;

public class RowAliasView {

	private Integer length;
	private String alias;
	private String columnTypeName;
	private long id;

	public RowAliasView() {
	}

	public RowAliasView(RowAlias rowAlias) {
		this.id = rowAlias.getId();
		this.length = rowAlias.getLength();
		this.alias = rowAlias.getAlias();
		this.columnTypeName = rowAlias.getValueType().getName();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getLength() {
		return length;
	}

	public String getAlias() {
		return alias;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	@Override
	public String toString() {
		return "RowAliasView [length=" + length + ", alias=" + alias
				+ ", columnTypeName=" + columnTypeName + ", id=" + id + "]";
	}

}
