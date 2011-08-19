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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
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

public class TestHBaseFilterFactory {
/*
	@Test
	public void testGetOffsetSingleAlias() {
		Mapping mapping = new Mapping();
		mapping.setTableName("tableDoesNotExist");

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(18);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Substring");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("row5");

		assertEquals(0, HBaseFilterFactory.getOffset(rowFilter, mapping));

	}

	@Test
	public void testGetOffsetTwosAlias() {
		Mapping mapping = new Mapping();
		mapping.setTableName("tableDoesNotExist");

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(18);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkey1");
		rAlias1.setLength(3);
		rAlias1.setValueType(valueType);

		mapping.addRowAlias(rAlias);
		mapping.addRowAlias(rAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Substring");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("row5");

		assertEquals(0, HBaseFilterFactory.getOffset(rowFilter, mapping));
		rowFilter.setRowAlias(rAlias1);
		assertEquals(18l, HBaseFilterFactory.getOffset(rowFilter, mapping));
	}

	@Test
	public void testGetColumnFilterLongGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterLongEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterLongNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterLongLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterLongLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterLongGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterIntegerGreaterThanEquals()
			throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterFloatGreaterThanEquals()
			throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterDoubleGreaterThanEquals()
			throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterShortGreaterThanEquals()
			throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("123");
		columnFilter.setColumnAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterBooleanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		BooleanComparator expectedComp = new BooleanComparator(true, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		BooleanComparator returnedComparator = (BooleanComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterBooleanNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		BooleanComparator expectedComp = new BooleanComparator(true, 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		BooleanComparator returnedComparator = (BooleanComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.LESS, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringStartsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Starts With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinaryPrefixComparator expectedComp = new SubBinaryPrefixComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinaryPrefixComparator returnedComparator = (SubBinaryPrefixComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringEndsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Ends With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubBinarySuffixComparator expectedComp = new SubBinarySuffixComparator(
				Bytes.toBytes("true"), 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubBinarySuffixComparator returnedComparator = (SubBinarySuffixComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringSubstring() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Substring");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubSubstringComparator expectedComp = new SubSubstringComparator(
				"true", 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubSubstringComparator returnedComparator = (SubSubstringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringPatternMatches() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"true", 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetColumnFilterStringPatternNotMatches()
			throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Not Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily("cf");
		alias.setQualifier("qualifier");
		alias.setValueType(valueType);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(type);
		columnFilter.setValue("true");
		columnFilter.setColumnAlias(alias);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"true", 0, -1);
		SingleColumnValueFilter expectedFilter = new SingleColumnValueFilter(
				Bytes.toBytes("cf"), Bytes.toBytes("qualifier"),
				CompareOp.NOT_EQUAL, expectedComp);
		expectedFilter.setFilterIfMissing(true);

		SingleColumnValueFilter filterReturned = (SingleColumnValueFilter) HBaseFilterFactory
				.getColumnFilter(columnFilter);
		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getFamily(),
				filterReturned.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter.getQualifier(),
				filterReturned.getQualifier()) == 0);
		assertTrue(filterReturned.getFilterIfMissing());
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterLongLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		LongComparator expectedComp = new LongComparator(123l, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		LongComparator returnedComparator = (LongComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterIntGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		IntComparator expectedComp = new IntComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		IntComparator returnedComparator = (IntComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterFloatGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(4);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		FloatComparator expectedComp = new FloatComparator(123, 0, 4);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		FloatComparator returnedComparator = (FloatComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterDoubleGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(8);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, 8);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		DoubleComparator returnedComparator = (DoubleComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortLessThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortGreaterThanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER_OR_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterShortGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(2);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("123");
		rowFilter.setRowAlias(alias);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, 2);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		ShortComparator returnedComparator = (ShortComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterBooleanEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("true");
		rowFilter.setRowAlias(alias);

		BooleanComparator expectedComp = new BooleanComparator(true, 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		BooleanComparator returnedComparator = (BooleanComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterBooleanNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("true");
		rowFilter.setRowAlias(alias);

		BooleanComparator expectedComp = new BooleanComparator(true, 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		BooleanComparator returnedComparator = (BooleanComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.LESS, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.GREATER,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringStartsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Starts With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinaryPrefixComparator expectedComp = new SubBinaryPrefixComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinaryPrefixComparator returnedComparator = (SubBinaryPrefixComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterStringSubstring() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Substring");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubSubstringComparator expectedComp = new SubSubstringComparator("abc",
				0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubSubstringComparator returnedComparator = (SubSubstringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterEndsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Ends With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinarySuffixComparator expectedComp = new SubBinarySuffixComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinarySuffixComparator returnedComparator = (SubBinarySuffixComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterPatternMatches() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"abc", 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFilterPatternNotMatches() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Not Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"abc", 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.NOT_EQUAL,
				expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetRowFiltersSimple() throws CruxException {
		FilterType type = new FilterType(1, "Greater Than");

		ValueType valueType = new ValueType(1, null, "java.lang.Long",
				"java.lang.Long", true);

		RowAlias alias = new RowAlias(1, null, valueType, 8, "alias");

		RowAliasFilter rowFilter = new RowAliasFilter(null, type, "123", alias);

		FilterType type1 = new FilterType(2, "Substring");

		ValueType valueType1 = new ValueType(2, null, "java.lang.String",
				"java.lang.String", false);

		RowAlias alias1 = new RowAlias(2, null, valueType1, 6, "alias1");

		RowAliasFilter rowFilter1 = new RowAliasFilter(null, type1, "Substr",
				alias1);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		rowAliases.put(alias1.getAlias(), alias1);

		mapping.setRowAlias(rowAliases);

		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter);

		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		RangeFilters rangeFilters = new RangeFilters(lesserFilters,
				greaterFilters);

		ArrayList<RowAliasFilter> rowAliasFilters = new ArrayList<RowAliasFilter>();
		rowAliasFilters.add(rowFilter);
		rowAliasFilters.add(rowFilter1);

		Report report = new Report();
		report.setRowAliasFilters(rowAliasFilters);

		SubSubstringComparator expectedComp = new SubSubstringComparator(
				"Substr", 8, 6);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);
		FilterList expectedFilterList = new FilterList();
		expectedFilterList.addFilter(expectedFilter);

		FilterList returnedFilterList = HBaseFilterFactory.getRowFilters(
				report, mapping, rangeFilters);

		assertTrue(returnedFilterList.getFilters().size() == 1);

		RowFilter returnedFilter = (RowFilter) returnedFilterList.getFilters()
				.get(0);
		SubSubstringComparator returnedComparator = (SubSubstringComparator) returnedFilter
				.getComparator();

		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
		assertEquals(expectedFilter.getOperator(), returnedFilter.getOperator());

	}

	@Test
	public void testGetComparatorLong() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		LongComparator expectedComp = new LongComparator(123l, 0, -1);
		LongComparator returnedComparator = (LongComparator) HBaseFilterFactory
				.getComparator(type, alias, "123", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorInteger() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		IntComparator expectedComp = new IntComparator(123, 0, -1);
		IntComparator returnedComparator = (IntComparator) HBaseFilterFactory
				.getComparator(type, alias, "123", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorShort() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		ShortComparator expectedComp = new ShortComparator((short) 123, 0, -1);
		ShortComparator returnedComparator = (ShortComparator) HBaseFilterFactory
				.getComparator(type, alias, "123", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorFloat() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Float");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		FloatComparator expectedComp = new FloatComparator(123, 0, -1);
		FloatComparator returnedComparator = (FloatComparator) HBaseFilterFactory
				.getComparator(type, alias, "123", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorDouble() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Double");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		DoubleComparator expectedComp = new DoubleComparator(123, 0, -1);
		DoubleComparator returnedComparator = (DoubleComparator) HBaseFilterFactory
				.getComparator(type, alias, "123", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorBoolean() throws CruxException {
		FilterType type = new FilterType();

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		BooleanComparator expectedComp = new BooleanComparator(true, 0, -1);
		BooleanComparator returnedComparator = (BooleanComparator) HBaseFilterFactory
				.getComparator(type, alias, "true", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinaryComparator returnedComparator = (SubBinaryComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringNotEquals() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Not Equals");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinaryComparator returnedComparator = (SubBinaryComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringGreaterThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Greater Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinaryComparator returnedComparator = (SubBinaryComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringLessThan() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Less Than");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinaryComparator returnedComparator = (SubBinaryComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringStartsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Starts With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinaryPrefixComparator expectedComp = new SubBinaryPrefixComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinaryPrefixComparator returnedComparator = (SubBinaryPrefixComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringEndsWith() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Ends With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubBinarySuffixComparator expectedComp = new SubBinarySuffixComparator(
				Bytes.toBytes("abc"), 0, -1);

		SubBinarySuffixComparator returnedComparator = (SubBinarySuffixComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringSubString() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Substring");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubSubstringComparator expectedComp = new SubSubstringComparator("abc",
				0, -1);

		SubSubstringComparator returnedComparator = (SubSubstringComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringPatterMatches() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"abc", 0, -1);

		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}

	@Test
	public void testGetComparatorStringPatternNotMatches() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Pattern Not Matches");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		alias.setLength(-1);

		SubRegexStringComparator expectedComp = new SubRegexStringComparator(
				"abc", 0, -1);

		SubRegexStringComparator returnedComparator = (SubRegexStringComparator) HBaseFilterFactory
				.getComparator(type, alias, "abc", 0, -1);
		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}
	
	@Test
	public void testGetColumnFilters() throws CruxException {
		FilterType type1 = new FilterType();
		type1.setType("Greater Than Equals");
		
		FilterType type2 = new FilterType();
		type2.setType("Equals");

		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.String");

		ColumnAlias alias1 = new ColumnAlias();
		alias1.setColumnFamily("cf1");
		alias1.setQualifier("qualifier");
		alias1.setValueType(valueType1);
		
		ColumnAlias alias2 = new ColumnAlias();
		alias2.setColumnFamily("cf2");
		alias2.setQualifier("qualifier");
		alias2.setValueType(valueType2);

		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setFilterType(type1);
		columnFilter1.setValue("123");
		columnFilter1.setColumnAlias(alias1);
		
		ColumnFilter columnFilter2 = new ColumnFilter();
		columnFilter2.setFilterType(type2);
		columnFilter2.setValue("123");
		columnFilter2.setColumnAlias(alias2);
		
		ArrayList<ColumnFilter> columnFilters = new ArrayList<ColumnFilter>();
		columnFilters.add(columnFilter1);
		columnFilters.add(columnFilter2);
		
		Report report = new Report();
		report.setColumnFilters(columnFilters);
		
		
		LongComparator expectedComp1 = new LongComparator(123l, 0, -1);
		SingleColumnValueFilter expectedFilter1 = new SingleColumnValueFilter(
				Bytes.toBytes("cf1"), Bytes.toBytes("qualifier"),
				CompareOp.GREATER_OR_EQUAL, expectedComp1);
		expectedFilter1.setFilterIfMissing(true);
		
		SubBinaryComparator expectedComp2 = new SubBinaryComparator(Bytes.toBytes("123"), 0, -1);
		SingleColumnValueFilter expectedFilter2 = new SingleColumnValueFilter(
				Bytes.toBytes("cf2"), Bytes.toBytes("qualifier"),
				CompareOp.EQUAL, expectedComp2);
		expectedFilter2.setFilterIfMissing(true);

		FilterList expectedFilterList = new FilterList();
		expectedFilterList.addFilter(expectedFilter1);
		expectedFilterList.addFilter(expectedFilter2);
		
		FilterList returnedFilterList = HBaseFilterFactory.getColumnFilters(report);
		
		assertTrue(returnedFilterList.getFilters().size()==2);
		
		SingleColumnValueFilter returnedFilter1 = (SingleColumnValueFilter) returnedFilterList.getFilters().get(0);
		LongComparator returnedComparator1 = (LongComparator) returnedFilter1.getComparator();
		assertTrue(Bytes.compareTo(expectedComp1.getValue(),
				returnedComparator1.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter1.getFamily(),
				returnedFilter1.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter1.getQualifier(),
				returnedFilter1.getQualifier()) == 0);
		assertTrue(returnedFilter1.getFilterIfMissing());
		assertEquals(expectedFilter1.getOperator(), returnedFilter1.getOperator());
		assertEquals(expectedComp1.getLength(), returnedComparator1.getLength());
		assertEquals(expectedComp1.getOffset(), returnedComparator1.getOffset());
		
		SingleColumnValueFilter returnedFilter2 = (SingleColumnValueFilter) returnedFilterList.getFilters().get(1);
		SubBinaryComparator returnedComparator2 = (SubBinaryComparator) returnedFilter2.getComparator();
		assertTrue(Bytes.compareTo(expectedComp2.getValue(),
				returnedComparator2.getValue()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter2.getFamily(),
				returnedFilter2.getFamily()) == 0);
		assertTrue(Bytes.compareTo(expectedFilter2.getQualifier(),
				returnedFilter2.getQualifier()) == 0);
		assertTrue(returnedFilter2.getFilterIfMissing());
		assertEquals(expectedFilter2.getOperator(), returnedFilter2.getOperator());
		assertEquals(expectedComp2.getLength(), returnedComparator2.getLength());
		assertEquals(expectedComp2.getOffset(), returnedComparator2.getOffset());
	}
	
	@Test
	public void testGetRowFilterNoLength() throws CruxException {
		FilterType type = new FilterType();
		type.setType("Ends With");

		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");

		RowAlias alias = new RowAlias();
		alias.setAlias("alias");
		alias.setValueType(valueType);
		
		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(type);
		rowFilter.setValue("abc");
		rowFilter.setRowAlias(alias);

		SubBinarySuffixComparator expectedComp = new SubBinarySuffixComparator(
				Bytes.toBytes("abc"), 0, -1);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		mapping.setRowAlias(rowAliases);

		RowFilter filterReturned = HBaseFilterFactory.getRowFilter(rowFilter,
				mapping);
		SubBinarySuffixComparator returnedComparator = (SubBinarySuffixComparator) filterReturned
				.getComparator();

		assertTrue(Bytes.compareTo(expectedComp.getValue(),
				returnedComparator.getValue()) == 0);
		assertEquals(expectedFilter.getOperator(), filterReturned.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
	}
	
	@Test
	public void testGetRowFiltersEquals() throws CruxException {
		FilterType type = new FilterType(1, "Greater Than");

		ValueType valueType = new ValueType(1, null, "java.lang.Long",
				"java.lang.Long", true);

		RowAlias alias = new RowAlias(1, null, valueType, 8, "alias");

		RowAliasFilter rowFilter = new RowAliasFilter(null, type, "123", alias);

		FilterType type1 = new FilterType(2, "Equals");

		ValueType valueType1 = new ValueType(2, null, "java.lang.String",
				"java.lang.String", false);

		RowAlias alias1 = new RowAlias(2, null, valueType1, 9, "alias1");

		RowAliasFilter rowFilter1 = new RowAliasFilter(null, type1, "Eq Value ",
				alias1);

		Mapping mapping = new Mapping();
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(alias.getAlias(), alias);
		rowAliases.put(alias1.getAlias(), alias1);

		mapping.setRowAlias(rowAliases);

		ArrayList<RowAliasFilter> greaterFilters = new ArrayList<RowAliasFilter>();
		greaterFilters.add(rowFilter);
		greaterFilters.add(rowFilter1);

		ArrayList<RowAliasFilter> lesserFilters = new ArrayList<RowAliasFilter>();
		RangeFilters rangeFilters = new RangeFilters(lesserFilters,
				greaterFilters);

		ArrayList<RowAliasFilter> rowAliasFilters = new ArrayList<RowAliasFilter>();
		rowAliasFilters.add(rowFilter);
		rowAliasFilters.add(rowFilter1);

		Report report = new Report();
		report.setRowAliasFilters(rowAliasFilters);

		SubBinaryComparator expectedComp = new SubBinaryComparator(
				Bytes.toBytes("Eq Value "), 8, 9);
		RowFilter expectedFilter = new RowFilter(CompareOp.EQUAL, expectedComp);
		FilterList expectedFilterList = new FilterList();
		expectedFilterList.addFilter(expectedFilter);

		FilterList returnedFilterList = HBaseFilterFactory.getRowFilters(
				report, mapping, rangeFilters);

		assertTrue(returnedFilterList.getFilters().size() == 1);

		RowFilter returnedFilter = (RowFilter) returnedFilterList.getFilters()
				.get(0);
		SubBinaryComparator returnedComparator = (SubBinaryComparator) returnedFilter
				.getComparator();

		assertEquals(expectedComp.getOffset(), returnedComparator.getOffset());
		assertEquals(expectedFilter.getOperator(), returnedFilter.getOperator());
		assertEquals(expectedComp.getLength(), returnedComparator.getLength());
		

	}

*/
}
