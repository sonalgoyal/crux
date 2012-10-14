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

import org.apache.hadoop.hbase.util.Bytes;

public class SubBinaryPrefixComparator extends SubBinaryComparator {
	
	public SubBinaryPrefixComparator() {
		
	}

	public SubBinaryPrefixComparator(byte[] value, int offset, int length) {
		super(value, offset, length);
	}

	@Override
	public int compareTo(byte[] value) {
		int result = -1;
		int valueLength = 0;
		if (value != null) {
			if (length == -1) {
				valueLength = value.length;
			} else{
				valueLength = length;
			}
			byte[] valueToCompareWith = getValue();
			byte[] valueToCompare = new byte[valueLength];
			Bytes.putBytes(valueToCompare, 0, value, offset, valueLength);

			if (valueToCompareWith.length <= valueToCompare.length) {
				result = Bytes.compareTo(valueToCompareWith, 0,
						valueToCompareWith.length, valueToCompare, 0,
						valueToCompareWith.length);
			} else {
				result = Bytes.compareTo(valueToCompareWith, valueToCompare);
			}
		}
		return result;

	}
	
}
