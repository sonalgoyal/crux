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

import java.util.Map;

public class RowAlias extends Alias {

	private Integer length;

	public RowAlias() {
	}

	public RowAlias(long id, Mapping mapping, ValueType valueType, Integer length,
			String alias) {
		this.id = id;
		this.mapping = mapping;
		this.valueType = valueType;
		this.length = length;
		this.alias = alias;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "RowAlias [id=" + id + ", mapping=" + ((mapping == null) ? 0 : mapping.getId())
				+ ", valueType=" + ((valueType == null) ? 0 : valueType.getId())+ ", length=" + length
				+ ", alias=" + alias + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((length == null) ? 0 : length.hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof RowAlias)) {
			return false;
		}
		RowAlias other = (RowAlias) obj;
		if (length == null) {
			if (other.length != null) {
				return false;
			}
		} else if (!length.equals(other.length)) {
			return false;
		}
		return true;
	}

	public int getOffset() {
		int offset = 0;
		Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		for (String alias : rowAliases.keySet()) {
			if (alias.equals(this.getAlias())) {
				break;
			} else {
				offset += rowAliases.get(alias).getLength();
			}
		}
		return offset;
	}
}
