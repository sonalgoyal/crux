package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAnalysis {

	@Test
	public void testEqualsWithAllValuesNull(){
		Analysis analysis = new Analysis();
		Analysis analysis1 = new Analysis();
		assertTrue(analysis.equals(analysis1));
	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		Analysis analysis = new Analysis();
		Analysis analysis1 = new Analysis();
		assertTrue(analysis.hashCode() == analysis1.hashCode());
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertTrue(analysis.hashCode() == analysis1.hashCode());

	}
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertTrue(analysis.equals(analysis1));
	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Analysis analysis = new Analysis();
		analysis.setId(1l);
		analysis.setName("TestAnalysis");
		analysis.setText("{foo:\"a\",boo:12}");
		
		Analysis analysis1 = new Analysis();
		analysis1.setId(1l);
		analysis1.setName("TestAnalysis1");
		analysis1.setText("{foo:\"a\",boo:12}");
		
		assertFalse(analysis.equals(analysis1));
	}
	
}
