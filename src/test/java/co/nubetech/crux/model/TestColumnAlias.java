package co.nubetech.crux.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestColumnAlias {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		ColumnAlias columnAlias = new ColumnAlias();		
		columnAlias.setMapping(new Mapping());
		columnAlias.setQualifier("testQualifier");
		columnAlias.setAlias("testAlias");
		columnAlias.setColumnFamily("testCF");
		columnAlias.setValueType(new ValueType());
		columnAlias.setId(0l);
		
		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setMapping(new Mapping());
		columnAlias1.setQualifier("testQualifier");
		columnAlias1.setAlias("testAlias");
		columnAlias1.setColumnFamily("testCF");
		columnAlias1.setValueType(new ValueType());
		columnAlias1.setId(0l);
		
		assertTrue(columnAlias.equals(columnAlias1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		ColumnAlias columnAlias = new ColumnAlias();		
		columnAlias.setMapping(new Mapping());
		columnAlias.setQualifier("testQualifier");
		columnAlias.setAlias("testAlias");
		columnAlias.setColumnFamily("testCF");
		columnAlias.setValueType(new ValueType());
		columnAlias.setId(0l);
		
		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setMapping(new Mapping());
		columnAlias1.setQualifier("testQualifier1");
		columnAlias1.setAlias("testAlias1");
		columnAlias1.setColumnFamily("testCF1");
		columnAlias1.setValueType(new ValueType());
		columnAlias1.setId(1l);
		
		assertFalse(columnAlias.equals(columnAlias1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		ColumnAlias columnAlias = new ColumnAlias();
		ColumnAlias columnAlias1 = new ColumnAlias();
		assertTrue(columnAlias.equals(columnAlias1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		ColumnAlias columnAlias = new ColumnAlias();		
		columnAlias.setMapping(new Mapping());
		columnAlias.setQualifier("testQualifier");
		columnAlias.setAlias("testAlias");
		columnAlias.setColumnFamily("testCF");
		columnAlias.setValueType(new ValueType());
		columnAlias.setId(0l);
		
		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setMapping(new Mapping());
		columnAlias1.setQualifier("testQualifier");
		columnAlias1.setAlias("testAlias");
		columnAlias1.setColumnFamily("testCF");
		columnAlias1.setValueType(new ValueType());
		columnAlias1.setId(0l);
		assertTrue((columnAlias.hashCode() == columnAlias1.hashCode()));
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		ColumnAlias columnAlias = new ColumnAlias();
		ColumnAlias columnAlias1 = new ColumnAlias();
		assertTrue((columnAlias.hashCode() == columnAlias1.hashCode()));
	}

}
