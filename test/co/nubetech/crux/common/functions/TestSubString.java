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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import co.nubetech.crux.functions.SubString;
import co.nubetech.crux.util.CruxException;

public class TestSubString {

	private final static Logger logger = Logger.getLogger(TestSubString.class);

	@Test
	public void testExecuteWithCorrectValues() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("offset", "0");
		properties.put("length", "4");
		SubString substring = new SubString(properties);
		String result = (String) substring.execute("TestString".getBytes());
		assertEquals(result, "Test");
	}

	@Test
	public void testExecuteWithNullValues() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("offset", "0");
		properties.put("length", "3");
		SubString substring = new SubString(properties);
		String result = (String) substring.execute(null);
		assertEquals(result, null);
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithWrongOffset() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("offset", "a");
		properties.put("length", "3");
		SubString substring = new SubString(properties);
		String result = (String) substring.execute("TestString".getBytes());
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithWrongLength() throws CruxException {
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("offset", "0");
		properties.put("length", "b");
		SubString substring = new SubString(properties);
		String result = (String) substring.execute("TestString".getBytes());
	}

}
