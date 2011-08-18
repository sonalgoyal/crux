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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class PopulateBseData {
	HTable hTable;

	public PopulateBseData() throws IOException {
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "localhost");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		hTable = new HTable(conf, "stockData1");
	}

	public void putRow(byte[] row, byte[] column, byte[] qaul, byte[] value)
			throws IOException {
		Put put = new Put(row);
		put.add(column, qaul, value);
		hTable.put(put);
	}

	public static void main(String... arg) throws IOException {
		PopulateBseData tester = new PopulateBseData();
		File folder = new File(arg[0]);
		File[] fileList = folder.listFiles();
		String columns = "price:open,price:high,price:low,price:close,stats:wap,stats:numShares,stats:numTrades,stats:turnOver,spread:highLow,spread:closeOpen";
		String[] columnList = columns.split(",");
		for (File file : fileList) {
			System.out.println("Filename: " + file.getName());
			FileInputStream fstream = new FileInputStream(
					file.getAbsolutePath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] columnsValue = strLine.split(",");
				if (columnsValue.length == 11) {
					String stockId = columnsValue[0].substring(0, 6);
					String date = columnsValue[0].substring(6);
					byte[] row = Bytes.add(
							Bytes.toBytes(Long.parseLong(stockId)),
							Bytes.toBytes(date));
					for (int i = 0; i < 10; i++) {
						String[] columnQual = columnList[i].split(":");
						byte[] col = Bytes.toBytes(columnQual[0]);
						byte[] qaul = Bytes.toBytes(columnQual[1]);
						byte[] value = null;
						if(columnQual[1].equalsIgnoreCase("numShares") || columnQual[1].equalsIgnoreCase("numTrades")){
							value = Bytes.toBytes(Long.parseLong(columnsValue[i + 1]));
						} else {
						value = Bytes.toBytes(Float.parseFloat(columnsValue[i + 1]));
						}
						tester.putRow(row, col, qaul, value);
					}
				}
			}
			br.close();
			in.close();
			fstream.close();
		}
	}
}
