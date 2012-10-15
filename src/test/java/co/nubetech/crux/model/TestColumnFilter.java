package co.nubetech.crux.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestColumnFilter {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setColumnAlias(new ColumnAlias());
		columnFilter.setFilterType(new FilterType());
		columnFilter.setId(0l);
		columnFilter.setReport(new Report());
		columnFilter.setValue("testValue");
		
		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setColumnAlias(new ColumnAlias());
		columnFilter1.setFilterType(new FilterType());
		columnFilter1.setId(0l);
		columnFilter1.setReport(new Report());
		columnFilter1.setValue("testValue");
		
		assertTrue(columnFilter1.equals(columnFilter));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setColumnAlias(new ColumnAlias());
		columnFilter.setFilterType(new FilterType());
		columnFilter.setId(0l);
		columnFilter.setReport(new Report());
		columnFilter.setValue("testValue");
		
		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setColumnAlias(new ColumnAlias());
		columnFilter1.setFilterType(new FilterType());
		columnFilter1.setId(1l);
		columnFilter1.setReport(new Report());
		columnFilter1.setValue("testValue1");
		
		assertFalse(columnFilter1.equals(columnFilter));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		ColumnFilter columnFilter = new ColumnFilter();
		ColumnFilter columnFilter1 = new ColumnFilter();
		assertTrue(columnFilter.equals(columnFilter1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setColumnAlias(new ColumnAlias());
		columnFilter.setFilterType(new FilterType());
		columnFilter.setId(0l);
		columnFilter.setReport(new Report());
		columnFilter.setValue("testValue");
		
		ColumnFilter columnFilter1 = new ColumnFilter();
		columnFilter1.setColumnAlias(new ColumnAlias());
		columnFilter1.setFilterType(new FilterType());
		columnFilter1.setId(0l);
		columnFilter1.setReport(new Report());
		columnFilter1.setValue("testValue");
		
		assertTrue((columnFilter1.hashCode() == columnFilter.hashCode()));
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		ColumnFilter columnFilter = new ColumnFilter();
		ColumnFilter columnFilter1 = new ColumnFilter();
		assertTrue((columnFilter1.hashCode() == columnFilter.hashCode()));
	}

}
