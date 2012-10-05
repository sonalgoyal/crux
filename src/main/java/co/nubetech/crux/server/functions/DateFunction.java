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
package co.nubetech.crux.server.functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.util.CruxException;

public class DateFunction extends FunctionBase implements CruxNonAggregator{

	private final static Logger logger = Logger.getLogger(DateFunction.class);

	String valueType;
	String valueReturn;
	
	public DateFunction(Map<String, String> properties) {
		super();
		this.properties = properties;
		valueType = getProperty(VALUE_TYPE_PROPERTY);
		valueReturn = getProperty(VALUE_RETURN_PROPERTY);
	}

	@Override
	public Object execute(byte[] value) throws CruxException {
		String valueInString = Bytes.toString(value);
		if (valueInString == null) {
			throw new CruxException("");
		}
		GregorianCalendar calendar = null;
		try {
			calendar = createDateObject(valueInString);
			if (calendar == null) {
				throw new CruxException("");
			}
		} catch (ParseException e) {
			throw new CruxException(e.getMessage());
		} catch (Exception e) {
			throw new CruxException(e.getMessage());
		}

		return getResult(calendar);
	}

	private GregorianCalendar createDateObject(String valueInString) throws Exception,
			ParseException, CruxException {
		GregorianCalendar calendar = new GregorianCalendar();
		long valueInLong = 0;
		if (valueType.equals("Long")) {
			 valueInLong = Long.parseLong(valueInString);						
		} else if (valueType.equals("String")) {
			DateFormat df = new SimpleDateFormat();
			Date date = df.parse(valueInString);
			valueInLong = date.getTime();
		} else {
			throw new CruxException("The type " + valueType
					+ "is not supported.");
		}
		calendar.setTimeInMillis(valueInLong);
		return calendar;
	}

	public Object getResult(GregorianCalendar calendar) throws CruxException {
	
		if (valueReturn.equals("date")) {
			return calendar.getTime();
		} else if (valueReturn.equals("day")) {
			return calendar.get(Calendar.DAY_OF_MONTH);
		} else if (valueReturn.equals("month")) {
			return calendar.get(Calendar.MONTH);
		} else if (valueReturn.equals("year")) {
			return calendar.get(Calendar.YEAR);
		} else if (valueReturn.equals("hours")) {
			return calendar.get(Calendar.HOUR);
		} else if (valueReturn.equals("minutes")) {
			return calendar.get(Calendar.MINUTE);
		} else if (valueReturn.equals("seconds")) {
			return calendar.get(Calendar.SECOND);
		} else {
			throw new CruxException(valueReturn + " is not supported.");
		}
	}

}
