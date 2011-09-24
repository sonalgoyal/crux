package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFilterType {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		FilterType filterType = new FilterType();
		filterType.setId(0l);
		filterType.setType("testType");
		
		FilterType filterType1 = new FilterType();
		filterType1.setId(0l);
		filterType1.setType("testType");
		
		assertTrue(filterType.equals(filterType1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		FilterType filterType = new FilterType();
		filterType.setId(0l);
		filterType.setType("testType");
		
		FilterType filterType1 = new FilterType();
		filterType1.setId(1l);
		filterType1.setType("testType1");
		
		assertFalse(filterType.equals(filterType1));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		FilterType filterType = new FilterType();
		FilterType filterType1 = new FilterType();
		assertTrue(filterType.equals(filterType1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		FilterType filterType = new FilterType();
		filterType.setId(0l);
		filterType.setType("testType");
		
		FilterType filterType1 = new FilterType();
		filterType1.setId(0l);
		filterType1.setType("testType");
		
		assertTrue(filterType.hashCode() == filterType1.hashCode());
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		FilterType filterType = new FilterType();
		FilterType filterType1 = new FilterType();
		assertTrue(filterType.hashCode() == filterType1.hashCode());
	}

}
