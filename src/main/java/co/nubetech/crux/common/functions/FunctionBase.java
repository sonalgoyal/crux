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

import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public abstract class FunctionBase {

	private final static Logger logger = Logger.getLogger(FunctionBase.class);

	protected Map<String, String> properties;

	public FunctionBase() {
		// this.properties = new HashMap<String, String>();
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	public String getProperty(String key) {
		return properties.get(key);
	}

	public abstract Object execute(byte[] value) throws CruxException;

}
