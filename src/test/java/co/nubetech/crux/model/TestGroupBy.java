package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGroupBy {
	
	@Test
	public void testEqualsWithNoValuesNull(){
		GroupBy groupBy = new GroupBy();
		groupBy.setId(1l);
		RowAlias alias = new RowAlias();
		alias.setId(100l);
		groupBy.setRowAlias(alias);
		groupBy.setAscending(false);
		
		GroupBy groupBy1 = new GroupBy();
		groupBy1.setId(1l);
		RowAlias alias1 = new RowAlias();
		alias1.setId(100l);
		groupBy1.setRowAlias(alias1);
		groupBy1.setAscending(false);
		
		assertEquals(groupBy, groupBy1);
	}
	
	@Test
	public void notEquals() {
		GroupBy groupBy = new GroupBy();
		groupBy.setId(1l);
		RowAlias alias = new RowAlias();
		alias.setId(100l);
		groupBy.setRowAlias(alias);
		groupBy.setAscending(false);
		
		GroupBy groupBy1 = new GroupBy();
		groupBy1.setId(2l);
		RowAlias alias1 = new RowAlias();
		alias1.setId(100l);
		groupBy1.setRowAlias(alias1);
		groupBy1.setAscending(false);
		
		assertFalse(groupBy.equals(groupBy1));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		GroupBy groupBy = new GroupBy();
		groupBy.setId(1l);
		RowAlias alias = new RowAlias();
		alias.setId(100l);
		groupBy.setRowAlias(alias);
		groupBy.setAscending(false);
		
		GroupBy groupBy1 = new GroupBy();
		groupBy1.setId(1l);
		RowAlias alias1 = new RowAlias();
		alias1.setId(100l);
		groupBy1.setRowAlias(alias1);
		groupBy1.setAscending(false);
		
		assertEquals(groupBy.hashCode(), groupBy1.hashCode());
	}

}
