package co.nubetech.crux.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestRowAlias {
	@Test
	public void testEquals() {
		Mapping mapping = new Mapping();
		ValueType valueType = new ValueType();
		
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		rowAlias.setLength(4);
		rowAlias.setMapping(mapping);
		rowAlias.setValueType(valueType);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		rowAlias1.setAlias("alias");
		rowAlias1.setLength(4);
		rowAlias1.setMapping(mapping);
		rowAlias1.setValueType(valueType);
		
		assertTrue(rowAlias.equals(rowAlias1));
	}
	
	@Test
	public void testEqualsForNullValue() {
		
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		rowAlias.setLength(4);
		rowAlias.setMapping(null);
		rowAlias.setValueType(null);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		rowAlias1.setAlias("alias");
		rowAlias1.setLength(4);
		rowAlias1.setMapping(null);
		rowAlias1.setValueType(null);
		
		assertTrue(rowAlias.equals(rowAlias1));
	}
	
	@Test
	public void testHashCode() {
		Mapping mapping = new Mapping();
		ValueType valueType = new ValueType();
		
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias");
		rowAlias.setLength(4);
		rowAlias.setMapping(mapping);
		rowAlias.setValueType(valueType);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		rowAlias1.setAlias("alias");
		rowAlias1.setLength(4);
		rowAlias1.setMapping(mapping);
		rowAlias1.setValueType(valueType);
		
		assertTrue(rowAlias.hashCode()==rowAlias1.hashCode());
	}
	
	@Test
	public void testHashCodeForNullValue() {
		Mapping mapping = new Mapping();
		ValueType valueType = new ValueType();
		
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias(null);
		rowAlias.setLength(4);
		rowAlias.setMapping(mapping);
		rowAlias.setValueType(valueType);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(1);
		rowAlias1.setAlias(null);
		rowAlias1.setLength(4);
		rowAlias1.setMapping(mapping);
		rowAlias1.setValueType(valueType);
		
		assertTrue(rowAlias.hashCode()==rowAlias1.hashCode());
	}
	
	@Test
	public void testOffset() {
		Mapping mapping = new Mapping();
		ValueType valueType = new ValueType();
		
		RowAlias rowAlias = new RowAlias();
		rowAlias.setId(1);
		rowAlias.setAlias("alias1");
		rowAlias.setLength(4);
		rowAlias.setMapping(mapping);
		rowAlias.setValueType(valueType);
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(2);
		rowAlias1.setAlias("alias2");
		rowAlias1.setLength(4);
		rowAlias1.setMapping(mapping);
		rowAlias1.setValueType(valueType);
		
		mapping.addRowAlias(rowAlias);
		mapping.addRowAlias(rowAlias1);
		
		assertEquals(0, rowAlias.getOffset());
		assertEquals(4, rowAlias1.getOffset());
	}
	
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
		
		assertEquals(18,rAlias1.getOffset());
		assertEquals(0,rAlias.getOffset());
	}

}
