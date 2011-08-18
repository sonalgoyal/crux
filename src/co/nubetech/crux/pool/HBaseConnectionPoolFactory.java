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

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.Utility;

public class HBaseConnectionPoolFactory implements KeyedPoolableObjectFactory {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.pool.HBaseConnectionPoolFactory.class);

	@Override
	public void activateObject(Object arg0, Object arg1) throws Exception {

	}

	@Override
	public void destroyObject(Object key, Object obj) throws Exception {
		// Not sure it works or not.

		HTablePool hTablePool = (HTablePool) key;
	}

	@Override
	public Object makeObject(Object obj) {
		Connection connection = (Connection) obj;
		Configuration conf = Utility.getConfiguration(connection);
		HTablePool hTablePool = new HTablePool(conf,
				CruxConstants.HTABLE_POOL_MAX_SIZE);
		return hTablePool;
	}

	@Override
	public void passivateObject(Object arg0, Object arg1) throws Exception {

	}

	@Override
	public boolean validateObject(Object arg0, Object arg1) {
		return true;
	}

}
