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
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class SubRegexStringComparator extends SubBinaryComparator {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.server.filter.types.SubRegexStringComparator.class);

	private Pattern pattern;
	private Charset charset = Charset.forName(HConstants.UTF8_ENCODING);
	
	public SubRegexStringComparator() {
		
	}

	public SubRegexStringComparator(String expr, int offset, int length) {
		super(Bytes.toBytes(expr), offset, length);
		this.pattern = Pattern.compile(expr, Pattern.DOTALL);
	}

	@Override
	public int compareTo(byte[] value) {
		int result = -1;
		if (value != null) {
			if (length == -1) {
				result = pattern.matcher(
						new String(Bytes.toString(value).getBytes(), charset))
						.find() ? 0 : 1;
			} else {
				result = pattern.matcher(
						new String(Bytes.toString(value, offset, length)
								.getBytes(), charset)).find() ? 0 : 1;
			}
		}
		return result;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		super.readFields(in);
		final String expr = in.readUTF();
	    this.pattern = Pattern.compile(expr);
	    final String charset = in.readUTF();
	    if (charset.length() > 0) {
	      try {
	        this.charset = Charset.forName(charset);
	      } catch (IllegalCharsetNameException e) {
	        logger.error("invalid charset", e);
	      }
	    }
	}

	@Override
	public void write(DataOutput out) throws IOException {
		super.write(out);
		out.writeUTF(pattern.toString());
		out.writeUTF(charset.name());
	}
}
