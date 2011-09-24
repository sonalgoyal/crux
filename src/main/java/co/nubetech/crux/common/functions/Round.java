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
package co.nubetech.crux.common.functions;

import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public class Round extends FunctionBase {

	private final static Logger logger = Logger.getLogger(Round.class);

	public String VALUE_TYPE_PROPERTY = "valueType";

	public Round(Map<String, String> properties) {
		super();
		this.properties = properties;
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		String valueInString = Bytes.toString(value);
		if (valueInString == null) {
			throw new CruxException("");
		}
		String valueType = getProperty(VALUE_TYPE_PROPERTY);
		try {
			if (valueType.equals("Double")) {
				Double valueInDouble = Double.parseDouble(valueInString);
				return Math.round(valueInDouble);
			} else if (valueType.equals("Float")) {
				Float valueInFloat = Float.parseFloat(valueInString);
				return Math.round(valueInFloat);
			} else {
				throw new CruxException("The type" + valueType
						+ "is not supported.");
			}
		} catch (Exception e) {
			throw new CruxException(e.getMessage());
		}
	}
}
