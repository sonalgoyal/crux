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
package co.nubetech.crux.server;

import org.apache.hadoop.hbase.util.Bytes;

import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxException;

public class BytesHelper {

	public static byte[] getBytes(String valueTypeClassName, String value)
			throws CruxException {
		byte[] valueBytes = null;
		if (valueTypeClassName.equals("java.lang.String")) {
			valueBytes = Bytes.toBytes(value);
		} else if (valueTypeClassName.equals("java.lang.Integer")) {
			int valueInt = Integer.parseInt(value);
			valueBytes = Bytes.toBytes(valueInt);
		} else if (valueTypeClassName.equals("java.lang.Float")) {
			float valueFl = Float.parseFloat(value);
			valueBytes = Bytes.toBytes(valueFl);
		} else if (valueTypeClassName.equals("java.lang.Double")) {
			double valueDbl = Double.parseDouble(value);
			valueBytes = Bytes.toBytes(valueDbl);
		} else if (valueTypeClassName.equals("java.lang.Boolean")) {
			boolean valueBool = Boolean.parseBoolean(value);
			valueBytes = Bytes.toBytes(valueBool);
		} else if (valueTypeClassName.equals("java.lang.Long")) {
			long valueLong = Long.parseLong(value);
			valueBytes = Bytes.toBytes(valueLong);
		} else if (valueTypeClassName.equals("java.lang.Short")) {
			short valueShort = Short.parseShort(value);
			valueBytes = Bytes.toBytes(valueShort);
		} else {
			throw new CruxException("The type " + valueTypeClassName
					+ " is not supported");
		}
		return valueBytes;
	}

	// computes bytes for the value sent and appends it to the sentBytes.
	// If sentBytes is null, return the byte[] of the value
	public static byte[] addToByteArray(ValueType valueType, String value,
			byte[] sentBytes) throws CruxException {
		String valueTypeClassName = valueType.getClassName();
		byte[] valueBytes = null;
		if (value != null) {
			try {
				valueBytes = getBytes(valueTypeClassName, value);
			} catch (Exception e) {
				throw new CruxException(e);
			}
		}// value null
		if (sentBytes != null) {
			if (valueBytes != null) {
				sentBytes = Bytes.add(sentBytes, valueBytes);
			}
		} else {
			sentBytes = valueBytes;
		}
		return sentBytes;
	}

}
