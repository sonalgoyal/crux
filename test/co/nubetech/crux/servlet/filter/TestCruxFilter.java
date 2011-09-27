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
package co.nubetech.crux.servlet.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Statement;
import java.util.List;

import org.apache.hadoop.hbase.rest.client.Client;
import org.apache.hadoop.hbase.rest.client.Cluster;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.DBConnection;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.util.CruxConstants;

public class TestCruxFilter extends DBConnection {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.servlet.filter.TestCruxFilter.class);

	@Test
	public void testInit() throws Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'hbase.rest','h1:8080,h2:8080')");

		MockFilterConfig filterConfig = new MockFilterConfig();

		CruxFilter cruxFilter = new CruxFilter();
		cruxFilter.init(filterConfig);
		HBaseConnectionPool pool = (HBaseConnectionPool) filterConfig
				.getServletContext().getAttribute(CruxConstants.HBASE_POOL);
		logger.debug("Pool is: " + pool);

	/*	ConnectionDAO connectionDAO = new ConnectionDAO();
		connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		List<Connection> connectionList = connectionDAO.findAll();
		int poolCount = 0;
		for (Connection connection : connectionList) {
			Client client = (Client) pool.borrowObject(connection);
			Cluster cluster = client.getCluster();
			logger.debug("Cluster is empty? : " + cluster.isEmpty());
			assertEquals(cluster.isEmpty(), false);
			poolCount++;
		}

		logger.debug("poolCount is: " + poolCount);
		assertFalse(poolCount == 0);*/

		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

	}

}
