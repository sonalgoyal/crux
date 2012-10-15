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

public class TestSubBinarySuffixComparator {

	@Test
	public void testCompareToWithEqualLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0,6);
		int result1 = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(0, result1);
	}
	
	@Test
	public void testCompareToWithZeroLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, 0);
		int result = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(6, result);
	}
	
	@Test
	public void testCompareToWithLessLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, 3);
		int result = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(3, result);
	}
	
	@Test
	public void testCompareToWithGreaterLength() {
		byte[] value = Bytes.toBytes("defghi");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, 9);
		int result = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdefghijh"));
		assertEquals(0, result);
	}
	
	@Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
	public void testCompareToWithInCorrectLength() {
		byte[] value = Bytes.toBytes("abcdef");
		byte[] valueToCompare = Bytes.toBytes("abc");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, valueToCompare.length + 1);
		int result = subBinarySuffixComparator.compareTo(valueToCompare);
		assertEquals(0, result);
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void testCompareToWithNullValue() {
		byte[] valueToCompare = Bytes.toBytes("abc");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				null, 0, valueToCompare.length);
		int result = subBinarySuffixComparator.compareTo(valueToCompare);
		assertEquals(0, result);
	}

	@Test
	public void testCompareToWithNullValueToCompare() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, value.length);
		int result = subBinarySuffixComparator.compareTo(null);
		assertEquals(-1, result);
	}
	
	@Test
	public void testReadFields() throws IOException{		
		byte[] value = Bytes.toBytes("abcdef");
		int offset = 0;
		int length = value.length;
		
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(value, offset, value.length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));
		Bytes.writeByteArray(dos, value);		
		dos.writeInt(offset);
		dos.writeInt(length);
			
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subBinarySuffixComparator.readFields(dis);
		assertEquals(offset, subBinarySuffixComparator.getOffset());
		assertEquals(length, subBinarySuffixComparator.getLength());
		assertTrue(Bytes.equals(value, subBinarySuffixComparator.getValue()));
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		byte[] value = Bytes.toBytes("abcdef");
		int offset = 0;
		int length = value.length;
		
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(value, offset, value.length);		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));		
		subBinarySuffixComparator.write(dos);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subBinarySuffixComparator.readFields(dis);
		assertEquals(offset, subBinarySuffixComparator.getOffset());
		assertEquals(length, subBinarySuffixComparator.getLength());
		assertTrue(Bytes.equals(value, subBinarySuffixComparator.getValue()));
	}

	@Test
	public void testCompareToWithLengthNeg() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0,-1);
		int result1 = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("xabcdef"));
		assertEquals(0, result1);
	}
	
	@Test
	public void testCompareToWithNegativeOneLength() {
		byte[] value = Bytes.toBytes("abcdef");
		SubBinarySuffixComparator subBinarySuffixComparator = new SubBinarySuffixComparator(
				value, 0, -1);
		int result = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdef"));
		assertEquals(0, result);
		int result1 = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcdefgh"));
		assertEquals(-2, result1);
		int result2 = subBinarySuffixComparator.compareTo(Bytes
				.toBytes("abcd"));
		assertEquals(2, result2);
	}


}
