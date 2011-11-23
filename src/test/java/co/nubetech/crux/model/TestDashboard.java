package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDashboard {

	@Test
	public void testEquals(){
		Dashboard dashboard1= new Dashboard(2,1);
		Dashboard dashboard2 = new Dashboard(2,1);
		Dashboard dashboard3 = new Dashboard();
		
		assertEquals(dashboard1,dashboard2);
		assertTrue(!dashboard1.equals(dashboard3));
	}
	
	@Test
	public void testEqualsForNull(){

		Dashboard dashboard3 = new Dashboard();
		assertTrue(!dashboard3.equals(null));
	}
	
	@Test
	public void testHashCode(){
		Dashboard dashboard1= new Dashboard(2,1);
		Dashboard dashboard2 = new Dashboard(2,1);
		Dashboard dashboard3 = new Dashboard();
		
		assertTrue(dashboard1.hashCode()==dashboard2.hashCode());
		assertTrue(dashboard1.hashCode()!=dashboard3.hashCode());
	}
}
