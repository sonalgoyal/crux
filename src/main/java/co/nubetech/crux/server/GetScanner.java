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

import org.apache.hadoop.hbase.client.Result;

public class GetScanner implements CruxScanner {

	private Result result;
	private boolean isRead;

	public GetScanner(Result get) {
		this.result = get;
	}

	public void close() {
		result = null;
		// do nothing really;
	}

	public Result next() throws IOException {
		if (isRead) {
			close();
		} else {
			isRead = true;
		}
		return result;
	}

	public Result[] next(int n) throws IOException {
		if (n >= 2) {
			throw new IOException(
					"Number of results requested is more than results available");
		} else if (n < 1) {
			throw new IOException("Invalid number of results requested");
		} else {
			return new Result[] { next() };
		}
	}

}
