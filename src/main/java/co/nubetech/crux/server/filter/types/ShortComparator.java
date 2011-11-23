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

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class ShortComparator extends SubBinaryComparator {

	final static Logger logger = Logger.getLogger(ShortComparator.class);
	
	private Short shortValue;

	public ShortComparator() {
		super();
	}

	public ShortComparator(byte[] value, int offset, int length) {
		super(value, offset, length);
		this.shortValue = Bytes.toShort(value);
	}
	
	public ShortComparator(String value, int offset, int length) {
		super(Bytes.toBytes(Short.parseShort(value)), offset, length);
		this.shortValue = Short.parseShort(value);
	}
	
	public ShortComparator(short value, int offset, int length) {
		super(Bytes.toBytes(value), offset, length);
		this.shortValue = value;
	}

	@Override
	public int compareTo(byte[] value) {
		int compareToValue = -1;			
		if (value != null) {
			logger.debug("length of passed value is " + value.length);
			if(getLength() == -1){
				Short toCompareShort = Bytes.toShort(value);
				compareToValue = shortValue.compareTo(toCompareShort);
			}else if (value.length >= getOffset() + getLength()) {
				logger.debug("length of passed value is more than offset+length");
				if (value.length >= getOffset()) {
					byte[] toCompare = new byte[getLength()];
					logger.debug("copying ");
					System.arraycopy(value, getOffset(), toCompare, 0,
							getLength());
					Short toCompareShort = Bytes.toShort(toCompare);
					compareToValue = shortValue.compareTo(toCompareShort);
				}
			}
		}
		// should we really be returning -1 here?
		return compareToValue;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		shortValue = Bytes.toShort(getValue());
	}

	public Short getShortValue() {
		return shortValue;
	}

	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}

	/*@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(getOffset());
		out.writeInt(getLength());
		out.writeShort(shortValue);
	}*/

}