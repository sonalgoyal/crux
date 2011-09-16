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

import co.nubetech.crux.model.Mapping;

public class MappingView {

	private long index;
	private long id;
	private String name;
	private String connectionName;
	private String tableName;

	public MappingView() {
	}

	public MappingView(long index, Mapping mapping) {
		this.index = index;
		this.id = mapping.getId();
		this.name = mapping.getName();
		this.connectionName = mapping.getConnection().getName();
		this.tableName = mapping.getTableName();
	}

	public long getIndex() {
		return index;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String toString() {
		return "MappingView [index=" + index + ", id=" + id + ", name=" + name
				+ ", connectionName=" + connectionName + ", tableName="
				+ tableName + "]";
	}

}
