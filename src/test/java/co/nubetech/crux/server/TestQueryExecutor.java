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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.filter.RangeFilters;
import co.nubetech.crux.util.CruxException;

public class TestQueryExecutor {

	final static Logger logger = Logger.getLogger(TestQueryExecutor.class);

	@Test
	public void testSetSelectedColumnsScanForColumnAliasNull() {
		Report report = new Report();
		
		ReportDesign design4 = new ReportDesign();
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("row");
		report.addDesign(design4);
		
		Scan scan = new Scan();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, scan);
		assertTrue(!scan.hasFamilies());

	}

	@Test
	public void testSetSelectedColumnsScanForDesignNull() {
		Report report = new Report();

		Scan scan = new Scan();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, scan);
		assertTrue(!scan.hasFamilies());

	}

	@Test
	public void testSetSelectedColumnsGetForColumnAliasNull() {
		Report report = new Report();
		
		ReportDesign design4 = new ReportDesign();
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("row");
		report.addDesign(design4);
		
		Get get = new Get();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, get);
		assertTrue(!get.hasFamilies());
	}

	@Test
	public void testSetSelectedColumnsGetForDesignNull() {
		Report report = new Report();
		
		Get get = new Get();
		
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, get);
		assertTrue(!get.hasFamilies());
	}

	@Test
	public void testSetSelectedColumnsGet() {
		Report report = new Report();

		ReportDesign design1 = new ReportDesign();
		ColumnAlias alias1 = new ColumnAlias();
		alias1.setColumnFamily("family");
		alias1.setQualifier("qualifier");
		design1.setColumnAlias(alias1);

		ReportDesign design2 = new ReportDesign();
		ColumnAlias alias2 = new ColumnAlias();
		alias2.setColumnFamily("family");
		alias2.setQualifier("qualifier1");
		design2.setColumnAlias(alias2);

		ReportDesign design3 = new ReportDesign();
		ColumnAlias alias3 = new ColumnAlias();
		alias3.setColumnFamily("family1");
		alias3.setQualifier("qualifier");
		design3.setColumnAlias(alias3);

		ReportDesign design4 = new ReportDesign();
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("row");

		report.addDesign(design1);
		report.addDesign(design2);
		report.addDesign(design3);
		report.addDesign(design4);

		Get get = new Get();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, get);

		Map<byte[], NavigableSet<byte[]>> map = get.getFamilyMap();
		for (byte[] family : map.keySet()) {
			System.out.println("Family is " + Bytes.toString(family));
			NavigableSet<byte[]> set = map.get(family);
			for (byte[] col : set) {
				System.out.println("Column is " + Bytes.toString(col));
			}
		}

		byte[] familyBytes = Bytes.toBytes("family");
		byte[] family1Bytes = Bytes.toBytes("family1");

		assertEquals(2, map.size());
		assertTrue(map.containsKey(familyBytes));
		assertTrue(map.containsKey(family1Bytes));

		assertEquals(2, map.get(familyBytes).size());
		assertEquals(1, map.get(family1Bytes).size());

		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier")));
		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier1")));
		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier")));

	}

	@Test
	public void testSetSelectedColumnsGetNoDesign() {
		Report report = new Report();

		Get get = new Get();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, get);

		Map<byte[], NavigableSet<byte[]>> map = get.getFamilyMap();
		for (byte[] family : map.keySet()) {
			System.out.println("Family is " + Bytes.toString(family));
			NavigableSet<byte[]> set = map.get(family);
			for (byte[] col : set) {
				System.out.println("Column is " + Bytes.toString(col));
			}
		}
		assertEquals(0, map.size());
	}

	@Test
	public void testSetSelectedColumnsScan() {
		Report report = new Report();

		ReportDesign design1 = new ReportDesign();
		ColumnAlias alias1 = new ColumnAlias();
		alias1.setColumnFamily("family");
		alias1.setQualifier("qualifier");
		design1.setColumnAlias(alias1);

		ReportDesign design2 = new ReportDesign();
		ColumnAlias alias2 = new ColumnAlias();
		alias2.setColumnFamily("family");
		alias2.setQualifier("qualifier1");
		design2.setColumnAlias(alias2);

		ReportDesign design3 = new ReportDesign();
		ColumnAlias alias3 = new ColumnAlias();
		alias3.setColumnFamily("family1");
		alias3.setQualifier("qualifier");
		design3.setColumnAlias(alias3);

		ReportDesign design4 = new ReportDesign();
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("row");

		report.addDesign(design1);
		report.addDesign(design2);
		report.addDesign(design3);
		report.addDesign(design4);

		Scan scan = new Scan();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, scan);

		Map<byte[], NavigableSet<byte[]>> map = scan.getFamilyMap();
		for (byte[] family : map.keySet()) {
			System.out.println("Family is " + Bytes.toString(family));
			NavigableSet<byte[]> set = map.get(family);
			for (byte[] col : set) {
				System.out.println("Column is " + Bytes.toString(col));
			}
		}

		byte[] familyBytes = Bytes.toBytes("family");
		byte[] family1Bytes = Bytes.toBytes("family1");

		assertEquals(2, map.size());
		assertTrue(map.containsKey(familyBytes));
		assertTrue(map.containsKey(family1Bytes));

		assertEquals(2, map.get(familyBytes).size());
		assertEquals(1, map.get(family1Bytes).size());

		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier")));
		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier1")));
		assertTrue(map.get(familyBytes).contains(Bytes.toBytes("qualifier")));

	}

	@Test
	public void testSetSelectedColumnsScanNoDesign() {
		Report report = new Report();

		Scan scan = new Scan();
		QueryExecutor exec = new QueryExecutor(null);
		exec.setSelectedColumns(report, scan);

		Map<byte[], NavigableSet<byte[]>> map = scan.getFamilyMap();
		for (byte[] family : map.keySet()) {
			System.out.println("Family is " + Bytes.toString(family));
			NavigableSet<byte[]> set = map.get(family);
			for (byte[] col : set) {
				System.out.println("Column is " + Bytes.toString(col));
			}
		}
		assertEquals(0, map.size());
	}

	@Test
	public void testIsGetOperation() throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);

		mapping.addRowAlias(rAlias);
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isGetOperation(report, mapping));

	}

	@Test
	public void testIsGetOperationWithColFilters() throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();

		FilterType filter1 = new FilterType();
		filter1.setType("Equals");

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		mapping.addColumnAlias(cAlias);

		ColumnFilter colFilter = new ColumnFilter();
		FilterType filterType = new FilterType();
		filterType.setType("Not Equals");

		colFilter.setFilterType(filterType);
		colFilter.setColumnAlias(cAlias);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);

		ArrayList<ColumnFilter> colFilters = new ArrayList<ColumnFilter>();
		colFilters.add(colFilter);

		report.setRowAliasFilters(filters);
		report.setColumnFilters(colFilters);

		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isGetOperation(report, mapping));

	}

	@Test
	public void testNotIsGetOperationWithColFilters() throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();

		FilterType filter1 = new FilterType();
		filter1.setType("Not Equals");

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		mapping.addColumnAlias(cAlias);

		ColumnFilter colFilter = new ColumnFilter();
		FilterType filterType = new FilterType();
		filterType.setType("Equals");

		colFilter.setFilterType(filterType);
		colFilter.setColumnAlias(cAlias);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);

		ArrayList<ColumnFilter> colFilters = new ArrayList<ColumnFilter>();
		colFilters.add(colFilter);

		report.setRowAliasFilters(filters);
		report.setColumnFilters(colFilters);

		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(!exec.isGetOperation(report, mapping));
	}

	@Test
	public void testIsGetOperationMultipleRowAliases() throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		mapping.addColumnAlias(cAlias);

		ColumnFilter colFilter = new ColumnFilter();
		FilterType filterType = new FilterType();
		filterType.setType("Not Equals");
		colFilter.setFilterType(filterType);
		colFilter.setColumnAlias(cAlias);
		ArrayList<ColumnFilter> colFilters = new ArrayList<ColumnFilter>();
		colFilters.add(colFilter);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();

		for (int i = 0; i < 5; ++i) {

			FilterType filter1 = new FilterType();
			filter1.setType("Equals");
			RowAlias rAlias = new RowAlias();
			rAlias.setAlias("rowkey" + i);
			mapping.addRowAlias(rAlias);

			RowAliasFilter rowFilter = new RowAliasFilter();
			rowFilter.setFilterType(filter1);
			rowFilter.setRowAlias(rAlias);

			filters.add(rowFilter);
		}
		report.setRowAliasFilters(filters);
		report.setColumnFilters(colFilters);

		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isGetOperation(report, mapping));

	}

	@Test
	public void testIsGetOperationMultipleRowAliasesSomeNotEquals()
			throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		mapping.addColumnAlias(cAlias);

		ColumnFilter colFilter = new ColumnFilter();
		FilterType filterType = new FilterType();
		filterType.setType("Not Equals");
		colFilter.setFilterType(filterType);
		colFilter.setColumnAlias(cAlias);
		ArrayList<ColumnFilter> colFilters = new ArrayList<ColumnFilter>();
		colFilters.add(colFilter);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();

		for (int i = 0; i < 5; ++i) {

			FilterType filter1 = new FilterType();
			String rowFilterType = "Equals";
			if (i % 2 == 0) {
				rowFilterType = "Less Than";
			}
			filter1.setType(rowFilterType);
			RowAlias rAlias = new RowAlias();
			rAlias.setAlias("rowkey" + i);
			mapping.addRowAlias(rAlias);

			RowAliasFilter rowFilter = new RowAliasFilter();
			rowFilter.setFilterType(filter1);
			rowFilter.setRowAlias(rAlias);

			filters.add(rowFilter);
		}
		report.setRowAliasFilters(filters);
		report.setColumnFilters(colFilters);

		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(!exec.isGetOperation(report, mapping));
	}

	@Test
	public void testGetRowForRowFilterinDifferentOrder() throws CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();

		ValueType longType = new ValueType(1l, new Datastore(), "Long",
				"java.lang.Long", true);
		ValueType stringType = new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false);

		RowAlias rAliasLong = new RowAlias();
		rAliasLong.setAlias("rowkey");
		rAliasLong.setId(1);
		rAliasLong.setLength(8);
		rAliasLong.setMapping(mapping);
		rAliasLong.setValueType(longType);

		RowAlias rAliasString = new RowAlias();
		rAliasString.setAlias("rowkey");
		rAliasString.setId(2);
		rAliasString.setLength(4);
		rAliasString.setMapping(mapping);
		rAliasString.setValueType(stringType);

		mapping.addRowAlias(rAliasLong);
		mapping.addRowAlias(rAliasString);

		FilterType filter1 = new FilterType();
		filter1.setType("Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(1);
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAliasString);
		rowFilter1.setValue("String");

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(filter1);
		rowFilter2.setRowAlias(rAliasLong);
		rowFilter2.setValue("123");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);

		report.setRowAliasFilters(filters);
		byte[] row = QueryExecutor.getGetRow(report, mapping);
		byte[] expected = Bytes.add(Bytes.toBytes(123l),
				Bytes.toBytes("String"));

		assertTrue(Bytes.equals(expected, row));
	}

	/*
	@Test
	public void testGetIndexOfCompareFilterSingleFilterWrongOperand()
			throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(0);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec.getIndexOfCompareFilter(filterList, CompareOp.EQUAL);
		assertEquals(-1, result);
	}

	@Test
	public void testGetIndexOfCompareFilterSingleFilterCorrectOperand()
			throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(0);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setReport(new Report());

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(0, result);
	}

	@Test
	public void testGetIndexOfCompareTwoFiltersWrongOperand()
			throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(0);
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter);
		filterList.add(rowFilter1);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec.getIndexOfCompareFilter(filterList, CompareOp.LESS);
		assertEquals(-1, result);
	}

	@Test
	public void testGetIndexOfCompareFilterTwoFiltersCorrectOperand()
			throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(0);
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter);
		filterList.add(rowFilter1);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(1, result);
	}

	@Test
	public void testGetIndexOfCompareFilterTwoFiltersUnordered()
			throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(0);
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(1, result);
	}

	@Test
	public void testGetIndexOfCompareFilterTwoFiltersWrongOperand()
			throws CruxException {

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter.setReport(new Report());
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter1.setReport(new Report());
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowFilter1.setRowAlias(rowAlias1);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec.getIndexOfCompareFilter(filterList, CompareOp.LESS);
		assertEquals(-1, result);
	}

	@Test
	public void testGetIndexOfCompareFilterThreeFiltersCorrectOperand()
			throws CruxException {

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Equals"));
		rowFilter1.setReport(new Report());
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowFilter1.setRowAlias(rowAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(3);
		rowFilter2.setFilterType(new FilterType(2, "Greater Than"));
		rowFilter2.setReport(new Report());
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(3);
		rowFilter2.setRowAlias(rowAlias2);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter2);
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(2, result);
	}

	@Test
	public void testGetIndexOfCompareFilterThreeFiltersCorrectOperands()
			throws CruxException {

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Greater Than"));
		rowFilter1.setReport(new Report());
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowFilter1.setRowAlias(rowAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(3);
		rowFilter2.setFilterType(new FilterType(2, "Greater Than"));
		rowFilter2.setReport(new Report());
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(3);
		rowFilter2.setRowAlias(rowAlias2);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter2);
		filterList.add(rowFilter);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(2, result);
	}

	@Test
	public void testGetIndexOfCompareFilterThreeFiltersUnorderedOperand()
			throws CruxException {

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(3);
		rowFilter1.setFilterType(new FilterType(1, "Equals"));
		rowFilter1.setReport(new Report());
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(3);
		rowFilter1.setRowAlias(rowAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(5);
		rowFilter2.setFilterType(new FilterType(2, "Greater Than"));
		rowFilter2.setReport(new Report());
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(5);
		rowFilter2.setRowAlias(rowAlias2);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter);
		filterList.add(rowFilter2);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec
				.getIndexOfCompareFilter(filterList, CompareOp.GREATER);
		assertEquals(2, result);
	}

	@Test
	public void testGetIndexOfCompareFilterThreeFiltersWrongOperand()
			throws CruxException {

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowFilter.setRowAlias(rowAlias);

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(3);
		rowFilter1.setFilterType(new FilterType(1, "Equals"));
		rowFilter1.setReport(new Report());
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(3);
		rowFilter1.setRowAlias(rowAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(5);
		rowFilter2.setFilterType(new FilterType(2, "Greater Than"));
		rowFilter2.setReport(new Report());
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(5);
		rowFilter2.setRowAlias(rowAlias2);

		ArrayList<RowAliasFilter> filterList = new ArrayList<RowAliasFilter>();
		filterList.add(rowFilter1);
		filterList.add(rowFilter);
		filterList.add(rowFilter2);

		QueryExecutor exec = new QueryExecutor(null);
		int result = exec.getIndexOfCompareFilter(filterList, CompareOp.LESS);
		assertEquals(-1, result);
	}
	*/
	
	//isRangeScan(ArrayList<RowAliasFilter> rowFilters, Map<String, RowAlias> rowAliasesMap,
	
	@Test
	public void testIsRangeScanSimpleSingleRowAliasGt() throws CruxException{
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Greater Than Equals"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Greater Than Equals"));
	}
	
	@Test
	public void testIsRangeScanSimpleSingleRowAliasLt() throws CruxException{
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanCompositeEqLt() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Equals"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanCompositeEqLtLeft() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Less Than"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanCompositeEqLtLeftRightMissing() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanCompositeEqLtRightLeftMissing() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(1);
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias1);		
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		QueryExecutor exec = new QueryExecutor(null);
		assertFalse(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanComposite3EqLtRight() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(0);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(1, "Equals"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);
		
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		rowAlias2.setAlias("alias2");
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(new FilterType(2, "Less Than"));
		rowFilter2.setReport(new Report());
		rowFilter2.setRowAlias(rowAlias2);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		rowAliasesMap.put(rowAlias2.getAlias(), rowAlias2);
		
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanComposite3EqLtMid() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(0);
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(2, "Less Than"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);
		
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		rowAlias2.setAlias("alias2");
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(new FilterType(2, "Equals"));
		rowFilter2.setReport(new Report());
		rowFilter2.setRowAlias(rowAlias2);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		rowAliasesMap.put(rowAlias2.getAlias(), rowAlias2);
		
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	@Test
	public void testIsRangeScanComposite3LtRight() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		rowAlias2.setAlias("alias2");
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(new FilterType(2, "Less Than"));
		rowFilter2.setReport(new Report());
		rowFilter2.setRowAlias(rowAlias2);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter2);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		rowAliasesMap.put(rowAlias2.getAlias(), rowAlias2);
		
		QueryExecutor exec = new QueryExecutor(null);
		assertFalse(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	
	@Test
	public void testIsRangeScanComposite3EqLtLeft() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setId(0);
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(new Report());
		rowFilter.setRowAlias(rowAlias);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias1");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(2);
		rowFilter1.setFilterType(new FilterType(2, "Equals"));
		rowFilter1.setReport(new Report());
		rowFilter1.setRowAlias(rowAlias1);
		
		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setId(2);
		rowAlias2.setAlias("alias2");
		
		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(new FilterType(2, "Equals"));
		rowFilter2.setReport(new Report());
		rowFilter2.setRowAlias(rowAlias2);
		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rowAlias.getAlias(), rowAlias);
		rowAliasesMap.put(rowAlias1.getAlias(), rowAlias1);
		rowAliasesMap.put(rowAlias2.getAlias(), rowAlias2);
		
		QueryExecutor exec = new QueryExecutor(null);
		assertTrue(exec.isRangeScan(filters, rowAliasesMap, "Less Than"));
	}
	
	


	@Test
	public void testGetRowBytes() throws CruxException {
		Mapping mapping = new Mapping();

		ValueType longType = new ValueType(1l, new Datastore(), "Long",
				"java.lang.Long", true);
		ValueType stringType = new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false);

		RowAlias rAliasLong = new RowAlias();
		rAliasLong.setAlias("rowkey");
		rAliasLong.setId(1);
		rAliasLong.setLength(8);
		rAliasLong.setMapping(mapping);
		rAliasLong.setValueType(longType);

		RowAlias rAliasString = new RowAlias();
		rAliasString.setAlias("rowkey");
		rAliasString.setId(2);
		rAliasString.setLength(4);
		rAliasString.setMapping(mapping);
		rAliasString.setValueType(stringType);

		mapping.addRowAlias(rAliasLong);
		mapping.addRowAlias(rAliasString);

		FilterType filter1 = new FilterType();
		filter1.setType("equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(1);
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAliasString);
		rowFilter1.setValue("String");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);

		byte[] row = QueryExecutor.getRowBytes(filters, mapping);
		byte[] expected = Bytes.toBytes("String");

		assertTrue(Bytes.equals(expected, row));
	}
	
	@Test
	public void testGetRangeFiltersSingleRowAliasGtFilter() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAliases.put("Alias", rowAlias);
		rowAlias.setAlias("Alias");
		rowAlias.setId(1l);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		assertTrue(range.contains(rowFilter));
		assertTrue(range.getGreaterRowFilters().size() == 1);
		assertNull(range.getLesserRowFilters());
	}

	
	@Test
	public void testGetRangeFiltersSingleRowAliasLtGtFilter() throws CruxException {
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);

		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setId(1l);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAlias.setAlias("Alias1");
		rowAlias.setId(1l);
		mapping.addRowAlias(rowAlias);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
	
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("stop");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias);
		rowFilter1.setValue("start");

		rowFilterList.add(rowFilter);
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);

		assertTrue(range.contains(rowFilter));
		assertTrue(range.contains(rowFilter1));
		assertTrue(range.getGreaterRowFilters().size() == 1);
		assertTrue(range.getLesserRowFilters().size() == 1);

	}
	 
	@Test
	public void testGetRangeFiltersSingleRowAliasLtFilter() throws CruxException {
		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAliases.put("Alias", rowAlias);
		rowAlias.setAlias("Alias");

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(2, "Less Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);

		assertTrue(range.contains(rowFilter));
		assertTrue(range.getLesserRowFilters().size() == 1);
		assertNull(range.getGreaterRowFilters());
	}

	@Test
	public void testGetRangeFiltersDoubleRowAliasLtFilter() throws CruxException {
		RowAlias rowAlias = new RowAlias();		
		rowAlias.setId(1);
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setAlias("Alias");
		rowAlias.setLength(5);
				
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setLength(5);
		rowAlias1.setAlias("Alias1");
		
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		
		rowAlias.setMapping(mapping);
		rowAlias1.setMapping(mapping);
		
		rowAliases.put("Alias", rowAlias);
		rowAliases.put("Alias1", rowAlias1);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		assertTrue(range.contains(rowFilter));
		assertTrue(range.contains(rowFilter1));
		assertTrue(range.getLesserRowFilters().size() == 2);
		assertNull(range.getGreaterRowFilters());
	}

	@Test
	public void testGetRangeFiltersDoubleRowAliasGtFilter() throws CruxException {
		RowAlias rowAlias = new RowAlias();		
		rowAlias.setId(1);
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setAlias("Alias");
		rowAlias.setLength(5);
				
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setLength(5);
		rowAlias1.setAlias("Alias1");
		
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		
		rowAlias.setMapping(mapping);
		rowAlias1.setMapping(mapping);
		
		rowAliases.put("Alias", rowAlias);
		rowAliases.put("Alias1", rowAlias1);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Greater Than Equals"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		assertTrue(range.contains(rowFilter));
		assertTrue(range.contains(rowFilter1));
		assertTrue(range.getGreaterRowFilters().size() == 2);
		assertNull(range.getLesserRowFilters());
	}
	
	@Test
	public void testGetRangeFiltersDoubleRowAliasGtFilterRight() throws CruxException {
		RowAlias rowAlias = new RowAlias();		
		rowAlias.setId(1);
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setAlias("Alias");
		rowAlias.setLength(5);
				
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setLength(5);
		rowAlias1.setAlias("Alias1");
		
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		
		rowAlias.setMapping(mapping);
		rowAlias1.setMapping(mapping);
		
		rowAliases.put("Alias", rowAlias);
		rowAliases.put("Alias1", rowAlias1);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		assertTrue(range.contains(rowFilter));
		assertTrue(range.contains(rowFilter1));
		assertTrue(range.getGreaterRowFilters().size() == 2);
		assertNull(range.getLesserRowFilters());
	}
	
	@Test
	public void testGetRangeFiltersDoubleRowAliasGtFilterRightMissingFilterLeft() throws CruxException {
		RowAlias rowAlias = new RowAlias();		
		rowAlias.setId(1);
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setAlias("Alias");
		rowAlias.setLength(5);
				
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setLength(5);
		rowAlias1.setAlias("Alias1");
		
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		
		rowAlias.setMapping(mapping);
		rowAlias1.setMapping(mapping);
		
		rowAliases.put("Alias", rowAlias);
		rowAliases.put("Alias1", rowAlias1);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		/*rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);*/
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		assertNull(range.getGreaterRowFilters());
		assertNull(range.getLesserRowFilters());
	}



	@Test
	public void testGetRangeFiltersDoubleRowAliasWrongFilter()
			throws CruxException {
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);
		
		RowAlias rowAlias = new RowAlias();
		RowAlias rowAlias1 = new RowAlias();
		
		rowAlias1.setId(1);
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setMapping(mapping);
		rowAlias1.setLength(5);
		rowAlias1.setAlias("Alias1");
		
		rowAlias.setId(2);
		rowAlias.setLength(5);
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setAlias("Alias");
		rowAlias.setMapping(mapping);
		
		rowAliases.put("Alias", rowAlias);
		rowAliases.put("Alias1", rowAlias1);

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(2, "Greater Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("start");
		rowFilterList.add(rowFilter);
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Less Than"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		RangeFilters range = exec.getRangeFilters(report, mapping);
		
		assertNull(range.getGreaterRowFilters());
		assertNull(range.getLesserRowFilters());
	}
	
	@Test
	public void testGetRowBytesMultiple() throws CruxException {
		Mapping mapping = new Mapping();

		ValueType longType = new ValueType(1l, new Datastore(), "Long",
				"java.lang.Long", true);
		ValueType stringType = new ValueType(2l, new Datastore(), "String",
				"java.lang.String", false);

		RowAlias rAliasLong = new RowAlias();
		rAliasLong.setAlias("rowkey");
		rAliasLong.setId(1);
		rAliasLong.setLength(8);
		rAliasLong.setMapping(mapping);
		rAliasLong.setValueType(longType);

		RowAlias rAliasString = new RowAlias();
		rAliasString.setAlias("rowkey");
		rAliasString.setId(2);
		rAliasString.setLength(4);
		rAliasString.setMapping(mapping);
		rAliasString.setValueType(stringType);

		mapping.addRowAlias(rAliasLong);
		mapping.addRowAlias(rAliasString);

		FilterType filter1 = new FilterType();
		filter1.setType("equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setId(1);
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAliasString);
		rowFilter1.setValue("String");
		
		FilterType filter2 = new FilterType();
		filter2.setType("equals");

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setId(2);
		rowFilter2.setFilterType(filter2);
		rowFilter2.setRowAlias(rAliasLong);
		rowFilter2.setValue("123");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);

		byte[] row = QueryExecutor.getRowBytes(filters, mapping);
		byte[] expected = Bytes.add(Bytes.toBytes(new Long(123)), Bytes.toBytes("String"));

		assertTrue(Bytes.equals(expected, row));
	}
	
	@Test
	public void testSetRangeScan() throws CruxException {
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);

		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setId(1l);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAlias.setAlias("Alias1");

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("stop");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias);
		rowFilter1.setValue("start");

		rowFilterList.add(rowFilter);
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		
		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		lesserFilters.add(rowFilter);
		
		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter1);
		
		RangeFilters rangeFilters = new RangeFilters(lesserFilters, greaterFilters);
		
		Scan scan = new Scan();
		exec.setRangeScan(scan, mapping, rangeFilters);

		assertTrue(Bytes.equals(scan.getStartRow(), Bytes.toBytes("start")));
		assertTrue(Bytes.equals(scan.getStopRow(), Bytes.toBytes("stop")));
	}
	
	@Test
	public void testSetRangeScanOnlyLt() throws CruxException {
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);

		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setId(1l);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAlias.setAlias("Alias1");

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Less Than"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("stop");

		
		rowFilterList.add(rowFilter);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		
		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		lesserFilters.add(rowFilter);
		
		RangeFilters rangeFilters = new RangeFilters(lesserFilters, null);
		
		Scan scan = new Scan();
		exec.setRangeScan(scan, mapping, rangeFilters);

		assertTrue(Bytes.equals(scan.getStartRow(), Bytes.toBytes("")));
		assertTrue(Bytes.equals(scan.getStopRow(), Bytes.toBytes("stop")));
	}
	
	@Test
	public void testSetRangeScanOnlyGt() throws CruxException {
		Mapping mapping = new Mapping();
		Map<String, RowAlias> rowAliases = new TreeMap<String, RowAlias>();
		mapping.setRowAlias(rowAliases);

		RowAlias rowAlias = new RowAlias();
		rowAlias.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias.setId(1l);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(4);
		rowAlias.setAlias("Alias1");
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setValueType(new ValueType(1l, new Datastore(), "String",
				"java.lang.String", false));
		rowAlias1.setId(2l);
		rowAlias.setMapping(mapping);
		rowAlias.setLength(5);
		rowAlias.setAlias("Alias2");

		Report report = new Report();
		ArrayList<RowAliasFilter> rowFilterList = new ArrayList<RowAliasFilter>();

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(new FilterType(1, "Equals"));
		rowFilter.setReport(report);
		rowFilter.setRowAlias(rowAlias);
		rowFilter.setValue("stop");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(new FilterType(2, "Greater Than Equals"));
		rowFilter1.setReport(report);
		rowFilter1.setRowAlias(rowAlias1);
		rowFilter1.setValue("start");

		rowFilterList.add(rowFilter);
		rowFilterList.add(rowFilter1);
		report.setRowAliasFilters(rowFilterList);

		QueryExecutor exec = new QueryExecutor(null);
		
		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter);
		greaterFilters.add(rowFilter1);
		
		RangeFilters rangeFilters = new RangeFilters(null, greaterFilters);
		
		Scan scan = new Scan();
		exec.setRangeScan(scan, mapping, rangeFilters);

		assertTrue(Bytes.equals(scan.getStartRow(), Bytes.toBytes("stopstart")));
		assertTrue(Bytes.equals(scan.getStopRow(), Bytes.toBytes("")));
	}
	

}
