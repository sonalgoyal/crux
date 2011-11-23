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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

public class SubSubstringComparator extends SubBinaryComparator {

	private String stringValue;
	
	public SubSubstringComparator() {
		
	}

	public SubSubstringComparator(String value, int offset, int length) {
		super(Bytes.toBytes(value),offset,length);
		this.stringValue = value;
	}

	@Override
	public int compareTo(byte[] value) {
		int result = -1;
		if (value != null) {
			if (length == -1) {
				result = Bytes.toString(value)
						.contains(stringValue) ? 0 : 1;
			} else {
			result = Bytes.toString(value, offset, length)
					.contains(stringValue) ? 0 : 1;
			}
		}
		return result;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		stringValue = Bytes.toString(getValue());
	}

	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		//out.writeUTF(stringValue);
	}

}
