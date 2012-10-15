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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class TestSubSubstringComparater {
	@Test
	public void testCompareToWithEqualLength() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, 6);
		int result = subSubstringComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(0, result);
	}

	@Test
	public void testCompareToWithZeroLength() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, 0);
		int result = subSubstringComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(1, result);
	}

	@Test
	public void testCompareToWithLessLength() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, 3);
		int result = subSubstringComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(1, result);
	}

	@Test
	public void testCompareToWithGreaterLength() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, 9);
		int result = subSubstringComparator.compareTo(Bytes
				.toBytes("abcdefghi"));
		assertEquals(0, result);
	}

	@Test(expected = java.lang.NullPointerException.class)
	public void testCompareToWithNullValue() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				null, 0, 9);
		int result = subSubstringComparator.compareTo(Bytes
				.toBytes("abcdefghi"));
		assertEquals(0, result);
	}

	@Test
	public void testCompareToWithNullValueToCompare() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, 9);
		int result = subSubstringComparator.compareTo(null);
		assertEquals(-1, result);
	}
	
	@Test
	public void testCompareToLengthNeg() {
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(
				"abcdef", 0, -1);
		int result = subSubstringComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(0, result);
	}
	
	@Test
	public void testReadFields() throws IOException{		
		String value = "abcdef";
		int offset = 0;
		int length = 6;
		
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(value, offset, length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));
		Bytes.writeByteArray(dos, Bytes.toBytes(value));
		dos.writeInt(offset);
		dos.writeInt(length);
		//dos.writeUTF(value);	
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subSubstringComparator.readFields(dis);
		assertEquals(offset, subSubstringComparator.getOffset());
		assertEquals(length, subSubstringComparator.getLength());
		assertEquals(value, Bytes.toString(subSubstringComparator.getValue()));
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		String value = "abcdef";
		int offset = 0;
		int length = 6;
		
		SubSubstringComparator subSubstringComparator = new SubSubstringComparator(value, offset, length);		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));		
		subSubstringComparator.write(dos);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subSubstringComparator.readFields(dis);
		assertEquals(offset, subSubstringComparator.getOffset());
		assertEquals(length, subSubstringComparator.getLength());
		assertEquals(value, Bytes.toString(subSubstringComparator.getValue()));
	}

}
