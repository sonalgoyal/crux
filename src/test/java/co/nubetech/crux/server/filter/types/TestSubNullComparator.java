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

public class TestSubNullComparator {

	@Test
	public void testCompareToWithZeroLength() {
		SubNullComparator subNullComparator = new SubNullComparator(0, 0);
		int result = subNullComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(0, result);
	}

	@Test
	public void testCompareToWithNonZeroLength() {
		SubNullComparator subNullComparator = new SubNullComparator(0, 1);
		int result = subNullComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(1, result);
	}
	
	@Test
	public void testCompareToWithLengthNeg() {
		SubNullComparator subNullComparator = new SubNullComparator(0, -1);
		int result = subNullComparator.compareTo(Bytes.toBytes("abcdefg"));
		assertEquals(1, result);
	}

	@Test
	public void testCompareToWithNullValueToCompare() {
		SubNullComparator subNullComparator = new SubNullComparator(0, 1);
		int result = subNullComparator.compareTo(null);
		assertEquals(0, result);
	}
	
	@Test
	public void testReadFields() throws IOException{		
		int offset = 1;
		int length = 3;
		
		SubNullComparator subNullComparator = new SubNullComparator(offset, length);
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));	
		Bytes.writeByteArray(dos, null);
		dos.writeInt(offset);
		dos.writeInt(length);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subNullComparator.readFields(dis);
		System.out.print("print:"+subNullComparator.getValue());
		assertEquals(offset, subNullComparator.getOffset());
		assertEquals(length, subNullComparator.getLength());
		//assertNull(subNullComparator.getValue());
		
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		int offset = 1;
		int length = 3;
		
		SubNullComparator subNullComparator = new SubNullComparator(offset, length);
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));		
		subNullComparator.write(dos);		
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		subNullComparator.readFields(dis);
		assertEquals(offset, subNullComparator.getOffset());
		assertEquals(length, subNullComparator.getLength());
		//assertNull(subNullComparator.getValue());
	}

}
