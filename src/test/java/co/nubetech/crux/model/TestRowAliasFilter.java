package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestRowAliasFilter {

	@Test
	public void testEquals(){
		Report report = new Report();
		FilterType filterType = new FilterType();
		RowAlias rowAlias = new RowAlias();
		
		RowAliasFilter rowAliasFilter = new RowAliasFilter();
		rowAliasFilter.setFilterType(filterType);
		rowAliasFilter.setId(2);
		rowAliasFilter.setReport(report);
		rowAliasFilter.setRowAlias(rowAlias);
		rowAliasFilter.setValue("value");
		
		RowAliasFilter rowAliasFilter1 = new RowAliasFilter();
		rowAliasFilter1.setFilterType(filterType);
		rowAliasFilter1.setId(2);
		rowAliasFilter1.setReport(report);
		rowAliasFilter1.setRowAlias(rowAlias);
		rowAliasFilter1.setValue("value");
		
		assertTrue(rowAliasFilter.equals(rowAliasFilter1));
	}
	
	@Test
	public void testEqualsForNull(){
		Report report = new Report();
		FilterType filterType = new FilterType();
		RowAlias rowAlias = new RowAlias();
		
		RowAliasFilter rowAliasFilter = new RowAliasFilter();
		rowAliasFilter.setFilterType(filterType);
		rowAliasFilter.setId(2);
		rowAliasFilter.setReport(report);
		rowAliasFilter.setRowAlias(rowAlias);
		rowAliasFilter.setValue(null);
		
		RowAliasFilter rowAliasFilter1 = new RowAliasFilter();
		rowAliasFilter1.setFilterType(filterType);
		rowAliasFilter1.setId(2);
		rowAliasFilter1.setReport(report);
		rowAliasFilter1.setRowAlias(rowAlias);
		rowAliasFilter1.setValue(null);
		
		assertTrue(rowAliasFilter.equals(rowAliasFilter1));
	}
	
	@Test
	public void testHashCode(){
		Report report = new Report();
		FilterType filterType = new FilterType();
		RowAlias rowAlias = new RowAlias();
		
		RowAliasFilter rowAliasFilter = new RowAliasFilter();
		rowAliasFilter.setFilterType(filterType);
		rowAliasFilter.setId(2);
		rowAliasFilter.setReport(report);
		rowAliasFilter.setRowAlias(rowAlias);
		rowAliasFilter.setValue("value");
		
		RowAliasFilter rowAliasFilter1 = new RowAliasFilter();
		rowAliasFilter1.setFilterType(filterType);
		rowAliasFilter1.setId(2);
		rowAliasFilter1.setReport(report);
		rowAliasFilter1.setRowAlias(rowAlias);
		rowAliasFilter1.setValue("value");
		
		assertTrue(rowAliasFilter.hashCode()==rowAliasFilter1.hashCode());
	}
	

	@Test
	public void testHashCodeForNull(){
		Report report = new Report();
		FilterType filterType = new FilterType();
		RowAlias rowAlias = new RowAlias();
		
		RowAliasFilter rowAliasFilter = new RowAliasFilter();
		rowAliasFilter.setFilterType(filterType);
		rowAliasFilter.setId(2);
		rowAliasFilter.setReport(report);
		rowAliasFilter.setRowAlias(rowAlias);
		rowAliasFilter.setValue(null);
		
		RowAliasFilter rowAliasFilter1 = new RowAliasFilter();
		rowAliasFilter1.setFilterType(filterType);
		rowAliasFilter1.setId(2);
		rowAliasFilter1.setReport(report);
		rowAliasFilter1.setRowAlias(rowAlias);
		rowAliasFilter1.setValue(null);
		
		assertTrue(rowAliasFilter.hashCode()==rowAliasFilter1.hashCode());
	}
}
