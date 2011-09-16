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

public class SubString extends FunctionBase {

	private final static Logger logger = Logger.getLogger(SubString.class);

	public String OFFSET_PROPERTY = "offset";
	public String LENGTH_PROPERTY = "length";
	int offset = 0;
	int length = 0;

	public SubString(Map<String, String> properties) throws CruxException {
		super();
		try {
		this.properties = properties;
		String offsetInString = getProperty(OFFSET_PROPERTY);
		String lengthInString = getProperty(LENGTH_PROPERTY);
		offset = Integer.parseInt(offsetInString);
		length = Integer.parseInt(lengthInString);
		} catch (Exception e){
			throw new CruxException(e.getMessage(),e);
		}
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		
		String subString = Bytes.toString(value, offset, length);
		return subString;
	}

}
