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
package co.nubetech.crux.server.filter;


import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.util.CruxException;

public class FilterHelper {

	final static Logger logger = Logger.getLogger(FilterHelper.class);

	/*
	 public static WritableByteArrayComparable getColumnComparator(
	 
			ColumnFilter columnFilter) throws ComparatorNotFoundException {
		String colClassType = columnFilter.getColumnAlias().getValueType()
				.getClassName();
		logger.debug("Class for which comparator is needed is " + colClassType);
		WritableByteArrayComparable comparator = null;
		try {
			Class comparatorClass = ComparatorRegister
					.getComparator(colClassType);
			logger.debug("Class to compare is " + comparatorClass);
			comparator = (WritableByteArrayComparable) comparatorClass
					.newInstance();

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ComparatorNotFoundException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ComparatorNotFoundException(e);
		}
		return comparator;
	}
	*/
	
	public static String getTrimUpperFilterTypeString(FilterType type)
			throws CruxException {
		if (type != null) {
			String typeString = type.getType();
			logger.debug("In type is " + typeString);
			if (typeString != null) {
				typeString = typeString.trim();
				typeString = typeString.replace(" ", "");
				typeString = typeString.toUpperCase();
				logger.debug("Return type is " + typeString);
				return typeString;
			} else {
				throw new CruxException(
						"Null filter type can not be converted to HBase filter");
			}
		} else {
			throw new CruxException(
					"Null filter type can not be converted to HBase filter");

		}
	}

	public static CompareFilter.CompareOp getCompareOp(FilterType type)
			throws CruxException {
		CompareFilter.CompareOp op = null;
		//String inType = getTrimUpperFilterTypeString(type);
		String inType = type.getType();
		if (inType.equals("Equals")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("Not Equals")) {
			op = CompareOp.NOT_EQUAL;
		} else if (inType.equals("Less Than")) {
			op = CompareOp.LESS;
		} else if (inType.equals("Less Than Equals")) {
			op = CompareOp.LESS_OR_EQUAL;
		} else if (inType.equals("Greater Than")) {
			op = CompareOp.GREATER;
		} else if (inType.equals("Greater Than Equals")) {
			op = CompareOp.GREATER_OR_EQUAL;
		} else if (inType.equals("Pattern Matches")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("Pattern Not Matches")) {
			op = CompareOp.NOT_EQUAL;
		} else if (inType.equals("Starts With")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("Ends With")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("Substring")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("Substring Not Matches")) {
			op = CompareOp.NOT_EQUAL;
		} /*else if (inType.equals("NULL")) {
			op = null;
		}
		else {	throw new CruxException(
					"Unsupported filter type. Server is unable to convert it to HBase filter");
		}
		/*
		if (inType.equals("EQUALS")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("NOTEQUALS") || inType.equals("NOTEQUAL")) {
			op = CompareOp.NOT_EQUAL;
		} else if (inType.equals("LESSTHAN") || inType.equals("LESSERTHAN")) {
			op = CompareOp.LESS;
		} else if (inType.equals("LESSTHANEQUAL")
				|| inType.equals("LESSERTHANEQUAL")) {
			op = CompareOp.LESS_OR_EQUAL;
		} else if (inType.equals("GREATERTHAN")) {
			op = CompareOp.GREATER;
		} else if (inType.equals("GREATERTHANEQUAL")) {
			op = CompareOp.GREATER_OR_EQUAL;
		} else if (inType.equals("PATTERNMATCHES")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("PATTERNNOTMATCHES")) {
			op = CompareOp.NOT_EQUAL;
		} else if (inType.equals("STARTSWITH")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("ENDSWITH")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("SUBSTRING")) {
			op = CompareOp.EQUAL;
		} else if (inType.equals("NULL")) {
			op = null;
		}
		else {	throw new CruxException(
					"Unsupported filter type. Server is unable to convert it to HBase filter");
		}
		*/

		return op;
	}

	public static boolean isEqualsFilter(RowAliasFilter filter) throws CruxException {
		boolean isEqual = false;
		if (filter != null) {
			if (CompareOp.EQUAL.equals(getCompareOp(filter.getFilterType()))) {
				isEqual = true;
			}
		} else {
			throw new CruxException("Filter is null");
		}
		return isEqual;
	}

}
