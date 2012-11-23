package co.nubetech.crux.action;

import org.junit.Test;

import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.GroupBy;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestSaveReportAction {

	@Test
	public void testPopulateGroupBys() throws CruxException {
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		ValueType valueType = new ValueType(101, datastore, "valueType", "className1", 
				"className1", true);
		
		Mapping mapping = new Mapping();
				
		RowAlias rowAlias1 = new RowAlias(901, mapping, valueType, 4 ,"rowAliasName1");
		RowAlias rowAlias2 = new RowAlias(902, mapping, valueType, 4 ,"rowAliasName2");
		
		
		Map<String, RowAlias> rowAliasMap = new TreeMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		
		
		mapping.setRowAlias(rowAliasMap);
		
		GroupBy groupBy1 = new GroupBy(111l, rowAlias1, 1);
		GroupBy groupBy2 = new GroupBy(112l, rowAlias2, 2);
		
		
		List<GroupBy> groupByList = new ArrayList<GroupBy>();
		groupByList.add(groupBy1);
		groupByList.add(groupBy2);
		
		
		Report report = new Report();
		report.setId(1234l);
		
		GroupBys groupBys = new GroupBys();
		groupBys.setGroupBy(groupByList);
		groupBys.setReport(report);
		groupBys.setId(1l);
		report.setGroupBys(groupBys);
		
		SaveReportAction saveReportAction = new SaveReportAction();
		saveReportAction.populateGroupBys(rowAliasMap);
		saveReportAction.setReport(report);
		
		assertEquals(saveReportAction.getReport().getGroupBys(), groupBys );
		assertEquals(saveReportAction.getReport().getId(), 1234l);
		assertEquals(saveReportAction.getReport().getGroupBys().getGroupBy()
				.get(0).getRowAlias().getAlias(), "rowAliasName1");
		assertEquals(saveReportAction.getReport().getGroupBys().getGroupBy()
				.get(1).getRowAlias().getAlias(), "rowAliasName2");

		
	}
}
