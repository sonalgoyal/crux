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

import java.util.List;

import javax.servlet.FilterConfig;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.PoolUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.pool.HBaseConnectionPoolFactory;
import co.nubetech.crux.util.CruxConstants;

public class CruxFilter extends StrutsPrepareAndExecuteFilter {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.servlet.filter.CruxFilter.class);

	@Override
	public void init(FilterConfig filter) {
		try {
			super.init(filter);
			ConnectionDAO connectionDAO = new ConnectionDAO();
			connectionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
					.getNewSession();
			List<Connection> connectionList = connectionDAO.findAll();
			logger.debug("connectionList size is: " + connectionList.size());
			KeyedPoolableObjectFactory factory = PoolUtils
					.synchronizedPoolableFactory(new HBaseConnectionPoolFactory());
			HBaseConnectionPool pool = new HBaseConnectionPool(factory);
			for (Connection connection : connectionList) {
				logger.debug("Connection is: " + connection);
				pool.addObject(connection);
			}
			filter.getServletContext().setAttribute(CruxConstants.HBASE_POOL,
					pool);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
