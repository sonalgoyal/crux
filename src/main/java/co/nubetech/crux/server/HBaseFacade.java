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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.util.CruxException;
import co.nubetech.crux.util.Utility;

public class HBaseFacade {

	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.server.HBaseFacade.class);

	private HBaseConnectionPool hbaseConnectionPool;

	public HBaseFacade(HBaseConnectionPool hbaseConnectionPool) {
		this.hbaseConnectionPool = hbaseConnectionPool;
	}

	private HBaseAdmin getHBaseAdmin(Connection conn) throws CruxException {
		logger.debug("Trying to get HBaseAdmin " + conn);
		Configuration conf = Utility.getConfiguration(conn);
		HBaseAdmin admin;
		try {
			admin = new HBaseAdmin(conf);
		} catch (MasterNotRunningException e) {
			throw new CruxException(e);
		} catch (ZooKeeperConnectionException e) {
			throw new CruxException(e);
		}
		logger.debug("Got hbase admin " + admin);
		return admin;
	}

	/**
	 * method used to verify connection to the hbase server
	 * 
	 * @throws CruxException
	 */
	public boolean isValidConnection(Connection conn) {
		logger.debug("Testing valid connection for " + conn);
		HBaseAdmin admin;
		boolean isMasterRunning;

		try {
			admin = getHBaseAdmin(conn);
			isMasterRunning = true;
			admin = null;
		} catch (CruxException e) {
			isMasterRunning = false;
		}
		logger.debug("is valid connection " + isMasterRunning);
		return isMasterRunning;
	}

	/**
	 * 
	 * return list of tables from the hbase server
	 */
	public String[] getTableList(Connection conn) throws CruxException {
		logger.debug("Getting table list for " + conn);
		HBaseAdmin admin = getHBaseAdmin(conn);
		HTableDescriptor[] tableDescriptor;
		try {
			tableDescriptor = admin.listTables();
			
		} catch (IOException e) {
			throw new CruxException(e);
		}
		int length = tableDescriptor.length;
		ArrayList<String> tableList = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			String table = tableDescriptor[i].getNameAsString();
			logger.debug("Found table " + table);
			tableList.add(table);
		}
		String[] tableArray = new String[tableList.size()];
		admin = null;
		logger.debug("Returning table list " + tableArray);
		return tableList.toArray(tableArray);
	}

	public Collection<HColumnDescriptor> getColumnFamilies(Connection conn,
			String table) throws CruxException {
		logger.debug("Getting column families for " + conn + " and table " + table);
		HTablePool hTablePool = null;
		Collection<HColumnDescriptor> columnDescriptor = null;
		HTable hTable = null;
		try {
			hTablePool = (HTablePool) hbaseConnectionPool.borrowObject(conn);
			hTable = (HTable) hTablePool.getTable(table);
			columnDescriptor = hTable.getTableDescriptor().getFamilies();
			hTablePool.putTable(hTable);
			//hbaseConnectionPool.returnObject(conn, hTablePool);
			//hTable.close();
			logger.debug("Column Descriptor is " + columnDescriptor);
			return columnDescriptor;
		} catch (Exception e) {
			throw new CruxException(e);
		} finally {
			if (hTablePool != null) {
				try {
					if (hTable != null) {
						hTablePool.putTable(hTable);
					}
					hbaseConnectionPool.returnObject(conn, hTablePool);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					throw new CruxException(e1);
				}
			}
		}
			
	}

	public CruxScanner execute(Connection conn, Report report, Mapping mapping)
			throws CruxException {
		logger.debug("Executing report " + report );
		logger.debug(" for connection " + conn );
		logger.debug(" and mapping " + mapping);
		
		HTablePool hTablePool = null;
		HTableInterface hTable = null;
		try {
			hTablePool = (HTablePool) hbaseConnectionPool.borrowObject(conn);
			hTable = hTablePool
					.getTable(mapping.getTableName());
			QueryExecutor querier = new QueryExecutor(hTable);
			CruxScanner scanner = querier.execute(report, mapping);
			hbaseConnectionPool.returnObject(conn, hTablePool);
			//hTable.close();
			logger.debug("Execute returning the scanner " + scanner);
			return scanner;			
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new CruxException(e);
		} finally {
			if (hTablePool != null) {
				try {
					if (hTable != null) {
						hTablePool.putTable(hTable);
					}
					hbaseConnectionPool.returnObject(conn, hTablePool);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					throw new CruxException(e1);
				}
			}
			
		}

	}

	public CruxScanner executeTest(Connection conn, Report report,
			Mapping mapping) throws CruxException {
		try {

			KeyValue[] keys = new KeyValue[2];
			KeyValue key1 = new KeyValue(Bytes.toBytes("row1"),
					Bytes.toBytes("cf"), Bytes.toBytes("A"), 120l,
					KeyValue.Type.Put, Bytes.toBytes(10));
			KeyValue.COMPARATOR = new KeyValue.KVComparator();
			KeyValue key2 = new KeyValue(Bytes.toBytes("row1"),
					Bytes.toBytes("cf"), Bytes.toBytes("B"), 121l,
					KeyValue.Type.Put, Bytes.toBytes(20));
			keys[0] = key1;
			keys[1] = key2;

			GetScanner scanner = new GetScanner(new Result(keys));
			return scanner;

		} catch (Exception e) {
			throw new CruxException(e);
		}
	}
}
