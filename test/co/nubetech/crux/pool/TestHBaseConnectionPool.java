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
package co.nubetech.crux.pool;

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.log4j.Logger;
import org.junit.Test;

import co.nubetech.crux.dao.DBConnection;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.util.CruxConstants;

public class TestHBaseConnectionPool extends DBConnection {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.pool.TestHBaseConnectionPool.class);

	@Test
	public void testSave() throws Exception {
		Connection connection = new Connection();
		connection.setName("HbaseConn1");
		ConnectionProperty connectionProperty = new ConnectionProperty();
		connectionProperty.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		connectionProperty.setValue("h1:2181,h2:8080");
		connection.addProperty(connectionProperty);

		HBaseConnectionPool pool = new HBaseConnectionPool(
				new HBaseConnectionPoolFactory());

		// pool.save(connection);

		// If the object is not available in pool. Then borrow object
		// is adding it to pool. Need another way to get client.

	/*	Client client = (Client) pool.borrowObject(connection);
		Cluster cluster = client.getCluster();
		logger.debug("Cluster is empty? : " + cluster.isEmpty());
		assertEquals(cluster.isEmpty(), false);*/
	}

	/*
	 * @Test public void testDelete() throws Exception{ Connection connection =
	 * new Connection(); connection.setName("HbaseConn1"); ConnectionProperty
	 * connectionProperty = new ConnectionProperty();
	 * connectionProperty.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
	 * connectionProperty.setValue("h1:2181,h2:8080");
	 * connection.addProperty(connectionProperty);
	 * 
	 * KeyedPoolableObjectFactory factory =
	 * PoolUtils.synchronizedPoolableFactory(new HBaseConnectionPoolFactory());
	 * HBaseConnectionPool pool = new HBaseConnectionPool(factory);
	 * pool.save(connection);
	 * 
	 * pool.delete(connection); Client client = (Client)
	 * pool.borrowObject(connection); Cluster cluster = client.getCluster();
	 * logger.debug("Cluster is empty? : " + cluster.isEmpty());
	 * assertEquals(cluster.isEmpty(), true); }
	 */
}
