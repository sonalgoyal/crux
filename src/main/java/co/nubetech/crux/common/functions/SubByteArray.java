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

public class SubByteArray extends FunctionBase {

	private final static Logger logger = Logger.getLogger(Conversion.class);

	public String OFFSET_PROPERTY = "offset";
	public String LENGTH_PROPERTY = "length";

	public SubByteArray(Map<String, String> properties) {
		super();
		this.properties = properties;
	}

	@Override
	public byte[] execute(byte[] value) throws CruxException {
		String offsetInString = getProperty(OFFSET_PROPERTY);
		String lengthInString = getProperty(LENGTH_PROPERTY);
		byte[] result = null;
		int offset = 0;
		int length = 0;
		try {
			offset = Integer.parseInt(offsetInString);
			length = Integer.parseInt(lengthInString);
			result = new byte[length];
			Bytes.putBytes(result, 0, value, offset, length);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CruxException(e.getMessage());
		}
		return result;
	}
}
