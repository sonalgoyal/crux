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

import org.apache.hadoop.hbase.util.Bytes;

import co.nubetech.crux.util.CruxException;

public class LowerCase extends FunctionBase implements CruxNonAggregator{

	@Override
	public Object execute(Object value) throws CruxException {
		if (value instanceof byte[]) {
			String valueInString = Bytes.toString((byte[])value);
			if (valueInString != null) {
				valueInString = valueInString.toLowerCase();
			}
			return valueInString;
		}
		else {
			String valueInString = (String) value;
			if (valueInString != null) {
				valueInString = valueInString.toLowerCase();
			}
			return valueInString;
		}
	}

	
	@Override
	public boolean isAggregate() {
		return false;
	}
}
