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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;

public class TestGetScanner {

	@Test
	public void testNext() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new GetScanner(result);
		Result resultFetched = null;
		int count = 0;
		while ((resultFetched = cruxScanner.next()) != null) {
			count++;
			assertEquals(result, resultFetched);
		}
		assertEquals(1, count);
	}

	@Test(expected = IOException.class)
	public void testNextRequestForMoreThanOneObjects() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new GetScanner(result);
		cruxScanner.next(2);
	}

	@Test(expected = IOException.class)
	public void testNextRequestForLessThanOneObjects() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new GetScanner(result);
		cruxScanner.next(0);
	}

	@Test
	public void testNextRequestForOneObjects() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new GetScanner(result);
		Result[] resultArray = cruxScanner.next(1);
		assertEquals(1, resultArray.length);
		assertEquals(result, resultArray[0]);
	}

	@Test
	public void testClose() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new GetScanner(result);
		cruxScanner.close();
		assertEquals(null, cruxScanner.next());
	}
}
