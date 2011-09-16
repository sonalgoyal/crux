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

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

public class SubResultScanner implements ResultScanner {
	private Result[] resultArray;
	private int count;
	private int length;

	public SubResultScanner(Result[] resultArray) {
		this.resultArray = resultArray;
		length = resultArray.length;
		count = 0;
	}

	@Override
	public Iterator<Result> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		resultArray = null;

	}

	@Override
	public Result next() throws IOException {
		if (resultArray != null && count < length) {
			return resultArray[count++];
		}
		return null;
	}

	@Override
	public Result[] next(int len) throws IOException {
		Result[] toReturn = new Result[len];
		if (resultArray != null && count + len <= length && len > 0) {
			int i = 0;
			for (int count = this.count; count < this.count + len; count++) {
				toReturn[i++] = resultArray[count];
			}
			return toReturn;
		} else {
			throw new IOException();
		}
	}

}
