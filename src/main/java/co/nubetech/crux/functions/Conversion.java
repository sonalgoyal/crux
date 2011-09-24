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
package co.nubetech.crux.functions;

import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public class Conversion extends FunctionBase {

	private final static Logger logger = Logger.getLogger(Conversion.class);

	public String CLASS_NAME_PROPERTY = "class.name";
	String className ;

	public Conversion() {

	}

	public Conversion(Map<String, String> properties) {
		super();
		this.properties = properties;
		className = getProperty(CLASS_NAME_PROPERTY);
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		try {
			if (className.equals("java.lang.String")) {
				return Bytes.toString(value);
			} else if (className.equals("java.lang.Boolean")) {
				return Bytes.toBoolean(value);
			} else if (className.equals("java.lang.Integer")) {
				return Bytes.toInt(value);
			} else if (className.equals("java.lang.Long")) {
				return Bytes.toLong(value);
			} else if (className.equals("java.lang.Float")) {
				return Bytes.toFloat(value);
			} else if (className.equals("java.lang.Double")) {
				return Bytes.toDouble(value);
			} else if (className.equals("java.lang.Short")) {
				return Bytes.toShort(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CruxException(e.getMessage());
		}
		return null;
	}

}
