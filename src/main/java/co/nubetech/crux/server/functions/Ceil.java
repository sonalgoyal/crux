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
package co.nubetech.crux.server.functions;

import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public class Ceil extends FunctionBase implements CruxNonAggregator{

	private final static Logger logger = Logger.getLogger(Ceil.class);
	String valueType;
	
	public Ceil() {
		
	}
	
	public Ceil(Map<String, String> properties) {
		super();
		this.properties = properties;
		valueType = getProperty(VALUE_TYPE_PROPERTY);
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		String valueInString = Bytes.toString(value);

		if (valueInString == null) {
			throw new CruxException("");
		}
		try {
			if (valueType.equals("Double")) {
				Double valueInDouble = Double.parseDouble(valueInString);
				return Math.ceil(valueInDouble);
			} else {
				throw new CruxException("The type" + valueType
						+ "is not supported.");
			}
		} catch (Exception e) {
			throw new CruxException(e.getMessage());
		}
	}
}
