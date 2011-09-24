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

import static org.junit.Assert.assertEquals;

import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.junit.Test;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.util.CruxConstants;

public class TestHBaseConnectionPoolFactory {

	@Test
	public void testGetCluster() {
		Connection connection = new Connection();

		ConnectionProperty restProperty = new ConnectionProperty();
		restProperty.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		restProperty.setValue("h1:8080,h2:8080");
		connection.addProperty(restProperty);

		HBaseConnectionPoolFactory hbaseConnectionPoolFactory = new HBaseConnectionPoolFactory();
		Cluster cluster = hbaseConnectionPoolFactory.getCluster(connection);
		assertEquals(cluster.isEmpty(), false); // Cluster is not empty.

		cluster.remove("h1", 8080);
		assertEquals(cluster.isEmpty(), false); // After removing one node
												// cluster is not empty.

		cluster.remove("h2", 8080);
		assertEquals(cluster.isEmpty(), true); // After removing second cluster
												// is empty.

	}

	@Test
	public void testMakeObject() {

		Connection connection = new Connection();

		ConnectionProperty restProperty = new ConnectionProperty();
		restProperty.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		restProperty.setValue("h1:8080,h2:8080");
		connection.addProperty(restProperty);

		HBaseConnectionPoolFactory hbaseConnectionPoolFactory = new HBaseConnectionPoolFactory();
		Client client = (Client) hbaseConnectionPoolFactory
				.makeObject(connection);
		Cluster cluster = client.getCluster();
		assertEquals(cluster.isEmpty(), false); // Cluster is not empty.

		cluster.remove("h1", 8080);
		assertEquals(cluster.isEmpty(), false); // After removing one node
												// cluster is not empty.

		cluster.remove("h2", 8080);
		assertEquals(cluster.isEmpty(), true); // After removing second cluster
												// is empty.
	}

}
