package co.nubetech.crux.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.TestingUtil;
import co.nubetech.crux.model.ValueType;

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
	public void testGetValueRowComposite() {
		byte[] longBytes = Bytes.toBytes(new Long(10));
		byte[] intBytes = Bytes.toBytes((int) 10);
		byte[] stringBytes = Bytes.toBytes("I am a String" + 10);
		byte[] row = Bytes.add(longBytes, intBytes, stringBytes);
		
		byte[] family = new String("f").getBytes();
		byte[] q = new String("q").getBytes();
		byte[] value = new String("I am the val").getBytes();
		
		KeyValue kv = new KeyValue(row, family, q, value);
		List<KeyValue> kvList = new ArrayList<KeyValue>();
		kvList.add(kv);
		
		Report report = new Report();
		Mapping mapping = new Mapping();
		mapping.setTableName("TABLE_2");

		RowAlias rAlias1 = new RowAlias();
		rAlias1.setAlias("rowkeyLong");
		rAlias1.setLength(8);
		rAlias1.setId(1l);
		ValueType valueType1 = new ValueType();
		valueType1.setClassName("java.lang.Long");
		rAlias1.setValueType(valueType1);
		rAlias1.setMapping(mapping);
		
		RowAlias rAlias2 = new RowAlias();
		rAlias2.setAlias("rowkeyInt");
		rAlias2.setLength(4);
		rAlias2.setId(2l);
		ValueType valueType2 = new ValueType();
		valueType2.setClassName("java.lang.Integer");
		rAlias2.setValueType(valueType2);
		rAlias2.setMapping(mapping);
		
		RowAlias rAlias3 = new RowAlias();
		rAlias3.setAlias("rowkeyString");
		rAlias3.setId(3l);
		rAlias3.setLength((int) Bytes.toBytes("I am a String" + 11).length);
		ValueType valueType3 = new ValueType();
		valueType3.setClassName("java.lang.String");
		rAlias3.setValueType(valueType3);
		rAlias3.setMapping(mapping);
		
		LinkedHashMap<String, RowAlias> rowAliases = new LinkedHashMap<String, RowAlias>();
		rowAliases.put(rAlias1.getAlias(), rAlias1);
		rowAliases.put(rAlias2.getAlias(), rAlias2);
		rowAliases.put(rAlias3.getAlias(), rAlias3);
		mapping.setRowAlias(rowAliases);
		
		byte[] valueReturnedLong = ServerUtil.getValue(kvList, rAlias1);
		assertEquals(0, Bytes.compareTo(longBytes, valueReturnedLong)); 
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
		System.out.println("Value returned is " + valueReturned);
		assertEquals(0, Bytes.compareTo(value, valueReturned));
	}
	
}
