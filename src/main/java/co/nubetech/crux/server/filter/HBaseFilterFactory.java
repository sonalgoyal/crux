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

import java.util.Collection;
import java.util.Map;

import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.server.BytesHelper;
import co.nubetech.crux.server.QueryExecutor;
import co.nubetech.crux.server.filter.types.BooleanComparator;
import co.nubetech.crux.server.filter.types.DoubleComparator;
import co.nubetech.crux.server.filter.types.FloatComparator;
import co.nubetech.crux.server.filter.types.IntComparator;
import co.nubetech.crux.server.filter.types.LongComparator;
import co.nubetech.crux.server.filter.types.ShortComparator;
import co.nubetech.crux.server.filter.types.SubBinaryComparator;
import co.nubetech.crux.server.filter.types.SubBinaryPrefixComparator;
import co.nubetech.crux.server.filter.types.SubBinarySuffixComparator;
import co.nubetech.crux.server.filter.types.SubRegexStringComparator;
import co.nubetech.crux.server.filter.types.SubSubstringComparator;
import co.nubetech.crux.util.CruxException;

public class HBaseFilterFactory {
	
	final static Logger logger = Logger.getLogger(HBaseFilterFactory.class);
	/**
	 * factory method for creating a hbase row filter from Crux Row filter 
	 * @param rowFilter
	 * @return
	 * @throws CruxException 
	 */
	public static RowFilter getRowFilter(RowAliasFilter rowFilter, Mapping mapping) throws CruxException {
		FilterType type = rowFilter.getFilterType();
		CompareOp op = FilterHelper.getCompareOp(type);
		RowAlias alias = rowFilter.getRowAlias();
		String value = rowFilter.getValue();
		int offset = getOffset(rowFilter, mapping);
		Integer length = rowFilter.getRowAlias().getLength();
		if (length == null || length == 0) {
			length = -1;
		}
		RowFilter filter = new RowFilter(op, getComparator(type, alias, value, offset, length));
		return filter;
	}
	
	
	/**
	 * Create filters for only those conditions which are not part of the range scan
	 * @param report
	 * @param mapping
	 * @param rangeFilters
	 * @return
	 * @throws CruxException
	 */
	public static FilterList getRowFilters(Report report, Mapping mapping, RangeFilters rangeFilters) 
		throws CruxException{
		Collection<RowAliasFilter> rowAliasFilters = report.getRowAliasFilters();
		FilterList filters = new FilterList();
		for (RowAliasFilter rowAliasFilter: rowAliasFilters) {
			if (!rangeFilters.contains(rowAliasFilter) || rowAliasFilter.getFilterType().getType().equals("Equals")) {
				RowFilter filter = getRowFilter(rowAliasFilter, mapping);
				filters.addFilter(filter);
			}
		}
		return filters;
	}
	
	
	/**
	 * Our comparators are taking ints. Actually the values should be ints.
	 * @param filter
	 * @param mapping
	 * @return
	 */
	
	public static int getOffset(RowAliasFilter filter, Mapping mapping) {
		int offset = 0;
		Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		for (String alias: rowAliases.keySet()) {
			if (alias.equals(filter.getRowAlias().getAlias())) {
				break;
			}
			else {
				offset += rowAliases.get(alias).getLength();
			}
		}
		return offset;
	}

	public static FilterList getColumnFilters(Report report)
			throws CruxException {
		Collection<ColumnFilter> columnFilters = report.getColumnFilters();
	
		FilterList filters = new FilterList();
		if (columnFilters != null) {
			for (ColumnFilter columnFilter : columnFilters) {
				Filter filter = HBaseFilterFactory.getColumnFilter(columnFilter);
				filters.addFilter(filter);
			}
		}
		return filters;
	}
	
	public static WritableByteArrayComparable  getComparator(FilterType type, 
			Alias alias, String value, int offset, int length) throws CruxException {
		WritableByteArrayComparable comparator = null;
		String aliasType = alias.getValueType().getClassName();
		if (aliasType.equals("java.lang.Long")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator = new LongComparator(valueBytes, offset, length);			
		}
		else if (aliasType.equals("java.lang.Integer")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator = new IntComparator(valueBytes, offset, length);
		}
		else if (aliasType.equals("java.lang.Double")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator = new DoubleComparator(valueBytes, offset, length);
		}
		else if (aliasType.equals("java.lang.Float")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator = new FloatComparator(valueBytes, offset, length);
		}
		else if (aliasType.equals("java.lang.Short")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator = new ShortComparator(valueBytes, offset, length);
		}
		else if (aliasType.equals("java.lang.Boolean")) {
			byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
			comparator =  new BooleanComparator(valueBytes, offset, length);
		}
		else if (aliasType.equals("java.lang.String")) {
			String filterType = type.getType(); 
			if (filterType.equals("Substring") || filterType.equals("Substring Not Matches")){
				comparator =  new SubSubstringComparator(value, offset, length);
			}
			else if (filterType.equals("Pattern Matches") || filterType.equals("Pattern Not Matches")){
				comparator =  new SubRegexStringComparator(value, offset, length);
			}
			else if (filterType.equals("Starts With")) {
				byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
				comparator =  new SubBinaryPrefixComparator(valueBytes, offset, length);
			}
			else if (filterType.equals("Ends With")){
				byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
				comparator =  new SubBinarySuffixComparator(valueBytes, offset, length);
			}
			else {
				byte[] valueBytes = BytesHelper.getBytes(aliasType, value);
				comparator  =  new SubBinaryComparator(valueBytes, offset, length);
			}
		}
		return comparator;
	}

	public static Filter getColumnFilter(ColumnFilter columnFilter)
			throws CruxException {
		CompareFilter.CompareOp compareOp = FilterHelper.getCompareOp(columnFilter
				.getFilterType());
		ColumnAlias alias = columnFilter.getColumnAlias();
		byte[] family = Bytes.toBytes(alias.getColumnFamily());
		byte[] qualifier = Bytes.toBytes(alias.getQualifier());
		WritableByteArrayComparable comparator = getComparator(columnFilter.getFilterType(), alias, columnFilter.getValue(), 0, -1);
		logger.debug("Comparator is " + comparator);
		SingleColumnValueFilter filter = new SingleColumnValueFilter(family, qualifier,
				compareOp, comparator);
		//if the column does not exist, do not include
		filter.setFilterIfMissing(true);
		return filter;
	}	

}
