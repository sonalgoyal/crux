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
package co.nubetech.crux.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Connection;

public class Utility {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.util.Utility.class);

	// Only passes the first pair of host:port.
	public static Configuration getConfiguration(Connection conn) {
		Configuration conf = new Configuration();
		String propertyValue = conn.getProperties()
				.get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue();
		String[] propertyValueArray = propertyValue.split(",");
		logger.debug("propertyValueArray.length is: "
				+ propertyValueArray.length);
		for (int i = 0; i < 1; i++) {
			String[] hbaseServerHostAndPort = propertyValueArray[i].split(":");
			String host = hbaseServerHostAndPort[0];
			String port = hbaseServerHostAndPort[1];

			logger.debug("Host is: " + host);
			logger.debug("Port is: " + port);
			conf.set("hbase.zookeeper.quorum", host);
			conf.set("hbase.zookeeper.property.clientPort", port);
		}
		logger.debug("Returning Configuration: " + conf);
		return conf;
	}

}
