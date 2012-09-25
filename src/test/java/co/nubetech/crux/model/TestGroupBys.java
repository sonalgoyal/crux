package co.nubetech.crux.model;

import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestGroupBys {
	
	@Test
	public void testEquals() {
		GroupBy date = new GroupBy();
		date.setId(1l);
		RowAlias alias = new RowAlias();
		alias.setId(1l);
		alias.setLength(10);
		alias.setValueType(new ValueType());
		date.setAlias(alias);
		
		GroupBy time = new GroupBy();
		time.setId(2l);
		RowAlias alias1 = new RowAlias();
		alias1.setId(2l);
		alias1.setLength(5);
		time.setAlias(alias1);
		
		GroupBys groupBys = new GroupBys();
		groupBys.setId(100l);
		ArrayList<GroupBy> groupByList = new ArrayList<GroupBy>();
		groupByList.add(date);
		groupByList.add(time);
		groupBys.setGroupBy(groupByList);
		
	}

}
