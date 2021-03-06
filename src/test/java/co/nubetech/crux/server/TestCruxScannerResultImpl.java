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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.junit.Test;

import co.nubetech.crux.model.TestingUtil;

//TODO : big one, tests in this package are breaking, need to revisit

public class TestCruxScannerResultImpl {

	@Test
	public void testNext() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new CruxScannerResultImpl(result, TestingUtil.getReport());
		CruxResult resultFetched = null;
		int count = 0;
		while ((resultFetched = cruxScanner.next()) != null) {
			count++;
			assertTrue(resultFetched instanceof CruxResultImpl);
		}
		assertEquals(1, count);
	}

	
	@Test
	public void testClose() throws IOException {
		Result result = mock(Result.class);
		CruxScanner cruxScanner = new CruxScannerResultImpl(result, null);
		cruxScanner.close();
		assertEquals(null, cruxScanner.next());
	}
}
