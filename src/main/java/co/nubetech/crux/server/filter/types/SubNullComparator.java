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

public class SubNullComparator extends SubBinaryComparator {

	
	public SubNullComparator() {
		
	}

	public SubNullComparator(int offset, int length) {
		// value = new byte[0]; set value.
		super(null, offset, length);
	}

	@Override
	public int compareTo(byte[] value) {
		int result = -1;
		if (value == null) {
			result = 0;
		} else {
			byte[] valueToCompare = null;
			if (length == -1) {
				valueToCompare = value;
			} else {
				valueToCompare = new byte[length];
				Bytes.putBytes(valueToCompare, 0, value, offset, length);
			}
			if (valueToCompare.length == 0) {
				valueToCompare = null;
			}
			result = valueToCompare != null ? 1 : 0;
		}
		return result;
	}

	/*@Override
	public void readFields(DataInput in) throws IOException {
		offset = in.readInt();
		length = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(offset);
		out.writeInt(length);
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setLength(int length) {
		this.length = length;
	}
	*/

}
