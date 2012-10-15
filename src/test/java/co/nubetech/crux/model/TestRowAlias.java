package co.nubetech.crux.model;

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
}
