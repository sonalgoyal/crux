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
package co.nubetech.crux.server.filter.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.server.filter.types.SubBinaryPrefixComparator;

public class TestSubBinaryPrefixComparator {

	@Test
	public void testCompareToWithEqualLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, value.length);
		int result1 = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdefg"));
		int result2 = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(0, result1);
		assertEquals(0, result2);
	}

	@Test
	public void testCompareToWithZeroLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, 0);
		int result = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(6, result);
	}

	@Test
	public void testCompareToWithLessLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, 3);
		int result = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(3, result);
	}

	@Test
	public void testCompareToWithGreaterLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, 9);
		int result = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdefghi"));
		assertEquals(0, result);
	}

	@Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
	public void testCompareToWithInCorrectLength() {
		byte[] value = Bytes.toBytes("abcdef");
		byte[] valueToCompare = Bytes.toBytes("abc");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, valueToCompare.length + 1);
		int result = subBinaryPrefixComparator.compareTo(valueToCompare);
		assertEquals(0, result);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testCompareToWithNullValue() {
		byte[] valueToCompare = Bytes.toBytes("abc");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				null, 0, valueToCompare.length);
		int result = subBinaryPrefixComparator.compareTo(valueToCompare);
		assertEquals(0, result);
	}

	@Test
	public void testCompareToWithNullValueToCompare() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, value.length);
		int result = subBinaryPrefixComparator.compareTo(null);
		assertEquals(-1, result);
	}
	
	@Test
	public void testReadFields() throws IOException{		
		byte[] value = Bytes.toBytes("abcdef");
		int offset = 0;
		int length = value.length;
		
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(value, offset, value.length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));
		Bytes.writeByteArray(dos, value);
		dos.writeInt(offset);
		dos.writeInt(length);
					
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subBinaryPrefixComparator.readFields(dis);
		assertEquals(offset, subBinaryPrefixComparator.getOffset());
		assertEquals(length, subBinaryPrefixComparator.getLength());
		assertTrue(Bytes.equals(value, subBinaryPrefixComparator.getValue()));
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		byte[] value = Bytes.toBytes("abcdef");
		int offset = 0;
		int length = value.length;
		
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(value, offset, value.length);		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));		
		subBinaryPrefixComparator.write(dos);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subBinaryPrefixComparator.readFields(dis);
		assertEquals(offset, subBinaryPrefixComparator.getOffset());
		assertEquals(length, subBinaryPrefixComparator.getLength());
		assertTrue(Bytes.equals(value, subBinaryPrefixComparator.getValue()));
	}

	@Test
	public void testCompareToWithLengthNeg() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, -1);
		int result1 = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdefg"));
		assertEquals(0, result1);
	}
	
	@Test
	public void testCompareToWithNegativeOneLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinaryPrefixComparator subBinaryPrefixComparator = new SubBinaryPrefixComparator(
				value, 0, -1);
		int result = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(0, result);
		int result1 = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcdefgh"));
		assertEquals(0, result1);
		int result2 = subBinaryPrefixComparator.compareTo(Bytes
				.toBytes("abcd"));
		assertEquals(2, result2);
	}

}
