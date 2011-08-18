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

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.util.CruxException;

public class TestSubByteArray {

	@Test
	public void testExecute() throws CruxException {
		SubByteArray subByteArray = new SubByteArray(
				new HashMap<String, String>());
		subByteArray.setProperty(subByteArray.OFFSET_PROPERTY, "4");
		subByteArray.setProperty(subByteArray.LENGTH_PROPERTY, "4");
		byte[] input = Bytes.add(Bytes.toBytes("abcd"), Bytes.toBytes(112));
		byte[] result = subByteArray.execute(input);
		assertTrue(Bytes.equals(Bytes.toBytes(112), result));
	}

	@Test(expected = CruxException.class)
	public void testExecuteWrongParameter() throws CruxException {
		SubByteArray subByteArray = new SubByteArray(
				new HashMap<String, String>());
		subByteArray.setProperty(subByteArray.OFFSET_PROPERTY, "4");
		subByteArray.setProperty(subByteArray.LENGTH_PROPERTY, "5");
		byte[] input = Bytes.add(Bytes.toBytes("abcd"), Bytes.toBytes(112));
		byte[] result = subByteArray.execute(input);

	}

}
