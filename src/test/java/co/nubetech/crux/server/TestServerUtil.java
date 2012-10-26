package co.nubetech.crux.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.TestingUtil;

public class TestServerUtil {
	
	@Test
	public void testGetRowAlias() {
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		ReportDesign rowDesign = report.getDesigns().get(0);
		
		System.out.println("Getting alias for " + rowDesign);
		Alias rowAlias = ServerUtil.getAlias(rowDesign);
		assertEquals("rowAlias", rowAlias.getAlias());
		assertEquals("co.nubetech.crux.model.RowAlias", 
				rowAlias.getClass().getName());		
		
	}
	

	@Test
	public void testGetColumnAlias() {
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		ReportDesign columnDesign = report.getDesigns().get(1);
		
		System.out.println("Getting alias for " + columnDesign);		
		assertEquals("alias", ServerUtil.getAlias(columnDesign).getAlias());
		assertEquals("co.nubetech.crux.model.ColumnAlias", 
				ServerUtil.getAlias(columnDesign).getClass().getName());
	}
	
	@Test
	public void testGetValueRow() {
		Report report = TestingUtil.getReportWithoutAggregateFunctions();
		ReportDesign rowDesign = report.getDesigns().get(0);
		RowAlias alias = rowDesign.getRowAlias();
		/*
		 * (byte[] row,
                int roffset,
                int rlength,
                byte[] family,
                int foffset,
                int flength,
                byte[] qualifier,
                int qoffset,
                int qlength,
                long timestamp,
                KeyValue.Type type,
                byte[] value,
                int voffset,
                int vlength)
		 */
		byte[] row = new String("abcdefgh").getBytes();
		byte[] family = new String("f").getBytes();
		byte[] q = new String("q").getBytes();
		byte[] value = new String("I am the val").getBytes();
		KeyValue kv = new KeyValue(row, family, q, value);
		List<KeyValue> kvList = new ArrayList<KeyValue>();
		kvList.add(kv);
		byte[] valueReturned = ServerUtil.getValue(kvList, alias);
		assertEquals(0, Bytes.compareTo(row, valueReturned));
	}
	
	@Test
	public void testGetValueColumn() {
		String columnFamily = new String("columnFamily");
		ColumnAlias alias = new ColumnAlias();
		alias.setColumnFamily(columnFamily);
		String qualifier = new String("q");
		alias.setQualifier(qualifier);
		byte[] row = new String("abcdefgh").getBytes();
		byte[] family = columnFamily.getBytes();
		byte[] q = qualifier.getBytes();
		byte[] value = new String("I am the val").getBytes();
		KeyValue kv = new KeyValue(row, family, q, value);
		List<KeyValue> kvList = new ArrayList<KeyValue>();
		kvList.add(kv);
		byte[] valueReturned = ServerUtil.getValue(kvList, alias);
		assertEquals(0, Bytes.compareTo(value, valueReturned));
	}
	
}
