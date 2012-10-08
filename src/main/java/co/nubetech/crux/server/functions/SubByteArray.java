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

public class SubByteArray extends FunctionBase implements CruxNonAggregator{

	private final static Logger logger = Logger.getLogger(Conversion.class);

	public String OFFSET_PROPERTY = "offset";
	public String LENGTH_PROPERTY = "length";
	private int offset;
	private int length;

	public SubByteArray(Map<String, String> properties) {
		super();
		this.properties = properties;
		String offsetInString = getProperty(OFFSET_PROPERTY);
		String lengthInString = getProperty(LENGTH_PROPERTY);
		offset = Integer.parseInt(offsetInString);
		length = Integer.parseInt(lengthInString);	
	}

	@Override
	public byte[] execute(byte[] value) throws CruxException {
		byte[] result = null;
		try {
			if(length != 0){
			result = new byte[length];
			Bytes.putBytes(result, 0, value, offset, length);
			} else {
				result = new byte[value.length];
				Bytes.putBytes(result, 0, value, offset, value.length);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CruxException(e.getMessage());
		}
		return result;
	}
}
