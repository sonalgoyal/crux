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

import co.nubetech.crux.model.ColumnAlias;

public class ColumnAliasView {

	// public static long count;
	// private long index;
	private long id;
	private String columnFamily;
	private String qualifier;
	private String alias;
	private String columnTypeName;

	public ColumnAliasView() {
	}

	public ColumnAliasView(ColumnAlias columnAlias) {
		// ++count;
		// setIndex(count);
		this.id = columnAlias.getId();
		this.columnFamily = columnAlias.getColumnFamily();
		this.qualifier = columnAlias.getQualifier();
		this.alias = columnAlias.getAlias();
		this.columnTypeName = columnAlias.getValueType().getName();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public String getQualifier() {
		return qualifier;
	}

	public String getAlias() {
		return alias;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	@Override
	public String toString() {
		return "ColumnAliasView [id=" + id + ", columnFamily=" + columnFamily
				+ ", qualifier=" + qualifier + ", alias=" + alias
				+ ", columnTypeName=" + columnTypeName + "]";
	}

}
