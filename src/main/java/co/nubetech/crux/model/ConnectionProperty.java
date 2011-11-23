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

public class ConnectionProperty {

	private long id;
	private Connection connection;
	private String property;
	private String value;

	public ConnectionProperty() {
	}

	public ConnectionProperty(Connection connection, String property,
			String value) {
		this.connection = connection;
		this.property = property;
		this.value = value;
	}

	public ConnectionProperty(String property) {
		this.property = property;
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConnectionProperty [id=" + id + ", connection="
				+ connection.getId() + ", property=" + property + ", value="
				+ value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connection == null) ? 0 : new Long(connection.getId()).hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ConnectionProperty other = (ConnectionProperty) obj;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!(connection.getId() == other.connection.getId()))
			return false;
		if (id != other.id)
			return false;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
