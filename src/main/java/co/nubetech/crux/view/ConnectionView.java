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

import org.apache.log4j.Logger;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.util.CruxConstants;

public class ConnectionView {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.view.ConnectionView.class);

	private long index;
	private long id;
	private String name;
	private String datastoreName;
	// private String hostPropertyValue;
	// private String portPropertyValue;
	private String hbaseRestServerPropertyValue;

	public ConnectionView() {
	}

	public ConnectionView(long index, Connection connection) {
		this.index = index;
		this.id = connection.getId();
		this.name = connection.getName();
		this.datastoreName = connection.getDatastore().getName();
		/*
		 * this.hostPropertyValue = connection.getProperties()
		 * .get(CruxConstants.HBASE_HOST_PROPERTY).getValue();
		 * this.portPropertyValue = connection.getProperties()
		 * .get(CruxConstants.HBASE_PORT_PROPERTY).getValue();
		 */
		logger.debug("Connection view: ");

		this.hbaseRestServerPropertyValue = connection.getProperties()
				.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue();
	}

	public String getHbaseRestServerPropertyValue() {
		return hbaseRestServerPropertyValue;
	}

	public void setHbaseRestServerPropertyValue(
			String hbaseRestServerPropertyValue) {
		this.hbaseRestServerPropertyValue = hbaseRestServerPropertyValue;
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

	public String getDatastoreName() {
		return datastoreName;
	}

	/*
	 * public String getHostPropertyValue() { return hostPropertyValue; }
	 * 
	 * public String getPortPropertyValue() { return portPropertyValue; }
	 */

	public void setIndex(long index) {
		this.index = index;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDatastoreName(String datastoreName) {
		this.datastoreName = datastoreName;
	}

	/*
	 * public void setHostPropertyValue(String hostPropertyValue) {
	 * this.hostPropertyValue = hostPropertyValue; }
	 * 
	 * public void setPortPropertyValue(String portPropertyValue) {
	 * this.portPropertyValue = portPropertyValue; }
	 */

}
