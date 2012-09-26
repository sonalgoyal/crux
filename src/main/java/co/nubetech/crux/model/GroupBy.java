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

public class GroupBy {
	private long id;
	private RowAlias rowAlias;
	private int position;
	
	public GroupBy() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupBy(long id, RowAlias alias, int order) {
		super();
		this.id = id;
		this.rowAlias = alias;
		this.position = order;
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
	 * @return the rowAlias
	 */
	public RowAlias getRowAlias() {
		return rowAlias;
	}

	/**
	 * @param rowAlias the rowAlias to set
	 */
	public void setRowAlias(RowAlias alias) {
		this.rowAlias = alias;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int order) {
		this.position = order;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rowAlias == null) ? 0 : rowAlias.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + position;
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
		if (!(obj instanceof GroupBy)) {
			return false;
		}
		GroupBy other = (GroupBy) obj;
		if (rowAlias == null) {
			if (other.rowAlias != null) {
				return false;
			}
		} else if (!rowAlias.equals(other.rowAlias)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (position != other.position) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupBy [id=" + id + ", rowAlias=" + rowAlias + ", position=" + position
				+ "]";
	}
	
		
}
