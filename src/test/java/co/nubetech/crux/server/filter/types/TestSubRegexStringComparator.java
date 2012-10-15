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
import java.nio.charset.Charset;

import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class TestSubRegexStringComparator {
	
	@Test
	public void testForLengthNeg(){
		SubRegexStringComparator subRegexComp = new SubRegexStringComparator("[0-9]",0,-1);
		int result = subRegexComp.compareTo(Bytes.toBytes("acg123"));
		assertEquals(0, result);
	}
	
	@Test
	public void testForRightValue(){
		SubRegexStringComparator subRegexComp = new SubRegexStringComparator("[0-9]",0,6);
		int result = subRegexComp.compareTo(Bytes.toBytes("acg123"));
		assertEquals(0, result);
	}
	
	@Test
	public void testForWrongValue(){
		SubRegexStringComparator subRegexComp = new SubRegexStringComparator("[0-9]",0,3);
		int result = subRegexComp.compareTo(Bytes.toBytes("acg123"));
		assertEquals(1, result);
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void testForNullRegex(){
		SubRegexStringComparator subRegexComp = new SubRegexStringComparator(null,0,3);
		subRegexComp.compareTo(Bytes.toBytes("acg123"));
	}
	
	@Test
	public void testForNullValue(){
		SubRegexStringComparator subRegexComp = new SubRegexStringComparator("[0-9]",0,3);
		int result = subRegexComp.compareTo(null);
		assertEquals(-1, result);
	}
	
	@Test
	public void testReadFields() throws IOException{		
		String value = "abcdef";
		int offset = 0;
		int length = 6;
		Charset charset = Charset.forName(HConstants.UTF8_ENCODING);
		
		SubRegexStringComparator comparator = new SubRegexStringComparator(value, offset, length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));	
		Bytes.writeByteArray(dos, Bytes.toBytes("abcdef"));		
		dos.writeInt(offset);
		dos.writeInt(length);
		dos.writeUTF(value);
		dos.writeUTF(charset.toString());
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		comparator.readFields(dis);
		assertEquals(offset, comparator.getOffset());
		assertEquals(length, comparator.getLength());
		assertEquals(value, Bytes.toString(comparator.getValue()));
	}
	
	@Test
	public void testWriteFields() throws IOException{		
		String value = "abcdef";
		int offset = 0;
		int length = 6;
		
		SubRegexStringComparator comparator = new SubRegexStringComparator(value, offset, length);		
		
		File temp = File.createTempFile("testFile", null);
		temp.deleteOnExit();
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(temp));	
		comparator.write(dos);
		
		DataInputStream dis = new DataInputStream(new FileInputStream(temp));
		comparator.readFields(dis);
		assertEquals(offset, comparator.getOffset());
		assertEquals(length, comparator.getLength());
		assertEquals(value, Bytes.toString(comparator.getValue()));
	}

}
