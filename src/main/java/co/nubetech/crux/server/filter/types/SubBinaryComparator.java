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

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class SubBinaryComparator extends BinaryComparator {

	final static Logger logger = Logger.getLogger(BinaryComparator.class);

	protected int offset;
	protected int length;
	//private byte[] value;

	/**
	 * compare against the given value compare with passed value in compare to,
	 * at the offset uptil length OF THE PASSED VALUE
	 * avniavni
	 * @param value
	 * @param offset
	 * @param length
	 */
	public SubBinaryComparator(byte[] value, int offset, int length) {
		super(value);
		//this.value = value;
		this.offset = offset;
		this.length = length;
		logger.debug("Offset and length are " + offset + "," + length);
	}

	public SubBinaryComparator() {
		super();
	}

	@Override
	public int compareTo(byte[] value) {
		logger.debug("Comparing " + getValue() + " with " + value );
		int result = -1;
		if (value != null) {
			if (length == -1) {
				result = Bytes.compareTo(getValue(), value);
			} else {
				result = Bytes.compareTo(getValue(), 0, getValue().length, value, offset, length 
						);
			}
		}
		return result;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		offset = in.readInt();
		length = in.readInt();
		 
		
	}

	@Override
	public void write(DataOutput out) throws IOException {		
		super.write(out);
		out.writeInt(offset);
		out.writeInt(length);
		//Bytes.writeByteArray(out, getValue());
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

}
