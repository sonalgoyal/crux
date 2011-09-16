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
package co.nubetech.crux.common.functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public class DateFunction extends FunctionBase {

	private final static Logger logger = Logger.getLogger(DateFunction.class);

	public String VALUE_TYPE_PROPERTY = "valueType";
	public String VALUE_RETURN_PROPERTY = "valueReturn";

	public DateFunction(Map<String, String> properties) {
		super();
		this.properties = properties;
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		String valueInString = Bytes.toString(value);
		if (valueInString == null) {
			throw new CruxException("");
		}
		Date date = null;
		try {
			date = createDateObject(valueInString);
			if (date == null) {
				throw new CruxException("");
			}
		} catch (ParseException e) {
			throw new CruxException(e.getMessage());
		} catch (Exception e) {
			throw new CruxException(e.getMessage());
		}

		return getResult(date);
	}

	private Date createDateObject(String valueInString) throws Exception,
			ParseException, CruxException {
		Date date = null;
		String valueType = getProperty(VALUE_TYPE_PROPERTY);
		if (valueType.equals("Long")) {
			long valueInLong = Long.parseLong(valueInString);
			date = new Date(valueInLong);
		} else if (valueType.equals("String")) {
			DateFormat df = new SimpleDateFormat();
			date = df.parse(valueInString);
		} else {
			throw new CruxException("The type " + valueType
					+ "is not supported.");
		}
		return date;
	}

	public Object getResult(Date date) throws CruxException {
		String valueReturn = getProperty(VALUE_RETURN_PROPERTY);

		if (valueReturn.equals("date")) {
			return date.getDate();
		} else if (valueReturn.equals("day")) {
			return date.getDay();
		} else if (valueReturn.equals("month")) {
			return date.getMonth();
		} else if (valueReturn.equals("year")) {
			return date.getYear();
		} else if (valueReturn.equals("hours")) {
			return date.getHours();
		} else if (valueReturn.equals("minutes")) {
			return date.getMinutes();
		} else if (valueReturn.equals("seconds")) {
			return date.getSeconds();
		} else if (valueReturn.equals("time")) {
			return date.getTime();
		} else {
			throw new CruxException(valueReturn + " is not supported.");
		}
	}

}
