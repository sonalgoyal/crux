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
package co.nubetech.crux.pool.rest;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.util.CruxConstants;

public class HBaseConnectionPoolFactory implements KeyedPoolableObjectFactory {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.pool.HBaseConnectionPoolFactory.class);

	public Cluster getCluster(Connection conn) {
		Cluster cluster = new Cluster();
		String hbaseRestServerPropertyValue = conn.getProperties()
				.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue();
		String[] hbaseRestServerPropertyValueArray = hbaseRestServerPropertyValue
				.split(",");
		logger.debug("hbaseRestServerPropertyValueArray.length is: "
				+ hbaseRestServerPropertyValueArray.length);
		for (int i = 0; i < hbaseRestServerPropertyValueArray.length; i++) {
			String[] hbaseRestServerHostAndPort = hbaseRestServerPropertyValueArray[i]
					.split(":");
			String host = hbaseRestServerHostAndPort[0];
			int port = Integer.parseInt(hbaseRestServerHostAndPort[1]);

			logger.debug("Host is: " + host);
			logger.debug("Port is: " + port);
			cluster.add(host, port);
		}
		logger.debug("Returning Cluster: " + cluster);
		return cluster;
	}

	@Override
	public void activateObject(Object arg0, Object arg1) throws Exception {

	}

	@Override
	public void destroyObject(Object key, Object obj) throws Exception {
		// Not sure it works or not.

		Client client = (Client) key;
		Cluster cluster = client.getCluster();
		Connection conn = (Connection) obj;

		String hbaseRestServerPropertyValue = conn.getProperties()
				.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue();
		String[] hbaseRestServerPropertyValueArray = hbaseRestServerPropertyValue
				.split(",");
		logger.debug("hbaseRestServerPropertyValueArray.length is: "
				+ hbaseRestServerPropertyValueArray.length);
		for (int i = 0; i < hbaseRestServerPropertyValueArray.length; i++) {
			String[] hbaseRestServerHostAndPort = hbaseRestServerPropertyValueArray[i]
					.split(":");
			String host = hbaseRestServerHostAndPort[0];
			int port = Integer.parseInt(hbaseRestServerHostAndPort[1]);

			logger.debug("Host is: " + host);
			logger.debug("Port is: " + port);
			cluster.remove(host, port);
		}
	}

	@Override
	public Object makeObject(Object obj) {
		Connection connection = (Connection) obj;
		Cluster cluster = getCluster(connection);
		Client client = new Client(cluster);
		return client;
	}

	@Override
	public void passivateObject(Object arg0, Object arg1) throws Exception {

	}

	@Override
	public boolean validateObject(Object arg0, Object arg1) {
		return true;
	}

}
