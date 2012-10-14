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
package co.nubetech.crux.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.filter.HBaseFilterFactory;
import co.nubetech.crux.server.filter.RangeFilters;
import co.nubetech.crux.server.filter.RowFilterComparator;
import co.nubetech.crux.util.CruxException;

public class QueryExecutor {
	HTableInterface hTable;
	final static Logger logger = Logger.getLogger(QueryExecutor.class);

	public QueryExecutor(HTableInterface hTable) {
		this.hTable = hTable;
	}

	protected CruxScanner execute(Report report, Mapping mapping)
			throws CruxException {
		CruxScanner scanner = null;
		try {
			// HTable hTable = (HTable)
			// hTablePool.getTable(mapping.getTableName());
			logger.debug("TableName is: "
					+ Bytes.toString(hTable.getTableName()));
			if (isGetOperation(report, mapping)) {
				byte[] getRow = getGetRow(report, mapping);
				// URLCodec codec = new URLCodec();
				// logger.debug("getRow is: " +
				// Bytes.toLong(codec.decode(getRow)));
				Get get = new Get(getRow);
				FilterList filters = HBaseFilterFactory.getColumnFilters(report);
				get.setFilter(filters);
				setSelectedColumns(report, get);
				Result result = hTable.get(get);
				logger.debug("getRow is: " + result.getRow());
				scanner = new GetScanner(result, report);
			} else {
				Scan scan = new Scan();
				RangeFilters rangeFilters = getRangeFilters(report, mapping);
				setRangeScan(scan, mapping, rangeFilters);
				FilterList columnFilters = HBaseFilterFactory.getColumnFilters(report);
				FilterList rowFilters = HBaseFilterFactory.getRowFilters(report, mapping, rangeFilters);
				for (Filter columnFilter: columnFilters.getFilters()) {
					rowFilters.addFilter(columnFilter);
				}
				scan.setFilter(rowFilters);
				setSelectedColumns(report, scan);
				logger.debug("Scan object is " + scan);
				ResultScanner resultScanner = hTable.getScanner(scan);
				if (report.getGroupBys() != null) {
					//we have to call the coprocessors here
					//TODO
				}
				else {
					scanner = new ScanScanner(resultScanner, report);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CruxException(e);
		}
		return scanner;
	}
	
	protected void close() throws CruxException {
		if (hTable != null) {
			try {
				hTable.close();
			}
			catch(Exception e) {
				e.printStackTrace();
				throw new CruxException(e);
			}
		}
	}
	
	protected void setRangeScan(Scan scan, Mapping mapping, RangeFilters filters) throws CruxException{
		List<RowAliasFilter> lesserRowFilters = filters.getLesserRowFilters();
		List<RowAliasFilter> greaterRowFilters = filters.getGreaterRowFilters();
		if (lesserRowFilters != null && lesserRowFilters.size() > 0) {
			scan.setStopRow(getRowBytes(lesserRowFilters, mapping));
		}
		if (greaterRowFilters != null && greaterRowFilters.size() > 0) {
			scan.setStartRow(getRowBytes(greaterRowFilters, mapping));
		}
		
	}
	
	/**
	 * It is a range scan if all the filters on the row are equals and >= or
	 * equals and <
	 * 
	 * @param report
	 * @param mapping
	 * @param scan
	 * @return
	 * @throws CruxException
	 */
	 
	protected RangeFilters getRangeFilters(Report report, Mapping mapping)
			throws CruxException {
		// check if left aliases are equal
		// the incoming map is sorted by id.
		Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		// the filters are specified by the user and are not sorted.
		ArrayList<RowAliasFilter> rowFilters = new ArrayList<RowAliasFilter>(
				report.getRowAliasFilters());
		// the row filters are sorted by the row aliases id
		// so that the left most are first
		boolean isRangeScan = true;
		RangeFilters rangeFilters = new RangeFilters();
		if (rowFilters != null) {
			ArrayList<RowAliasFilter> lesserRowFilters = new ArrayList<RowAliasFilter>();
			ArrayList<RowAliasFilter> greaterRowFilters = new ArrayList<RowAliasFilter>();

			for (RowAliasFilter filter : rowFilters) {
				String filterType = filter.getFilterType().getType();
				if (filterType.equals("Equals")) {
					lesserRowFilters.add(filter);
					greaterRowFilters.add(filter);
				} else if (filterType.equals("Greater Than Equals")) {
					greaterRowFilters.add(filter);
				} else if (filterType.equals("Less Than")) {
					lesserRowFilters.add(filter);
				} else {
					logger.debug("Got filter type " + filterType + ", this is not a range scan");
					isRangeScan = false;
				}
			}

			if (isRangeScan) {
				/*
				 * we have to see that a. all aliases are specified b. OR
				 * rightmost column specified in filters is range
				 */
				int lesserFiltersSize = lesserRowFilters.size();
				if (lesserFiltersSize > 0) {
						logger.debug("Checking if lesser filters are range scan");
						isRangeScan = isRangeScan(lesserRowFilters, rowAliases, "Less Than");
						if (isRangeScan) {
							rangeFilters.setLesserRowFilters(lesserRowFilters);
						}
				}
				// same for greater row
				int greaterFilterSize = greaterRowFilters.size();
				if (greaterFilterSize > 0) {
						logger.debug("Checking if greater filters are range scan");
						isRangeScan = isRangeScan(greaterRowFilters, rowAliases, "Greater Than Equals");
						if (isRangeScan) {
							rangeFilters.setGreaterRowFilters(greaterRowFilters);					
						}
				}
			}
		}
		return rangeFilters;
	}

	/**
	 * A range scan means all filters are of type >= and or =
	 * OR
	 * = and/or < 
	 * For composite key AB, range filter(gt than) will be A=, B>=; A>=, B=; A>=, B>=; A>=
	 * For composite key ABC, range filter(gt) will be 
	 * A=, B=, C>=; 
	 * A=, B>=, C=; 
	 * A=, B>=, C>=; 
	 * A>=, B=, C=; 
	 * A>=, B=, C>=; 
	 * A>=, B>=, C=
	 * A>=, B>=, C>=
	 * A>= 
	 * Similar pattern will be for <
	 * 
	 * What this means is that we need eq/gt eq/le filters for ALL aliases uptil the last alias
	 * which has a greater than eq/le. 
	 * 
	 * Also, filters should be present on ALL aliases from left to right, no alias should be missing.
	 * @param rowFilters
	 * @param rowAliases
	 * @return
	 * @throws CruxException 
	 */
	//TODO: We could also have A>= and C= which should translate to range scan on A and filter on C. 
	//But we are not supporting that right now. 
	//We can take care of that by sending the right values in the calling method - later
	protected boolean isRangeScan(ArrayList<RowAliasFilter> rowFilters, Map<String, RowAlias> rowAliasesMap, 
			String type) throws CruxException {
		boolean isRangeScan = false;
		Collections.sort(rowFilters, new RowFilterComparator());
		// we now need to make sure all the left aliases are in there.
		int index = -1;
		int count = 0;
		RowAliasFilter rangeFilter  = null;
		for (RowAliasFilter filter : rowFilters) {
			if (type.equals(filter.getFilterType().getType())) {
				index = count;
				rangeFilter = filter;
				logger.debug("At index " + index + ", found matching filter " + rangeFilter);
			}
			count++;
		}
		logger.debug("Finally, at index " + index + ", found matching filter " + rangeFilter);
		
		if (index != -1) {
			count = 0;
			RowAlias rowAliasAtIndex = null;
			//this is the check for cases for composite A B C and filters on A and C
			//count will not match in that case
			for (RowAlias rowAlias: rowAliasesMap.values()) {
				if (count == index) {
					logger.debug("Found the alias at index " + index);
					rowAliasAtIndex = rowAlias;
					logger.debug("Found the alias at index " + rowAliasAtIndex);
				}
				count ++;
			}
			if (rowAliasAtIndex != null) {
				logger.debug("Row Alias at index=" + rowAliasAtIndex);
				logger.debug("Range Filter is " + rangeFilter);
				if (rowAliasAtIndex.equals(rangeFilter.getRowAlias())) {
					isRangeScan = true;
				}
			}
		}
		logger.debug("Returning " + isRangeScan);
		return isRangeScan;
	}

	/**
	 * It is a range scan if all the filters on the row are equals and >= or
	 * equals and <
	 * 
	 * @param report
	 * @param mapping
	 * @param scan
	 * @return
	 * @throws CruxException
	 * 
	 
	protected RangeFilters getRangeFilters(Report report, Mapping mapping)
			throws CruxException {
		// check if left aliases are equal
		// the incoming map is sorted by id.
		Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		// the filters are specified by the user and are not sorted.
		ArrayList<RowAliasFilter> rowFilters = new ArrayList<RowAliasFilter>(
				report.getRowAliasFilters());
		// the row filters are sorted by the row aliases id
		// so that the left most are first
		boolean isRangeScan = true;
		RangeFilters rangeFilters = new RangeFilters();
		if (rowFilters != null) {
			ArrayList<RowAliasFilter> lesserRowFilters = new ArrayList<RowAliasFilter>();
			ArrayList<RowAliasFilter> greaterRowFilters = new ArrayList<RowAliasFilter>();

			for (RowAliasFilter filter : rowFilters) {
				String filterType = filter.getFilterType().getType();
				if (filterType.equals("Equals")) {
					lesserRowFilters.add(filter);
					greaterRowFilters.add(filter);
				} else if (filterType.equals("Greater Than Equals")) {
					greaterRowFilters.add(filter);
				} else if (filterType.equals("Less Than")) {
					lesserRowFilters.add(filter);
				} else {
					logger.debug("Got filter type " + filterType + ", this is not a range scan");
					isRangeScan = false;
				}
			}

			if (isRangeScan) {
				/*
				 * we have to see that a. all aliases are specified b. OR
				 * rightmost column specified in filters is range
				int lesserFiltersSize = lesserRowFilters.size();
				if (lesserFiltersSize > 0) {
					if (lesserFiltersSize == rowAliases.size()) {
						logger.debug("All filters are less than");
						rangeFilters.setLesserRowFilters(lesserRowFilters);
					} else {
						logger.debug("All filters are not less than, some are, lets see which ones");
						int index = getIndexOfCompareFilter(lesserRowFilters,
								CompareOp.LESS);
						if (index == (lesserRowFilters.size()-1)) {
							logger.debug("Right most is");
							rangeFilters.setLesserRowFilters(lesserRowFilters);
						}
					}
				}
				
				// same for greater row
				int greaterFilterSize = greaterRowFilters.size();
				if (greaterFilterSize > 0) {
					if (greaterRowFilters.size() == rowAliases.size()) {
					
						logger.debug("All filters are greater than");
						rangeFilters.setGreaterRowFilters(greaterRowFilters);
					} else {
						logger.debug("All filters are not greater than, some are, lets see which ones");
						
						int index = getIndexOfCompareFilter(greaterRowFilters,
								CompareOp.GREATER_OR_EQUAL);
						if (index == (greaterRowFilters.size() -1)) {
							logger.debug("Right most is");
							rangeFilters.setGreaterRowFilters(greaterRowFilters);
						}
					}
				}
			}
		}
		return rangeFilters;
	}
	 
	

	
	protected int getIndexOfCompareFilter(ArrayList<RowAliasFilter> rowFilters,
			FilterType type) throws CruxException {

		Collections.sort(rowFilters, new RowFilterComparator());
		// we now need to make sure all the left aliases are in there.
		int index = -1;
		int count = 0;
		for (RowAliasFilter filter : rowFilters) {
			if (type.getType().equals(filter.getFilterType().getType())) {
				index = count;
			}
			count++;
		}
		return index;
	}
	*/
	
	
	protected void setSelectedColumns(Report report, Get get) {
		Collection<ReportDesign> designs = report.getDesigns();
		if (designs != null) {
			logger.debug("designs.size is: " + designs.size());

			for (ReportDesign design : designs) {
				// we are only bothered about the column alias here
				// the row will be passed back in entirety
				ColumnAlias alias = design.getColumnAlias();
				if (alias != null) {
					logger.debug("alias.getColumnFamily is: "
							+ alias.getColumnFamily());
					logger.debug("alias.getQualifier is: "
							+ alias.getQualifier());
					get.addColumn(Bytes.toBytes(alias.getColumnFamily()),
							Bytes.toBytes(alias.getQualifier()));
				}
			}
		}
	}

	protected void setSelectedColumns(Report report, Scan scan) {
		Collection<ReportDesign> designs = report.getDesigns();
		if (designs != null) {
			logger.debug("designs.size is: " + designs.size());
			for (ReportDesign design : designs) {
				// we are only bothered about the column alias here
				// the row will be passed back in entirety
				ColumnAlias alias = design.getColumnAlias();
				if (alias != null) {
					logger.debug("alias.getColumnFamily is: "
							+ alias.getColumnFamily());
					logger.debug("alias.getQualifier is: "
							+ alias.getQualifier());
					scan.addColumn(Bytes.toBytes(alias.getColumnFamily()),
							Bytes.toBytes(alias.getQualifier()));
				}
			}
		}
		// we may need to columns with the filters too here, lets see.
	}

	protected boolean isGetOperation(Report report, Mapping mapping)
			throws CruxException {
		// it is a get operation if all aliases - one or many are equals
		boolean isGet = true;
		// get list of row aliases
		int numRowAlias = mapping.getRowAlias().size();
		// get filters on row aliases
		Collection<RowAliasFilter> rowFilters = report.getRowAliasFilters();
		int rowFilterSize = rowFilters.size();
		if (numRowAlias == rowFilterSize) {
			logger.debug("Number of row aliases and number of filters match");
			for (RowAliasFilter rowFilter : rowFilters) {
				if (!rowFilter.getFilterType().getType().equals("Equals")) {
					logger.debug("Encountered row filter with type: "
							+ rowFilter.getFilterType().getType());
					isGet = false;
				}
			}
		} else {
			isGet = false;
		}
		return isGet;
	}

	// returns the row byte[] for get operation
	protected static byte[] getGetRow(Report report, Mapping mapping)
			throws CruxException {
		return getRowBytes(report.getRowAliasFilters(), mapping);
	}

	// returns the row byte[] for get operation
	protected static byte[] getRowBytes(Collection<RowAliasFilter> filters,
			Mapping mapping) throws CruxException {
		// Map<String, RowAlias> rowAliases = mapping.getRowAlias();
		byte[] rowBytes = null;
		ArrayList<RowAliasFilter> rowFilters = new ArrayList<RowAliasFilter>(filters);
		Collections.sort(rowFilters, new RowFilterComparator());
		for (RowAliasFilter rowFilter : rowFilters) {
			ValueType rowAliasValueType = rowFilter.getRowAlias()
					.getValueType();
			// URLCodec codec = new URLCodec();
			// rowBytes =
			// codec.encode(BytesHelper.addToByteArray(rowAliasValueType,
			// rowFilter.getValue(),
			// rowBytes));
			rowBytes = BytesHelper.addToByteArray(rowAliasValueType,
					rowFilter.getValue(), rowBytes);
			logger.debug("Row bytes are: " + rowBytes);

		}
		/*for (int i = 0; i < rowBytes.length; ++i) {
			logger.debug(rowBytes[i]);
		}*/
		// URLCodec codec = new URLCodec();
		// codec.encode(rowBytes);
		return rowBytes;
	}

}
