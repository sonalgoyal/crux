package co.nubetech.crux.action;

import static org.junit.Assert.*;

import org.junit.Test;

import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.ValueType;

public class TestSaveReportAction {
	@Test
	public void testGetOffset() {
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
		
		assertEquals(18,SaveReportAction.getOffset(rAlias1));
		assertEquals(0,SaveReportAction.getOffset(rAlias));

	}
	
	@Test
	public void testGetLengthWhenLengthZero() {

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(0);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		
		assertEquals(18,SaveReportAction.getRowAliasLength(rAlias, 18));
	}
	
	@Test
	public void testGetLengthWhenLengthNull() {

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(null);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		
		assertEquals(18,SaveReportAction.getRowAliasLength(rAlias, 18));
	}
	
	@Test
	public void testGetLength() {

		RowAlias rAlias = new RowAlias();
		rAlias.setAlias("rowkey");
		rAlias.setLength(5);
		ValueType valueType = new ValueType();
		valueType.setClassName("java.lang.String");
		rAlias.setValueType(valueType);
		
		assertEquals(5,SaveReportAction.getRowAliasLength(rAlias, 18));
	}
}
