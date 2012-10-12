package co.nubetech.crux.model;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestReportDesignFunction {
	
	private static ValueType valueType;
	
	@BeforeClass
	public static void setUp() {
		valueType = new ValueType();
		valueType.setClassName("java.lang.Integer");
		valueType.setName("int");
	}
	
	@Test
	public void testEqualsWithNoValuesNull(){
		Function function = new Function(1, "sum","class.Sum",false, valueType, valueType);
		Report report = new Report();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(null);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);
		
		ReportDesignFunction reportDesignFunction1 = new ReportDesignFunction(design, function);
		ReportDesignFunction reportDesignFunction2 = new ReportDesignFunction(design, function);
		assertTrue(reportDesignFunction1.equals(reportDesignFunction2));

	}
	
	@Test
	public void testEqualsForNotEqualObjects(){
		Function functions1 = new Function(1, "sum","class.Sum",false, valueType, valueType);
		Function functions2 = new Function(1, "avg","class.Avg",false, valueType, valueType);
		Report report = new Report();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(null);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);
		
		ReportDesignFunction reportDesignFunction1 = new ReportDesignFunction(design, functions1);
		ReportDesignFunction reportDesignFunction2 = new ReportDesignFunction(design, functions2);
		assertTrue(!reportDesignFunction1.equals(reportDesignFunction2));
	}
	
	@Test
	public void testEqualsWithAllValuesNull(){
		ReportDesignFunction reportDesignFunction1 = new ReportDesignFunction();
		ReportDesignFunction reportDesignFunction2 = new ReportDesignFunction();
		assertTrue(reportDesignFunction1.equals(reportDesignFunction2));
	}
	
	@Test
	public void testHashCodeWithNoValuesNull(){
		Function function = new Function(1, "sum","class.Sum",false, valueType, valueType);
		Report report = new Report();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(null);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);
		
		ReportDesignFunction reportDesignFunction1 = new ReportDesignFunction(design, function);
		ReportDesignFunction reportDesignFunction2 = new ReportDesignFunction(design, function);
		assertTrue(reportDesignFunction1.hashCode() == reportDesignFunction2.hashCode());

	}
	
	@Test
	public void testHashCodeWithAllValuesNull(){
		ReportDesignFunction reportDesignFunction1 = new ReportDesignFunction();
		ReportDesignFunction reportDesignFunction2 = new ReportDesignFunction();
		assertTrue(reportDesignFunction1.hashCode() == reportDesignFunction2.hashCode());
	}

}
