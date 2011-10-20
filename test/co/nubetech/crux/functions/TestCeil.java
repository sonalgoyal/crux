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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import co.nubetech.crux.functions.Ceil;
import co.nubetech.crux.util.CruxException;

public class TestCeil {

	@Test
	public void testExecuteWithCorrectValues() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("valueType", "Double");
		Ceil ceil = new Ceil(properties);
		Double result = (Double) ceil.execute("1.235".getBytes());
		assertEquals(result, new Double(2.0));
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithInCorrectType() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		Ceil ceil = new Ceil(properties);
		ceil.setProperty(ceil.VALUE_TYPE_PROPERTY, "String");
		ceil.execute("1.51".getBytes());
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithNullValue() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		Ceil ceil = new Ceil(properties);
		ceil.setProperty(ceil.VALUE_TYPE_PROPERTY, "Double");
		ceil.execute(null);
	}

}
