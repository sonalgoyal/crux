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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Mapping {

	private long id;
	private Connection connection;
	private String name;
	private String tableName;
	private Map<String, ColumnAlias> columnAlias;
	private SortedMap<String, RowAlias> rowAlias;

	public Mapping() {
		connection = new Connection();
		columnAlias = new HashMap<String, ColumnAlias>();
		rowAlias = new TreeMap<String, RowAlias>();
	}

	public Mapping(Connection connection, String name, String tableName,
			Map<String, ColumnAlias> columnAlias, SortedMap<String, RowAlias> rowAlias) {
		super();
		this.connection = connection;
		this.name = name;
		this.tableName = tableName;
		this.columnAlias = columnAlias;
		this.rowAlias = rowAlias;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Map<String, ColumnAlias> getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(Map<String, ColumnAlias> columnAlias) {
		this.columnAlias = columnAlias;
	}

	public Map<String, RowAlias> getRowAlias() {
		return rowAlias;
	}

	public void setRowAlias(SortedMap<String, RowAlias> rowAlias) {
		this.rowAlias = rowAlias;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void addColumnAlias(ColumnAlias columnAlias) {
		columnAlias.setMapping(this);
		this.columnAlias.put(columnAlias.getAlias(), columnAlias);
	}

	public void addRowAlias(RowAlias rowAlias) {
		rowAlias.setMapping(this);
		this.rowAlias.put(rowAlias.getAlias(), rowAlias);
	}

	@Override
	public String toString() {
		return "Mapping [id=" + id + ", connection=" + ((connection == null) ? 0 :connection.getId())
				+ ", name=" + name + ", tableName=" + tableName
				+ ", columnAlias=" + columnAlias + ", rowAlias=" + rowAlias
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnAlias == null) ? 0 : columnAlias.hashCode());
		result = prime * result
				+ ((connection == null) ? 0 : connection.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((rowAlias == null) ? 0 : rowAlias.hashCode());
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
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
		Mapping other = (Mapping) obj;
		if (columnAlias == null) {
			if (other.columnAlias != null)
				return false;
		} else if (!columnAlias.equals(other.columnAlias))
			return false;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!connection.equals(other.connection))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rowAlias == null) {
			if (other.rowAlias != null)
				return false;
		} else if (!rowAlias.equals(other.rowAlias))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

}
