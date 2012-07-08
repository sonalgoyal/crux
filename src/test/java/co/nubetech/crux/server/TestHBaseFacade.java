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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.PoolUtils;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.pool.HBaseConnectionPoolFactory;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxException;

public class TestHBaseFacade {
	private static final HBaseTestingUtility TEST_UTIL = new HBaseTestingUtility();
	// private static final HBaseRESTTestingUtility REST_TEST_UTIL = new
	// HBaseRESTTestingUtility();
	private static final String TABLE = "testTable";
	private static final byte[] COLUMN_1 = Bytes.toBytes("cf");
	private static final byte[] COLUMN_2 = Bytes.toBytes("cf1");
	private static final String TABLE_1 = "testTable1";
	private static final String TABLE_2 = "testTable2";
	private static final String TABLE_3 = "testTable3";
	private static final String TABLE_4 = "testTable4";
	private static final String TABLE_5 = "testTable5";
	private static Connection connection = null;
	private static HBaseConnectionPool pool = null;
	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.server.TestHBaseFacade.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TEST_UTIL.startMiniCluster(3);
		// REST_TEST_UTIL.startServletContainer(TEST_UTIL.getConfiguration());
		HBaseAdmin admin = TEST_UTIL.getHBaseAdmin();

		if (!admin.tableExists(TABLE)) {
			HTableDescriptor htd = new HTableDescriptor(TABLE);
			htd.addFamily(new HColumnDescriptor(COLUMN_1));
			htd.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd);
			HTable table = new HTable(TEST_UTIL.getConfiguration(), TABLE);

			for (long i = 0; i < 10; ++i) {
				Put put = new Put(Bytes.toBytes("row" + i));
				put.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				table.put(put);
			}
			table.flushCommits();
		}

		if (!admin.tableExists(TABLE_1)) {
			HTableDescriptor htd1 = new HTableDescriptor(TABLE_1);
			htd1.addFamily(new HColumnDescriptor(COLUMN_1));
			htd1.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd1);

			HTable table1 = new HTable(TEST_UTIL.getConfiguration(), TABLE_1);

			for (long i = 0; i < 10; ++i) {
				Put put1 = new Put(Bytes.toBytes(new Long(i)));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier"), Bytes.toBytes(i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier1"),
						Bytes.toBytes((int) i));

				table1.put(put1);
			}
			table1.flushCommits();
		}

		if (!admin.tableExists(TABLE_2)) {
			HTableDescriptor htd1 = new HTableDescriptor(TABLE_2);
			htd1.addFamily(new HColumnDescriptor(COLUMN_1));
			htd1.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd1);

			HTable table1 = new HTable(TEST_UTIL.getConfiguration(), TABLE_2);

			for (long i = 10; i < 20; ++i) {
				byte[] longBytes = Bytes.toBytes(new Long(i));
				byte[] intBytes = Bytes.toBytes((int) i);
				byte[] stringBytes = Bytes.toBytes("I am a String" + i);

				Put put1 = new Put(Bytes.add(longBytes, intBytes, stringBytes));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier1"),
						Bytes.toBytes("I am a sub" + i*2 + " and I am a long"));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier"), Bytes.toBytes(i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier1"),
						Bytes.toBytes((int) i));

				table1.put(put1);

				for (int k = 0; k < put1.getRow().length; ++k) {
					logger.debug("Entered " + put1.getRow()[k]);
				}
			}
			table1.flushCommits();
		}

		if (!admin.tableExists(TABLE_3)) {
			HTableDescriptor htd1 = new HTableDescriptor(TABLE_3);
			htd1.addFamily(new HColumnDescriptor(COLUMN_1));
			htd1.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd1);

			HTable table1 = new HTable(TEST_UTIL.getConfiguration(), TABLE_3);

			for (long i = 0; i < 10; ++i) {
				Put put1 = new Put(Bytes.toBytes((short) i));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier"), Bytes.toBytes(i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier1"),
						Bytes.toBytes((int) i));

				table1.put(put1);
			}
			table1.flushCommits();
		}

		if (!admin.tableExists(TABLE_4)) {
			HTableDescriptor htd1 = new HTableDescriptor(TABLE_4);
			htd1.addFamily(new HColumnDescriptor(COLUMN_1));
			htd1.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd1);

			HTable table1 = new HTable(TEST_UTIL.getConfiguration(), TABLE_4);

			for (long i = 10; i < 20; ++i) {
				byte[] longBytes = Bytes.toBytes(new Long(i));
				byte[] boleanBytes = Bytes.toBytes(true);

				Put put1 = new Put(Bytes.add(longBytes, boleanBytes));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier"), Bytes.toBytes(i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier1"),
						Bytes.toBytes((int) i));

				table1.put(put1);

				for (int k = 0; k < put1.getRow().length; ++k) {
					logger.debug("Entered " + put1.getRow()[k]);
				}
			}
			table1.flushCommits();
		}

		if (!admin.tableExists(TABLE_5)) {
			HTableDescriptor htd1 = new HTableDescriptor(TABLE_5);
			htd1.addFamily(new HColumnDescriptor(COLUMN_1));
			htd1.addFamily(new HColumnDescriptor(COLUMN_2));
			admin.createTable(htd1);

			HTable table1 = new HTable(TEST_UTIL.getConfiguration(), TABLE_5);

			for (long i = 1; i < 2; ++i) {
				Put put1 = new Put(Bytes.toBytes(true));
				put1.add(COLUMN_1, Bytes.toBytes("qualifier"),
						Bytes.toBytes("value" + i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier"), Bytes.toBytes(i));
				put1.add(COLUMN_2, Bytes.toBytes("qualifier1"),
						Bytes.toBytes((int) i));
				table1.put(put1);
			}

			table1.flushCommits();
		}

		KeyedPoolableObjectFactory factory = PoolUtils
				.synchronizedPoolableFactory(new HBaseConnectionPoolFactory());

		connection = new Connection();
		ConnectionProperty connectionProperty = new ConnectionProperty();
		connectionProperty.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		logger.debug("Port is: " + TEST_UTIL.getConfiguration().get("hbase.zookeeper.property.clientPort"));
		connectionProperty.setValue("localhost:"
				+ TEST_UTIL.getConfiguration().get("hbase.zookeeper.property.clientPort"));
		connection.addProperty(connectionProperty);

		pool = new HBaseConnectionPool(factory);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		connection = null;
		pool = null;

		// REST_TEST_UTIL.shutdownServletContainer();
		TEST_UTIL.shutdownMiniCluster();
	}

	@Test
	public void testIsValidConnection() throws IOException, CruxException {
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		boolean connectionAvailable = hbaseFacade.isValidConnection(connection);
		assertTrue(connectionAvailable);

	}

	@Test
	public void testIsValidConnectionForWrongValue() throws IOException {
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		Connection connection = new Connection();
		ConnectionProperty property = new ConnectionProperty();
		property.setProperty(CruxConstants.HBASE_ZOOKEEPER_PROPERTY);
		property.setValue("localhost:0000");
		connection.addProperty(property);
		boolean connectionAvailable = hbaseFacade.isValidConnection(connection);
		assertFalse(connectionAvailable);
	}

	@Test
	public void testGetTableList() throws CruxException {
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		String[] tableList = hbaseFacade.getTableList(connection);
		assertEquals(6, tableList.length);
		assertEquals(TABLE, tableList[0]);
		assertEquals(TABLE_1, tableList[1]);
		assertEquals(TABLE_2, tableList[2]);
		assertEquals(TABLE_3, tableList[3]);
		assertEquals(TABLE_4, tableList[4]);
		assertEquals(TABLE_5, tableList[5]);
	}

	@Test
	public void testGetColumnFamilies() throws CruxException {
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		ArrayList<HColumnDescriptor> columnList = new ArrayList<HColumnDescriptor>(
				hbaseFacade.getColumnFamilies(connection, TABLE));
		assertEquals(columnList.get(0).getNameAsString(), "cf");
		assertEquals(columnList.get(1).getNameAsString(), "cf1");
		assertEquals(columnList.size(), 2);
	}

	@Test(expected = CruxException.class)
	public void testGetColumnFamiliesTableDoesNotExist() throws CruxException {
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		ArrayList<HColumnDescriptor> columnList = new ArrayList<HColumnDescriptor>(
				hbaseFacade.getColumnFamilies(connection, "doesNotExistTable"));

	}

	@Test(expected = CruxException.class)
	public void testGetTableDoesNotExist() throws IOException, CruxException {
		Report report = new Report();
		report.setId(1l);
		Mapping mapping = new Mapping();
		mapping.setTableName("tableDoesNotExist");

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType);
		mapping.addColumnAlias(cAlias);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setReport(report);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		rowFilter.setValue("row5");

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);

	}

	@Test
	public void testGetSimpleStringRow() throws IOException, CruxException {
		Report report = new Report();
		report.setId(1l);
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType);
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setReport(report);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		rowFilter.setValue("row5");
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		logger.debug("Value is "
				+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
		assertEquals(
				"value5",
				new String(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		logger.debug("Result is " + scanResult);
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();
	}

	@Test
	public void testGetSimpleLongRow() throws IOException, CruxException {
		Report report = new Report();
		report.setId(1l);
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_1);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Long");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias.setValueType(valueType);
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setReport(report);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		rowFilter.setValue("4");
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		logger.debug("Value 1 is "
				+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
		logger.debug("Value 2 is "
				+ scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier")));

		assertEquals(
				"value4",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				4,
				Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				4,
				Bytes.toInt(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier1"))));

		logger.debug("Result is " + scanResult);
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();
	}

	@Test
	public void testGetSimpleShortRow() throws IOException, CruxException {
		Report report = new Report();
		report.setId(1l);
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_3);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(2);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Short");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		rowFilter.setValue("4");
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		logger.debug("Value 1 is "
				+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
		logger.debug("Value 2 is "
				+ scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier")));

		assertEquals(
				"value4",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				4,
				Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				4,
				Bytes.toInt(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier1"))));

		logger.debug("Result is " + scanResult);
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();
	}

	@Test
	public void testGetSimpleBooleanRow() throws IOException, CruxException {

		Report report = new Report();
		report.setId(1l);
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_5);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(1);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.Boolean");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);
		cAlias.setValueType(valueType);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType);
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		rowFilter.setValue("true");
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);

		logger.debug("Value 1 is "
				+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
		logger.debug("Value 2 is "
				+ scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier")));

		assertEquals(
				"value1",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				1,
				Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				1,
				Bytes.toInt(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier1"))));

		logger.debug("Result is " + scanResult);
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();
	}

	@Test
	public void testGetCompositeRowWithBoolean() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_4);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyBoolean");
		rAlias2.setLength(1);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Boolean");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		mapping.setRowAlias(rowAliases);


		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType1);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType1);
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter);
		rowFilter1.setRowAlias(rAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(filter);
		rowFilter2.setRowAlias(rAlias2);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		report.setRowAliasFilters(filters);

		rowFilter1.setValue("11");
		rowFilter2.setValue("true");

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		assertFalse(scanResult.isEmpty());
		logger.debug("Value 1 is "
				+ Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		logger.debug("Value 2 is "
				+ Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		logger.debug("Result is " + scanResult);

		assertEquals(
				11l,
				Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				11,
				Bytes.toInt(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier1"))));
		assertEquals(
				"value11",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();

	}

	@Test
	public void testGetCompositeRow() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);


		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setValueType(valueType1);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType1);
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter);
		rowFilter1.setRowAlias(rAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(filter);
		rowFilter2.setRowAlias(rAlias2);

		RowAliasFilter rowFilter3 = new RowAliasFilter();
		rowFilter3.setFilterType(filter);
		rowFilter3.setRowAlias(rAlias3);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		filters.add(rowFilter3);
		report.setRowAliasFilters(filters);

		rowFilter1.setValue("11");
		rowFilter2.setValue("11");
		rowFilter3.setValue("I am a String11");

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		assertFalse(scanResult.isEmpty());
		logger.debug("Value 1 is "
				+ Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		logger.debug("Value 2 is "
				+ Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		logger.debug("Result is " + scanResult);

		assertEquals(
				11l,
				Bytes.toLong(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier"))));
		assertEquals(
				11,
				Bytes.toInt(scanResult.getValue(COLUMN_2,
						Bytes.toBytes("qualifier1"))));
		assertEquals(
				"value11",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();

	}

	@Test
	public void testGetCompositeRowWithSelectedColumns() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		cAlias.setValueType(valueType1);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias.setValueType(valueType1);
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter);
		rowFilter1.setRowAlias(rAlias1);

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(filter);
		rowFilter2.setRowAlias(rAlias2);

		RowAliasFilter rowFilter3 = new RowAliasFilter();
		rowFilter3.setFilterType(filter);
		rowFilter3.setRowAlias(rAlias3);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		filters.add(rowFilter3);
		report.setRowAliasFilters(filters);

		rowFilter1.setValue("11");
		rowFilter2.setValue("11");
		rowFilter3.setValue("I am a String11");

		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof GetScanner);
		Result scanResult = scanner.next();
		assertNotNull(scanResult);
		assertFalse(scanResult.isEmpty());
		logger.debug("Value 1 is "
				+ Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		logger.debug("Value 2 is "
				+ scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		logger.debug("Result is " + scanResult);

		assertNull(scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		assertNull(scanResult.getValue(COLUMN_2, Bytes.toBytes("qualifier1")));
		assertEquals(
				"value11",
				Bytes.toString(scanResult.getValue(COLUMN_1,
						Bytes.toBytes("qualifier"))));
		while ((scanResult = scanner.next()) != null) {
			fail("more rows found than expected");
		}
		scanner.close();

	}
	
	@Test
	public void testRangeScanSimpleStringRowGreater() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Greater Than Equals");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("row5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(5, results.size());
		for (int i=5; i <10; ++i) {
			assertTrue(Bytes.equals(Bytes.toBytes("row" + i), results.get(i-5).getRow()));
		}
		scanner.close();
		
	}

	@Test
	public void testRangeScanSimpleStringRowLesser() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Less Than");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("row5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(5, results.size());
		for (int i=0; i <5; ++i) {
			assertTrue(Bytes.equals(Bytes.toBytes("row" + i), results.get(i).getRow()));
		}
		scanner.close();
	}
	
	@Test
	public void testRangeScanSimpleStringRowLesserGreater() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter = new FilterType();
		filter.setType("Less Than");
		rowFilter.setFilterType(filter);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("row7");
		
		RowAliasFilter rowFilter1 = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Greater Than Equals");
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAlias);
		rowFilter1.setValue("row5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		filters.add(rowFilter1);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(2, results.size());
		for (int i=5; i <7; ++i) {
			assertTrue(Bytes.equals(Bytes.toBytes("row" + i), results.get(i-5).getRow()));
		}
		scanner.close();
	}
	
	@Test
	public void testRangeScanCompositeRowWithSelectedColumns() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		
		LinkedHashMap<String, RowAlias> rowAliasesMap = new LinkedHashMap<String, RowAlias>();
		rowAliasesMap.put(rAlias1.getAlias(), rAlias1);
		rowAliasesMap.put(rAlias2.getAlias(), rAlias2);
		rowAliasesMap.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliasesMap);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Greater Than Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter);
		rowFilter1.setRowAlias(rAlias1);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		report.setRowAliasFilters(filters);

		rowFilter1.setValue("15");
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(5, results.size());
		for (int i=15; i <20; ++i) {
			byte[] longBytes = Bytes.toBytes(new Long(i));
			byte[] intBytes = Bytes.toBytes((int) i);
			byte[] stringBytes = Bytes.toBytes("I am a String" + i);

			assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-15).getRow()));
			byte[] column = results.get(i-15).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
			assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
			assertNull(results.get(i-15).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		}
		scanner.close();
	}
	
	@Test
	public void testScanCompositeRowWithSelectedColumns() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		cAlias.setValueType(valueType3);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType3);
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Greater Than");

		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setFilterType(filter);
		columnFilter1.setColumnAlias(cAlias);
		columnFilter1.setValue("value15");
		

		ArrayList<ColumnFilter> filters = new ArrayList<ColumnFilter>();
		filters.add(columnFilter1);
		report.setColumnFilters(filters);
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);			
		}
		assertEquals(4, results.size());
		for (int i=16; i <20; ++i) {
			byte[] longBytes = Bytes.toBytes(new Long(i));
			byte[] intBytes = Bytes.toBytes((int) i);
			byte[] stringBytes = Bytes.toBytes("I am a String" + i);

			assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-16).getRow()));
			byte[] column = results.get(i-16).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
			assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
			assertNull(results.get(i-16).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		}
		scanner.close();

	}
	
	@Test
	public void testScanCompositeRowWithSelectedColumnsDontExist() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		cAlias.setValueType(valueType3);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType3);
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Greater Than");

		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setFilterType(filter);
		columnFilter1.setColumnAlias(cAlias);
		columnFilter1.setValue("w51213");
		

		ArrayList<ColumnFilter> filters = new ArrayList<ColumnFilter>();
		filters.add(columnFilter1);
		report.setColumnFilters(filters);
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(0, results.size());	
		scanner.close();
	}
	
	@Test
	public void testScanCompositeRowWithSelectedColumnsSubstr() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		cAlias.setValueType(valueType3);
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		cAlias1.setValueType(valueType3);
		mapping.addColumnAlias(cAlias1);
		
		ColumnAlias cAlias2 = new ColumnAlias();
		cAlias2.setAlias("col");
		cAlias2.setColumnFamily("cf");
		cAlias2.setQualifier("qualifier1");		
		cAlias2.setValueType(valueType3);
		mapping.addColumnAlias(cAlias2);

		FilterType filter = new FilterType();
		filter.setType("Substring");

		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setFilterType(filter);
		columnFilter1.setColumnAlias(cAlias2);
		columnFilter1.setValue("sub2");		

		ArrayList<ColumnFilter> filters = new ArrayList<ColumnFilter>();
		filters.add(columnFilter1);
		report.setColumnFilters(filters);
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);
		ReportDesign design1 = new ReportDesign();
		design1.setColumnAlias(cAlias2);
		report.addDesign(design1);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(5, results.size());
		for (int i=10; i <15; ++i) {
			byte[] longBytes = Bytes.toBytes(new Long(i));
			byte[] intBytes = Bytes.toBytes((int) i);
			byte[] stringBytes = Bytes.toBytes("I am a String" + i);

			assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-10).getRow()));
			byte[] column = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
			byte[] column1 = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier1"));
			
			assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
			assertTrue(Bytes.equals(Bytes.toBytes("I am a sub" +i*2 + " and I am a long"), column1));
			assertNull(results.get(i-10).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		}		
		scanner.close();
	}
	
	@Test
	public void testRangeScanCompositeRowWithLongEquals() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		//add aliases in order
		LinkedHashMap<String, RowAlias> rowMap = new LinkedHashMap<String, RowAlias>();
		rowMap.put(rAlias1.getAlias(), rAlias1);
		rowMap.put(rAlias2.getAlias(), rAlias2);
		rowMap.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowMap);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		FilterType filter = new FilterType();
		filter.setType("Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter);
		rowFilter1.setRowAlias(rAlias1);

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		report.setRowAliasFilters(filters);

		rowFilter1.setValue("15");
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		scanner.close();
	}
	
	@Test
	public void testRangeScanCompositeRowWithLongGtEqIntEq() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		//add aliases in order
		LinkedHashMap<String, RowAlias> rowMap = new LinkedHashMap<String, RowAlias>();
		rowMap.put(rAlias1.getAlias(), rAlias1);
		rowMap.put(rAlias2.getAlias(), rAlias2);
		rowMap.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowMap);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		FilterType filter1 = new FilterType();
		filter1.setType("Greater Than Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAlias1);
		rowFilter1.setValue("15");

		FilterType filter2 = new FilterType();
		filter2.setType("Equals");

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(filter2);
		rowFilter2.setRowAlias(rAlias2);
		rowFilter2.setValue("15");

		
		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		report.setRowAliasFilters(filters);

		
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		scanner.close();
	}
	
	@Test
	public void testRangeScanCompositeRowWithLongGtEqStringEq() throws IOException,
			CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE_2);

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		//mapping.addRowAlias(rAlias1);

		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		//mapping.addRowAlias(rAlias2);

		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		//mapping.addRowAlias(rAlias3);
		//add aliases in order
		LinkedHashMap<String, RowAlias> rowMap = new LinkedHashMap<String, RowAlias>();
		rowMap.put(rAlias1.getAlias(), rAlias1);
		rowMap.put(rAlias2.getAlias(), rAlias2);
		rowMap.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowMap);
		
		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		cAlias.setQualifier("qualifier");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		FilterType filter1 = new FilterType();
		filter1.setType("Greater Than Equals");

		RowAliasFilter rowFilter1 = new RowAliasFilter();
		rowFilter1.setFilterType(filter1);
		rowFilter1.setRowAlias(rAlias1);
		rowFilter1.setValue("15");

		FilterType filter2 = new FilterType();
		filter2.setType("Equals");

		RowAliasFilter rowFilter2 = new RowAliasFilter();
		rowFilter2.setFilterType(filter2);
		rowFilter2.setRowAlias(rAlias3);
		rowFilter2.setValue("I am a String15");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter1);
		filters.add(rowFilter2);
		report.setRowAliasFilters(filters);
		
		ReportDesign design = new ReportDesign();
		design.setColumnAlias(cAlias);
		report.addDesign(design);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
// Need to re check	
//		assertEquals(1, results.size());
		scanner.close();
	}


	@Test
	public void testRangeScanSimpleStringRowRegex() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		//rAlias.setLength(8);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter = new FilterType();
		filter.setType("Pattern Matches");
		rowFilter.setFilterType(filter);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("r*1");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		//for (int i=5; i <7; ++i) {
			assertTrue(Bytes.equals(Bytes.toBytes("row1"), results.get(0).getRow()));
		//}
		scanner.close();
	}
	
	@Test
	public void testRangeScanCompositeRowRegex() throws IOException, CruxException {
			Report report = new Report();
			Mapping mapping = new Mapping();
			mapping.setTableName(TABLE_2);

			RowAlias rAlias1 = new RowAlias();
			rAlias1.setAlias("rowkeyLong");
			rAlias1.setLength(8);
			rAlias1.setId(1l);
			ValueType valueType1 = new ValueType();
			valueType1.setClassName("java.lang.Long");
			rAlias1.setValueType(valueType1);
			//mapping.addRowAlias(rAlias1);

			RowAlias rAlias2 = new RowAlias();
			rAlias2.setAlias("rowkeyInt");
			rAlias2.setLength(4);
			rAlias2.setId(2l);
			ValueType valueType2 = new ValueType();
			valueType2.setClassName("java.lang.Integer");
			rAlias2.setValueType(valueType2);
			//mapping.addRowAlias(rAlias2);

			RowAlias rAlias3 = new RowAlias();
			rAlias3.setAlias("rowkeyString");
			rAlias3.setId(3l);
			rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
			ValueType valueType3 = new ValueType();
			valueType3.setClassName("java.lang.String");
			rAlias3.setValueType(valueType3);
			//mapping.addRowAlias(rAlias3);
			//add aliases in order
			LinkedHashMap<String, RowAlias> rowMap = new LinkedHashMap<String, RowAlias>();
			rowMap.put(rAlias1.getAlias(), rAlias1);
			rowMap.put(rAlias2.getAlias(), rAlias2);
			rowMap.put(rAlias3.getAlias(), rAlias3);
			mapping.setRowAlias(rowMap);
			
			ColumnAlias cAlias = new ColumnAlias();
			cAlias.setAlias("col");
			cAlias.setColumnFamily("cf");
			cAlias.setQualifier("qualifier");
			mapping.addColumnAlias(cAlias);

			ColumnAlias cAlias1 = new ColumnAlias();
			cAlias1.setAlias("col1");
			cAlias1.setColumnFamily("cf1");
			mapping.addColumnAlias(cAlias1);
			
			ColumnAlias cAlias2 = new ColumnAlias();
			cAlias2.setAlias("col2");
			cAlias2.setColumnFamily("cf");
			cAlias2.setQualifier("qualifier1");
			mapping.addColumnAlias(cAlias2);

			RowAliasFilter rowFilter = new RowAliasFilter();
			FilterType filter = new FilterType();
			filter.setType("Pattern Not Matches");
			rowFilter.setFilterType(filter);
			rowFilter.setRowAlias(rAlias3);
			rowFilter.setValue(".*6|.*7");

			ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
			filters.add(rowFilter);
			report.setRowAliasFilters(filters);
			
			ReportDesign design = new ReportDesign();
			design.setColumnAlias(cAlias);
			report.addDesign(design);
			
			ReportDesign design1 = new ReportDesign();
			design1.setColumnAlias(cAlias2);
			report.addDesign(design1);

			HBaseFacade hbaseFacade = new HBaseFacade(pool);
			CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
			assertTrue(scanner instanceof ScanScanner);
			Result scanResult = null;
			ArrayList<Result> results = new ArrayList<Result>();
			while ((scanResult = scanner.next()) != null) {
				results.add(scanResult);
				logger.debug("Value is "
						+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
				logger.debug("Result is " + scanResult);
				
			}
			assertEquals(8, results.size());
			for (int i=10; i <16; ++i) {
					byte[] longBytes = Bytes.toBytes(new Long(i));
					byte[] intBytes = Bytes.toBytes((int) i);
					byte[] stringBytes = Bytes.toBytes("I am a String" + i);
	
					assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-10).getRow()));
					byte[] column = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
					byte[] column1 = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier1"));
					
					assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
					assertTrue(Bytes.equals(Bytes.toBytes("I am a sub" +i*2 + " and I am a long"), column1));
					assertNull(results.get(i-10).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
			}
			for (int i=18; i <20; ++i) {
				byte[] longBytes = Bytes.toBytes(new Long(i));
				byte[] intBytes = Bytes.toBytes((int) i);
				byte[] stringBytes = Bytes.toBytes("I am a String" + i);

				assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-12).getRow()));
				byte[] column = results.get(i-12).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
				byte[] column1 = results.get(i-12).getValue(COLUMN_1, Bytes.toBytes("qualifier1"));
				
				assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
				assertTrue(Bytes.equals(Bytes.toBytes("I am a sub" +i*2 + " and I am a long"), column1));
				assertNull(results.get(i-12).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
		}
			scanner.close();
	}
	
	
	@Test
	public void testRangeScanSimpleStringRowPattern() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(4);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Pattern Matches");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue(".*5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		assertTrue(Bytes.equals(Bytes.toBytes("row5"), results.get(0).getRow()));
		scanner.close();
		
	}
	
	@Test
	public void testRangeScanSimpleStringRowEndsWith() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(4);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Ends With");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		assertTrue(Bytes.equals(Bytes.toBytes("row5"), results.get(0).getRow()));
		scanner.close();
		
	}
	
	@Test
	public void testRangeScanSimpleStringRowEndsWithNoLength() throws IOException, CruxException {
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName(TABLE);

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		mapping.addRowAlias(rAlias);

		ColumnAlias cAlias = new ColumnAlias();
		cAlias.setAlias("col");
		cAlias.setColumnFamily("cf");
		mapping.addColumnAlias(cAlias);

		ColumnAlias cAlias1 = new ColumnAlias();
		cAlias1.setAlias("col1");
		cAlias1.setColumnFamily("cf1");
		mapping.addColumnAlias(cAlias1);

		RowAliasFilter rowFilter = new RowAliasFilter();
		FilterType filter1 = new FilterType();
		filter1.setType("Ends With");
		rowFilter.setFilterType(filter1);
		rowFilter.setRowAlias(rAlias);
		rowFilter.setValue("5");

		ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
		filters.add(rowFilter);
		report.setRowAliasFilters(filters);

		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
		assertTrue(scanner instanceof ScanScanner);
		Result scanResult = null;
		ArrayList<Result> results = new ArrayList<Result>();
		while ((scanResult = scanner.next()) != null) {
			results.add(scanResult);
			logger.debug("Value is "
					+ scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
			logger.debug("Result is " + scanResult);
			
		}
		assertEquals(1, results.size());
		assertTrue(Bytes.equals(Bytes.toBytes("row5"), results.get(0).getRow()));
		scanner.close();
		
	}
	
	@Test
    public void testRangeScanCompositeRowGreaterThanEq() throws IOException, CruxException {
            Report report = new Report();
            Mapping mapping = new Mapping();
            mapping.setTableName(TABLE_2);

            RowAlias rAlias1 = new RowAlias();
            rAlias1.setAlias("rowkeyLong");
            rAlias1.setLength(8);
            rAlias1.setId(1l);
            ValueType valueType1 = new ValueType();
            valueType1.setClassName("java.lang.Long");
            rAlias1.setValueType(valueType1);
            //mapping.addRowAlias(rAlias1);

            RowAlias rAlias2 = new RowAlias();
            rAlias2.setAlias("rowkeyInt");
            rAlias2.setLength(4);
            rAlias2.setId(2l);
            ValueType valueType2 = new ValueType();
            valueType2.setClassName("java.lang.Integer");
            rAlias2.setValueType(valueType2);
            //mapping.addRowAlias(rAlias2);

            RowAlias rAlias3 = new RowAlias();
            rAlias3.setAlias("rowkeyString");
            rAlias3.setId(3l);
            rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
            ValueType valueType3 = new ValueType();
            valueType3.setClassName("java.lang.String");
            rAlias3.setValueType(valueType3);
            //mapping.addRowAlias(rAlias3);
            //add aliases in order
            LinkedHashMap<String, RowAlias> rowMap = new LinkedHashMap<String, RowAlias>();
            rowMap.put(rAlias1.getAlias(), rAlias1);
            rowMap.put(rAlias2.getAlias(), rAlias2);
            rowMap.put(rAlias3.getAlias(), rAlias3);
            mapping.setRowAlias(rowMap);
            
            ColumnAlias cAlias = new ColumnAlias();
            cAlias.setAlias("col");
            cAlias.setColumnFamily("cf");
            cAlias.setQualifier("qualifier");
            mapping.addColumnAlias(cAlias);

            ColumnAlias cAlias1 = new ColumnAlias();
            cAlias1.setAlias("col1");
            cAlias1.setColumnFamily("cf1");
            mapping.addColumnAlias(cAlias1);
            
            ColumnAlias cAlias2 = new ColumnAlias();
            cAlias2.setAlias("col2");
            cAlias2.setColumnFamily("cf");
            cAlias2.setQualifier("qualifier1");
            mapping.addColumnAlias(cAlias2);

            RowAliasFilter rowFilter = new RowAliasFilter();
            FilterType filter = new FilterType();
            filter.setType("Greater Than Equals");
            rowFilter.setFilterType(filter);
            rowFilter.setRowAlias(rAlias3);
            rowFilter.setValue("I am a String17");

            ArrayList<RowAliasFilter> filters = new ArrayList<RowAliasFilter>();
            filters.add(rowFilter);
            report.setRowAliasFilters(filters);
            
            ReportDesign design = new ReportDesign();
            design.setColumnAlias(cAlias);
            report.addDesign(design);
            
            ReportDesign design1 = new ReportDesign();
            design1.setColumnAlias(cAlias2);
            report.addDesign(design1);

            HBaseFacade hbaseFacade = new HBaseFacade(pool);
            CruxScanner scanner = hbaseFacade.execute(connection, report, mapping);
            assertTrue(scanner instanceof ScanScanner);
            Result scanResult = null;
            ArrayList<Result> results = new ArrayList<Result>();
            while ((scanResult = scanner.next()) != null) {
                results.add(scanResult);
                logger.debug("Value is "
                        + scanResult.getValue(COLUMN_1, Bytes.toBytes("qualifier")));
                logger.debug("Result is " + scanResult);
                
            }
            assertEquals(3, results.size());
            /*for (int i=10; i <16; ++i) {
                    byte[] longBytes = Bytes.toBytes(new Long(i));
                    byte[] intBytes = Bytes.toBytes((int) i);
                    byte[] stringBytes = Bytes.toBytes("I am a String" + i);
    
                    assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-10).getRow()));
                    byte[] column = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
                    byte[] column1 = results.get(i-10).getValue(COLUMN_1, Bytes.toBytes("qualifier1"));
                    
                    assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
                    assertTrue(Bytes.equals(Bytes.toBytes("I am a sub" +i*2 + " and I am a long"), column1));
                    assertNull(results.get(i-10).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
            }
            for (int i=18; i <20; ++i) {
                byte[] longBytes = Bytes.toBytes(new Long(i));
                byte[] intBytes = Bytes.toBytes((int) i);
                byte[] stringBytes = Bytes.toBytes("I am a String" + i);

                assertTrue(Bytes.equals(Bytes.add(longBytes, intBytes, stringBytes), results.get(i-12).getRow()));
                byte[] column = results.get(i-12).getValue(COLUMN_1, Bytes.toBytes("qualifier"));
                byte[] column1 = results.get(i-12).getValue(COLUMN_1, Bytes.toBytes("qualifier1"));
                
                assertTrue(Bytes.equals(Bytes.toBytes("value" +i), column));
                assertTrue(Bytes.equals(Bytes.toBytes("I am a sub" +i*2 + " and I am a long"), column1));
                assertNull(results.get(i-12).getValue(COLUMN_2, Bytes.toBytes("qualifier")));
        }*/
            scanner.close();
    }




}