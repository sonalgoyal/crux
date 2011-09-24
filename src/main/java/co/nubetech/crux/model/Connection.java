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
import java.util.Map;

public class Connection {

	private long id;
	private Datastore datastore;
	private User user;
	private String name;
	private Map<String, ConnectionProperty> properties;

	public Connection() {
		datastore = new Datastore();
		properties = new HashMap<String, ConnectionProperty>();

	}

	public Connection(Datastore datastore, User user, String name,
			Map<String, ConnectionProperty> prop) {
		this.datastore = datastore;
		this.user = user;
		this.name = name;
		this.properties = prop;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public User getUser() {
		return user;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ConnectionProperty> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, ConnectionProperty> properties) {
		this.properties = properties;
	}

	public void addProperty(ConnectionProperty prop) {
		prop.setConnection(this);
		properties.put(prop.getProperty(), prop);
	}

	@Override
	public String toString() {
		return "Connection [id=" + id + ", datastore=" + datastore + ", user="
				+ user + ", name=" + name + ", properties=" + properties + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((datastore == null) ? 0 : datastore.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Connection other = (Connection) obj;
		if (datastore == null) {
			if (other.datastore != null)
				return false;
		} else if (!datastore.equals(other.datastore))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	

}
