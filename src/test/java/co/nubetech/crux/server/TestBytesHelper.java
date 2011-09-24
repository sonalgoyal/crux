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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxException;

public class TestBytesHelper {

	@Test
	public void testAddToByteArrayStringToNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		String value = "value";
		byte[] bytes = BytesHelper.addToByteArray(valueType, value, null);
		assertEquals(0, Bytes.compareTo(value.getBytes(), bytes));
	}

	@Test
	public void testAddToByteArrayNullToNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		String value = null;
		byte[] bytes = BytesHelper.addToByteArray(valueType, value, null);
		assertNull(bytes);
	}

	@Test
	public void testAddToByteArrayNullToNotNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		String value = null;
		byte[] bytes = BytesHelper.addToByteArray(valueType, value,
				Bytes.toBytes(true));
		assertArrayEquals(Bytes.toBytes(true), bytes);
	}

	@Test
	public void testAddToByteArrayLongToNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		Long value = 10l;
		byte[] bytes = BytesHelper.addToByteArray(valueType, "10", null);
		assertEquals(0, Bytes.compareTo(Bytes.toBytes(10l), bytes));
	}

	@Test
	public void testAddToByteArrayLongToString() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		byte[] bytes = BytesHelper.addToByteArray(valueType, "10",
				Bytes.toBytes("I am a string"));
		byte[] expected = Bytes.add(Bytes.toBytes("I am a string"),
				Bytes.toBytes(10l));
		assertArrayEquals(expected, bytes);
	}

	@Test
	public void testAddToByteArrayLongToIntToString() throws CruxException {
		ValueType valueType = new ValueType();
		// int to string
		valueType.setClassName("java.lang.Integer");

		byte[] bytes = BytesHelper.addToByteArray(valueType, "10",
				Bytes.toBytes("I am a string"));
		byte[] expected = Bytes.add(Bytes.toBytes("I am a string"),
				Bytes.toBytes(10));
		assertArrayEquals(expected, bytes);

		// long to above
		valueType.setClassName("java.lang.Long");
		expected = Bytes.add(bytes, Bytes.toBytes(100l));
		bytes = BytesHelper.addToByteArray(valueType, "100", bytes);
		assertArrayEquals(expected, bytes);
	}

	@Test(expected = CruxException.class)
	public void testNotSupportedType() throws CruxException {
		ValueType type = new ValueType();
		type.setClassName("co.nubetech.crux.util.CruxException");

		BytesHelper.addToByteArray(type, "value", null);
	}

	@Test
	public void testGetBytesForBooleanType() throws CruxException {
		assertEquals(true, Bytes.toBoolean(BytesHelper.getBytes(
				"java.lang.Boolean", "true")));
	}

	@Test
	public void testGetBytesForShortType() throws CruxException {
		assertEquals((short) 200,
				Bytes.toShort(BytesHelper.getBytes("java.lang.Short", "200")));
	}

	@Test
	public void testAddToByteArrayBooleanToNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");
		byte[] bytes = BytesHelper.addToByteArray(valueType, "true", null);
		assertEquals(0, Bytes.compareTo(Bytes.toBytes(true), bytes));
	}

	@Test
	public void testAddToByteArrayShortToNull() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");
		byte[] bytes = BytesHelper.addToByteArray(valueType, "200", null);
		assertEquals(0, Bytes.compareTo(Bytes.toBytes((short) 200), bytes));
	}

	@Test
	public void testAddToByteArrayBooleanToString() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		byte[] bytes = BytesHelper.addToByteArray(valueType, "true",
				Bytes.toBytes("I am a string"));
		byte[] expected = Bytes.add(Bytes.toBytes("I am a string"),
				Bytes.toBytes(true));
		assertArrayEquals(expected, bytes);
	}

	@Test
	public void testAddToByteArrayShortToString() throws CruxException {
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		byte[] bytes = BytesHelper.addToByteArray(valueType, "10",
				Bytes.toBytes("I am a string"));
		byte[] expected = Bytes.add(Bytes.toBytes("I am a string"),
				Bytes.toBytes((short) 10));
		assertArrayEquals(expected, bytes);
	}

}
