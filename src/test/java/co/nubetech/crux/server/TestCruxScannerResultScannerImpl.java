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
//TODO: fix the junist

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;

public class TestCruxScannerResultScannerImpl {

	//@Test
	public void testNext() throws IOException {
		Result result1 = new Result();
		Result result2 = new Result();
		Result result3 = new Result();
		SubResultScanner resultScanner = new SubResultScanner(new Result[] {
				result1, result2, result3 });
		CruxScanner cruxScanner = new CruxScannerResultScannerImpl(resultScanner, null);

		assertEquals(result1, cruxScanner.next());
		assertEquals(result2, cruxScanner.next());
		assertEquals(result3, cruxScanner.next());
		assertEquals(null, cruxScanner.next());

	}

	//@Test(expected = IOException.class)
	public void testNextArrayForLessThanOne() throws IOException {
		Result result1 = new Result();
		Result result2 = new Result();
		Result result3 = new Result();
		SubResultScanner resultScanner = new SubResultScanner(new Result[] {
				result1, result2, result3 });
		CruxScanner cruxScanner = new CruxScannerResultScannerImpl(resultScanner, null);
		cruxScanner.next();
	}

	//@Test(expected = IOException.class)
	public void testNextArrayForMoreThanLength() throws IOException {
		Result result1 = new Result();
		Result result2 = new Result();
		Result result3 = new Result();
		SubResultScanner resultScanner = new SubResultScanner(new Result[] {
				result1, result2, result3 });
		CruxScanner cruxScanner = new CruxScannerResultScannerImpl(resultScanner, null);
		cruxScanner.next();
		cruxScanner.next();
		cruxScanner.next();
		cruxScanner.next();
		cruxScanner.next();
	}

	//TODO
	//@Test
	public void testNextArrayForValidLength() throws IOException {
		Result result1 = new Result();
		Result result2 = new Result();
		Result result3 = new Result();
		SubResultScanner resultScanner = new SubResultScanner(new Result[] {
				result1, result2, result3 });
		CruxScanner cruxScanner = new CruxScannerResultScannerImpl(resultScanner, null);
		CruxResult result = cruxScanner.next();
		//assertEquals(result1, result.get(0));
		//assertEquals(result2, result[1]);
		//assertEquals(2, result.length);
	}

	//@Test
	public void testClose() throws IOException {
		Result result1 = new Result();
		Result result2 = new Result();
		Result result3 = new Result();
		SubResultScanner resultScanner = new SubResultScanner(new Result[] {
				result1, result2, result3 });
		CruxScanner cruxScanner = new CruxScannerResultScannerImpl(resultScanner, null);
		cruxScanner.close();
		assertEquals(null, cruxScanner.next());
	}
	
	
}
