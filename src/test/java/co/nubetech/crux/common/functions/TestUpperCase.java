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

import org.junit.Test;

import co.nubetech.crux.functions.UpperCase;
import co.nubetech.crux.util.CruxException;

public class TestUpperCase {

	@Test
	public void testExecuteWithCorrectValues() throws CruxException {
		UpperCase substring = new UpperCase();
		String result = (String) substring.execute("TestString".getBytes());
		System.out.println(result);
		assertEquals(result, "TESTSTRING");
	}

	@Test
	public void testExecuteWithNullValues() throws CruxException {
		UpperCase substring = new UpperCase();
		String result = (String) substring.execute(null);
		assertEquals(result, null);
	}

}
