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

public class FloatComparator extends SubBinaryComparator {

	final static Logger logger = Logger.getLogger(FloatComparator.class);
	
	private Float floatValue;

	public FloatComparator() {
		super();
	}

	public FloatComparator(byte[] value, int offset, int length) {
		super(value, offset, length);
		this.floatValue = Bytes.toFloat(value);
	}
	
	public FloatComparator(String value, int offset, int length) {
		super(Bytes.toBytes(Float.parseFloat(value)), offset, length);
		this.floatValue = Float.parseFloat(value);
	}
	
	public FloatComparator(float value, int offset, int length) {
		super(Bytes.toBytes(value), offset, length);
		this.floatValue = value;
	}

	@Override
	public int compareTo(byte[] value) {
		int compareToValue = -1;
		if (value != null) {
			logger.debug("length of passed value is " + value.length);
			if(getLength() == -1){
				Float toCompareFloat = Bytes.toFloat(value);
				compareToValue = floatValue.compareTo(toCompareFloat);
			}else if (value.length >= getOffset() + getLength()) {
				logger.debug("length of passed value is more than offset+length");
				if (value.length >= getOffset()) {
					byte[] toCompare = new byte[getLength()];
					logger.debug("copying ");
					System.arraycopy(value, getOffset(), toCompare, 0,
							getLength());
					Float toCompareFloat = Bytes.toFloat(toCompare);
					compareToValue = floatValue.compareTo(toCompareFloat);
				}
			}
		}
		// should we really be returning -1 here?
		return compareToValue;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		floatValue = Bytes.toFloat(getValue());	;
	}

/*	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(getOffset());
		out.writeInt(getLength());
		out.writeFloat(floatValue);
	}*/

	public Float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}
}
