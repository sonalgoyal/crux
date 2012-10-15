package co.nubetech.crux.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import co.nubetech.crux.dao.FunctionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.dao.ReportTypeDAO;
import co.nubetech.crux.dao.ValueFilterTypeDAO;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.FilterType;
import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.ValueFilterType;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.util.CruxException;

public class TestReportDesignAction {

	@Test
	public void testPopulateMappingList(){
		Mapping mapping1 = new Mapping();
		mapping1.setId(121);
		Mapping mapping2 = new Mapping();
		mapping2.setId(122);
		Mapping mapping3 = new Mapping();
		mapping3.setId(123);
				
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping1);
		mappingList.add(mapping2);
		mappingList.add(mapping3);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		ReportDesignAction reportDesignAction = new ReportDesignAction();
		reportDesignAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
	
		ArrayList<Mapping> populateMappingList = null;
		populateMappingList = reportDesignAction.populateMappingList(mockedMappingDAO, populateMappingList);
		
		assertEquals(mappingList.get(0).getId(), populateMappingList.get(0).getId());
		assertEquals(mappingList.get(1).getId(), populateMappingList.get(1).getId());
		assertEquals(mappingList.get(2).getId(), populateMappingList.get(2).getId());
		
	}

	@Test
	public void testPopulateDimensionAndMeasureList(){
						
		Mapping mapping = new Mapping();
		mapping.setId(120);
								
		ArrayList<Mapping> mappingList = new ArrayList<Mapping>();
		mappingList.add(mapping);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		ReportDesignAction reportDesignAction = new ReportDesignAction();
		reportDesignAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findAll()).thenReturn(mappingList);
	
		ArrayList<Mapping> populateMappingList = null;
		populateMappingList = reportDesignAction.populateMappingList(mockedMappingDAO, populateMappingList);
			
		ReportType reportType1 = new ReportType(201, "reportTypeOne");
		ReportType reportType2 = new ReportType(202, "reportTypeTwo");
		ReportType reportType3 = new ReportType(203, "reportTypeThree");
		
		ArrayList<ReportType> reportTypeList = new ArrayList<ReportType>();
		reportTypeList.add(reportType1);
		reportTypeList.add(reportType2);
		reportTypeList.add(reportType3);
				
		ReportTypeDAO mockedReportTypeDAO = mock(ReportTypeDAO.class); 
		reportDesignAction.setReportTypeDAO(mockedReportTypeDAO);
		when(mockedReportTypeDAO.findAll()).thenReturn(reportTypeList);
						
		
		ValueType valueType1 = new ValueType();
		valueType1.setId(1001);
		ValueType valueType2 = new ValueType();
		valueType2.setId(1002);
	
		Function function1 = new Function(1,"FirstFunction", "classFirstFunction", false, valueType1, valueType1);
		Function function2 = new Function(2, "SecondFunction", "classSecondFunction", true, valueType1, valueType2);
		Function function3 = new Function(3,"ThirdFunction", "classThirdFunction", false, valueType1, valueType2);
		
		List<Function> functions = new ArrayList<Function>();
		functions.add(function1);
		functions.add(function2);
		functions.add(function3);
		FunctionDAO mockedFunctionDAO = mock(FunctionDAO.class);
		reportDesignAction.setFunctionDAO(mockedFunctionDAO);
		when(mockedFunctionDAO.findAll()).thenReturn(functions);
		
		String returnString = reportDesignAction.populateDimensionAndMeasureList();
		
		assertEquals(returnString, "success");
		//asserting 
		assertEquals(mappingList.get(0).getId(), populateMappingList.get(0).getId());
		//asserting reportTypeList :
		assertEquals(reportTypeList.get(0), reportDesignAction.getReportTypeList().get(0));
		assertEquals(reportTypeList.get(1), reportDesignAction.getReportTypeList().get(1));
		assertEquals(reportTypeList.get(2), reportDesignAction.getReportTypeList().get(2));
		//asserting functionTypeMappingList :
		assertEquals(functions.get(0).getFunctionName(), reportDesignAction.getFunctionViewList().get(0).getFunctionName());
		assertEquals(functions.get(1).getFunctionName(), reportDesignAction.getFunctionViewList().get(1).getFunctionName());
		assertEquals(functions.get(2).getFunctionName(), reportDesignAction.getFunctionViewList().get(2).getFunctionName());
		
		// Problem : 'if (mappingId != 0){..}' section not done in this test method.
		
	}
	
	@Test
	public void populateFilterType() throws CruxException{
		
		ReportDesignAction reportDesignAction = new ReportDesignAction();
		
		Datastore datastore = new Datastore();
		datastore.setId(90);
		datastore.setName("DataStore");
				
		ValueType valueType1 = new ValueType(501, datastore, "valueTypeOne", "classValueType1", 
				"className1", false);
		ValueType valueType2 = new ValueType(502, datastore, "valueTypeTwo", "classValueType2", 
				"className1", false);
		ValueType valueType3 = new ValueType(503, datastore, "valueTypeThree", "classValueType3", 
				"className1", false);
		
		FilterType filterType1 = new FilterType(701, "filterTypeOne");
		FilterType filterType2 = new FilterType(702, "filterTypeTwo");
		FilterType filterType3 = new FilterType(703, "filterTypeThree");
		
		ValueFilterType valueFilterType1 = new ValueFilterType(valueType1, filterType1);
		ValueFilterType valueFilterType2 = new ValueFilterType(valueType2, filterType2);
		ValueFilterType valueFilterType3 = new ValueFilterType(valueType3, filterType3);
		
		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setAlias("columnAlias1");
		columnAlias1.setValueType(valueType1);
		columnAlias1.setId(701);
		ColumnAlias columnAlias2 = new ColumnAlias();
		columnAlias2.setAlias("columnAlias2");
		columnAlias2.setValueType(valueType2);
		columnAlias2.setId(702);
		ColumnAlias columnAlias3 = new ColumnAlias();
		columnAlias3.setAlias("columnAlias3");
		columnAlias3.setValueType(valueType3);
		columnAlias3.setId(703);
				
		Map<String, ColumnAlias> columnAliasMap = new HashMap<String, ColumnAlias>();
		columnAliasMap.put("ColumnAlias1", columnAlias1);
		columnAliasMap.put("ColumnAlias2", columnAlias2);
		columnAliasMap.put("ColumnAlias3", columnAlias3);
		
		RowAlias rowAlias1= new RowAlias();
		rowAlias1.setAlias("rowAlias1");
		rowAlias1.setValueType(valueType1);
		RowAlias rowAlias2= new RowAlias();
		rowAlias2.setAlias("rowAlias2");
		rowAlias2.setValueType(valueType2);
		RowAlias rowAlias3= new RowAlias();
		rowAlias3.setAlias("rowAlias3");
		rowAlias3.setValueType(valueType3);
		
		Map<String, RowAlias> rowAliasMap = new HashMap<String, RowAlias>();
		rowAliasMap.put("rowAlias1", rowAlias1);
		rowAliasMap.put("rowAlias2", rowAlias2);
		rowAliasMap.put("rowAlias3", rowAlias3);
		
		Mapping mapping = new Mapping();
		mapping.setId(120);
		mapping.setColumnAlias(columnAliasMap);
		mapping.setRowAlias(rowAliasMap);
		
		reportDesignAction.setMappingId(120);
		
		MappingDAO mockedMappingDAO = mock(MappingDAO.class);
		reportDesignAction.setMappingDAO(mockedMappingDAO);
		when(mockedMappingDAO.findById((long)120)).thenReturn(mapping);
				
		List<ValueFilterType> valueFilterTypeList = new ArrayList<ValueFilterType>();
		valueFilterTypeList.add(valueFilterType1);
		valueFilterTypeList.add(valueFilterType2);
		valueFilterTypeList.add(valueFilterType3);
				
		ValueFilterTypeDAO mockedValueFilterTypeDAO = mock(ValueFilterTypeDAO.class);
		reportDesignAction.setValueFilterTypeDAO(mockedValueFilterTypeDAO);
		when(mockedValueFilterTypeDAO.findAll()).thenReturn(valueFilterTypeList);
		
		reportDesignAction.setMappingId(120);
		reportDesignAction.setAliasName("rowAlias1");
		
		ArrayList<FilterType> filterTypeList = new ArrayList<FilterType>();
		
		for(ValueFilterType valueFilterType : valueFilterTypeList){
			filterTypeList.add(valueFilterType.getFilterType());
		}
		
		
		reportDesignAction.populateFilterType();
		
		assertEquals(filterTypeList.get(0), reportDesignAction.getFilterTypeList().get(0));
		
		
	}
	
	@Test
	public void testEditReport() throws CruxException{
		
		ReportDesignAction reportDesignAction = new ReportDesignAction();
		
		Report report = new Report();
		report.setId(100);
		//report.set
		reportDesignAction.setReport(report);
		
		
		ReportDAO mockedReportDAO = mock(ReportDAO.class);
		reportDesignAction.setReportDAO(mockedReportDAO);
		when(mockedReportDAO.findById(100)).thenReturn(report);
		
		
		
		//reportDesignAction.editReport();
		
	}
	
	
}
