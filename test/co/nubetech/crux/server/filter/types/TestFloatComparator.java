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

import co.nubetech.crux.server.filter.types.FloatComparator;

public class TestFloatComparator {

	@Test
	public void testCompareToEqual() {
		byte[] value = Bytes.toBytes(100.0f);
		FloatComparator comparator = new FloatComparator(value, 0, value.length);
		assertEquals(0, comparator.compareTo(Bytes.toBytes(100.0f)));
	}

	@Test
	public void testCompareToWithNull() {
		byte[] value = Bytes.toBytes(100f);
		FloatComparator comparator = new FloatComparator(value, 0, value.length);
		assertEquals(-1, comparator.compareTo(null));
	}

	@Test
	public void testCompareToWithDiferentLengthStart() {
		byte[] value = Bytes.toBytes(100f);
		byte[] value1 = Bytes.add(value, Bytes.toBytes(200f));
		FloatComparator comparator = new FloatComparator(value, 0, value.length);
		assertEquals(0, comparator.compareTo(value1));
	}

	@Test
	public void testCompareToWithDiferentLengthEnd() {
		byte[] value = Bytes.toBytes("Test");
		byte[] value1 = Bytes.add(value, Bytes.toBytes(200f));
		FloatComparator comparator = new FloatComparator(Bytes.toBytes(200f),
				value.length, 4);
		assertEquals(0, comparator.compareTo(value1));
	}

	@Test
	public void testCompareToWithDifferentLengthMiddle() {
		byte[] value = Bytes.toBytes("Test");
		byte[] value1 = Bytes.add(value, Bytes.toBytes(200f));
		byte[] value2 = Bytes.add(value1, Bytes.toBytes("TestString"));
		FloatComparator comparator = new FloatComparator(Bytes.toBytes(200f),
				value.length, 4);
		assertEquals(0, comparator.compareTo(value2));
	}

	@Test
	public void testCompareLessToWithDifferentLengthMiddle() {
		byte[] value = Bytes.toBytes("Test");
		byte[] value1 = Bytes.add(value, Bytes.toBytes(200f));
		byte[] value2 = Bytes.add(value1, Bytes.toBytes("TestString"));
		FloatComparator comparator = new FloatComparator(Bytes.toBytes(100f),
				value.length, 4);
		assertEquals(-1, comparator.compareTo(value2));
	}

	@Test
	public void testCompareGreaterWithDifferentLengthMiddle() {
		byte[] value = Bytes.toBytes("Test");
		byte[] value1 = Bytes.add(value, Bytes.toBytes(200f));
		byte[] value2 = Bytes.add(value1, Bytes.toBytes("TestString"));
		FloatComparator comparator = new FloatComparator(Bytes.toBytes(500f),
				value.length, 4);
		assertEquals(1, comparator.compareTo(value2));
	}
	
	@Test
	public void testCompareToEqualWithLengthNotGiven() {
		byte[] value = Bytes.toBytes(100.2f);
		FloatComparator comparator = new FloatComparator(value, 0,
				-1);
		assertEquals(0, comparator.compareTo(Bytes.toBytes(100.2f)));
	}
	
	@Test
	public void testReadFields() throws IOException{		
		byte[] value = Bytes.toBytes(100.2f);
		int offset = 0;
		int length = value.length;
		
		FloatComparator comparator = new FloatComparator(value, offset, value.length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));
		Bytes.writeByteArray(dos, value);		
		dos.writeInt(offset);
		dos.writeInt(length);
		//dos.writeFloat(Bytes.toFloat(value));	
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		comparator.readFields(dis);
		assertEquals(offset, comparator.getOffset());
		assertEquals(length, comparator.getLength());
		assertTrue(new Float(Bytes.toFloat(value)).equals(new Float(comparator.getFloatValue())));
		assertTrue(Bytes.equals(value,comparator.getValue()));
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		byte[] value = Bytes.toBytes(100.2f);
		int offset = 0;
		int length = value.length;
		
		FloatComparator comparator = new FloatComparator(value, offset, value.length);		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));		
		comparator.write(dos);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		comparator.readFields(dis);
		assertEquals(offset, comparator.getOffset());
		assertEquals(length, comparator.getLength());
		assertTrue(Bytes.equals(value,comparator.getValue()));
		assertTrue(new Float(Bytes.toFloat(value)).equals(new Float(comparator.getFloatValue())));
	}

}
