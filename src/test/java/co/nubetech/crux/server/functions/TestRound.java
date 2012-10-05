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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import co.nubetech.crux.server.functions.Round;
import co.nubetech.crux.util.CruxException;

public class TestRound {

	@Test
	public void testExecuteWithCorrectValuesOfTypeDouble() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("valueType", "Double");
		Round round = new Round(properties);
		Long result = (Long) round.execute("1.235".getBytes());
		assertEquals(result.longValue(), 1);
	}

	@Test
	public void testExecuteWithCorrectValuesOfTypeFloat() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("valueType", "Float");
		Round round = new Round(properties);
		Integer result = (Integer) round.execute("1.51f".getBytes());
		assertEquals(result.intValue(), 2);
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithInCorrectType() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		Round round = new Round(properties);
		round.setProperty(round.VALUE_TYPE_PROPERTY, "String");
		Integer result = (Integer) round.execute("1.51f".getBytes());
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithNullValue() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		Round round = new Round(properties);
		round.setProperty(round.VALUE_TYPE_PROPERTY, "Float");
		Integer result = (Integer) round.execute(null);
	}

}
