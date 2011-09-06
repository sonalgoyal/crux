
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

public class PopulateDataSet {
	HTable hTable;

	public PopulateDataSet() throws IOException {
		Configuration conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "localhost");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		hTable = new HTable(conf, "russellCrowePageView");
	}

	public void putRow(byte[] row, byte[] column, byte[] qaul, byte[] value)
			throws IOException {
		Put put = new Put(row);
		put.add(column, qaul, value);
		hTable.put(put);
	}

	public static void main(String... arg) throws IOException {
		PopulateDataSet tester = new PopulateDataSet();
		File folder = new File(arg[0]);
		File[] fileList = folder.listFiles();
		String columns = "pageView:count";
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
				if (columnsValue.length == 2) {
					String[] date1 = columnsValue[0].split("/");
					String date = "20" + date1[2] + date1[0] + date1[1];
					
					byte[] row = Bytes.toBytes(date);
					for (int i = 0; i < 1; i++) {
						String[] columnQual = columnList[i].split(":");
						byte[] col = Bytes.toBytes(columnQual[0]);
						byte[] qaul = Bytes.toBytes(columnQual[1]);
						byte[] value = null;
						value = Bytes.toBytes(Integer.parseInt(columnsValue[i + 1]));
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
