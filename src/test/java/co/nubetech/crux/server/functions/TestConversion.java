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

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.server.functions.Conversion;
import co.nubetech.crux.util.CruxException;

public class TestConversion {

	@Test
	public void testExecuteWithLongValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.Long");
		Conversion conversion = new Conversion(properties);
	
		long result = (Long) conversion.execute(Bytes.toBytes(12l));
		assertEquals(12l, result);
	}

	@Test
	public void testExecuteWithStringValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.String");
		Conversion conversion = new Conversion(properties);
		String result = (String) conversion.execute(Bytes.toBytes("ABc"));
		assertEquals("ABc", result);
	}

	@Test
	public void testExecuteWithIntValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.Integer");
		Conversion conversion = new Conversion(properties);
		int result = (Integer) conversion.execute(Bytes.toBytes(12));
		assertEquals(12, result);
	}

	@Test
	public void testExecuteWithBooleanValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.Boolean");
		Conversion conversion = new Conversion(properties);
		boolean result = (Boolean) conversion.execute(Bytes.toBytes(true));
		assertEquals(true, result);
	}

	@Test
	public void testExecuteWithFloatValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.Float");
		Conversion conversion = new Conversion(properties);
		float result = (Float) conversion.execute(Bytes.toBytes(12.8f));
		assertEquals(12.8, result, .1);
	}

	@Test
	public void testExecuteWithDoubleValues() throws CruxException {
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.put("class.name", "java.lang.Double");
		Conversion conversion = new Conversion(properties);
		double result = (Double) conversion.execute(Bytes.toBytes(12.8));
		assertEquals(12.8, result, 0);
	}

	@Test(expected = CruxException.class)
	public void testExecuteWithWrongValues() throws CruxException {
		Conversion conversion = new Conversion(new HashMap<String, String>());
		conversion.setProperty(conversion.CLASS_NAME_PROPERTY,
				"java.lang.Double");
		double result = (Double) conversion.execute(Bytes.toBytes("as"));
		assertEquals(12.8, result, 0);
	}
}
