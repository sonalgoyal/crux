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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.coprocessor.CoprocessorHost;
import org.junit.Test;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.Utility;

public class TestHBaseConnectionPoolFactory {

	// Can't test for multiple machines.
	@Test
	public void testGetConfiguration() {
		Connection connection = new Connection();

		ConnectionProperty property = new ConnectionProperty();
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("h1:2181");
		connection.addProperty(property);

		HBaseConnectionPoolFactory hbaseConnectionPoolFactory = new HBaseConnectionPoolFactory();
		Utility utility = new Utility();
		Configuration conf = utility.getConfiguration(connection);
		assertEquals(conf.get("hbase.zookeeper.quorum"), "h1");
		assertEquals(conf.get("hbase.zookeeper.property.clientPort"), "2181");
		assertEquals(conf.get(CoprocessorHost.REGION_COPROCESSOR_CONF_KEY), 
				"co.nubetech.crux.server.aggregate.GroupingAggregationImpl");
	}

	// InComplete
	@Test
	public void testMakeObject() {

		Connection connection = new Connection();

		ConnectionProperty property = new ConnectionProperty();
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("h1:2181");
		connection.addProperty(property);

		HBaseConnectionPoolFactory hbaseConnectionPoolFactory = new HBaseConnectionPoolFactory();
		HTablePool htablePool = (HTablePool) hbaseConnectionPoolFactory
				.makeObject(connection);
	}

}
