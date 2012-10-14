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
import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class DoubleComparator extends SubBinaryComparator {

	final static Logger logger = Logger.getLogger(DoubleComparator.class);
	
	private Double doubleValue;	

	public DoubleComparator() {
		super();
	}

	public DoubleComparator(byte[] value, int offset, int length) {
		super(value, offset, length);
		this.doubleValue = Bytes.toDouble(value);

	}
	
	public DoubleComparator(String value, int offset, int length) {
		super(Bytes.toBytes(Double.parseDouble(value)), offset, length);
		this.doubleValue = Double.parseDouble(value);
	}
	
	public DoubleComparator(double value, int offset, int length) {
		super(Bytes.toBytes(value), offset, length);
		this.doubleValue = value;
	}

	@Override
	public int compareTo(byte[] value) {
		int compareToValue = -1;
		if (value != null) {
			logger.debug("length of passed doubleValue is " + value.length);
			if(getLength() == -1){
				Double toCompareDouble = Bytes.toDouble(value);
				compareToValue = doubleValue.compareTo(toCompareDouble);
			}
			else if(value.length >= getOffset() + getLength()) {
				logger.debug("length of passed doubleValue is more than offset+length");
				if (value.length >= getOffset()) {					
					byte[] toCompare = new byte[getLength()];
					logger.debug("copying ");					
					System.arraycopy(value, getOffset(), toCompare, 0, getLength());
					Double toCompareDouble = Bytes.toDouble(toCompare);
					compareToValue = doubleValue.compareTo(toCompareDouble);
				}
			}
		}
		// should we really be returning -1 here?
		return compareToValue;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		doubleValue = Bytes.toDouble(getValue());	
	}

	/*@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		out.writeDouble(doubleValue);	
	}*/

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	
	

}
