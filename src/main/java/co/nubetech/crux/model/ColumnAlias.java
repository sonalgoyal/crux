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

public class ColumnAlias extends Alias {

	private String columnFamily;
	private String qualifier;

	public ColumnAlias() {
	}

	public ColumnAlias(Mapping mapping, ValueType valueType,
			String columnFamily, String qualifier, String alias) {
		super();
		this.mapping = mapping;
		this.valueType = valueType;
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		this.alias = alias;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	@Override
	public String toString() {
		return "ColumnAlias [id=" + id + ", mapping=" + ((mapping == null) ? 0 : mapping.getId())
				+ ", valueType=" + ((valueType == null) ? 0 : valueType.getId()) + ", columnFamily="
				+ columnFamily + ", qualifier=" + qualifier + ", alias="
				+ alias + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((columnFamily == null) ? 0 : columnFamily.hashCode());
		result = prime * result
				+ ((qualifier == null) ? 0 : qualifier.hashCode());
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
		ColumnAlias other = (ColumnAlias) obj;
		if (columnFamily == null) {
			if (other.columnFamily != null)
				return false;
		} else if (!columnFamily.equals(other.columnFamily))
			return false;
		if (qualifier == null) {
			if (other.qualifier != null)
				return false;
		} else if (!qualifier.equals(other.qualifier))
			return false;
		return true;
	}

}
