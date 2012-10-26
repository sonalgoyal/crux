package co.nubetech.crux.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

public class TestingUtil {
	
	  public static final byte[] TEST_TABLE = Bytes.toBytes("TestTable");
	  public static final byte[] TEST_FAMILY = Bytes.toBytes("TestFamily");
	  public static final byte[] TEST_QUALIFIER = Bytes.toBytes("TestQualifier");
	  public static final byte[] TEST_MULTI_CQ = Bytes.toBytes("TestMultiCQ");

	  public static byte[] ROW = Bytes.toBytes("testRow");
	  public static final int ROWSIZE = 20;
	  public static final int rowSeperator1 = 5;
	  public static final int rowSeperator2 = 12;
	  public static byte[][] ROWS = makeN(ROW, ROWSIZE);

	  /**
	   * an infrastructure method to prepare rows for the testtable.
	   * @param base
	   * @param n
	   * @return
	   */
	  public static byte[][] makeN(byte[] base, int n) {
	    byte[][] ret = new byte[n][];
	    for (int i = 0; i < n; i++) {
	      ret[i] = Bytes.add(base, Bytes.toBytes(i));
	    }
	    return ret;
	  }

	  public static ValueType getStringValueType() {
		  ValueType string = new ValueType();
		  string.setClassName("java.lang.String");
		  string.setNumeric(false);
		  return string;
	  }
	  
	  public static ValueType getDoubleValueType() {
		  ValueType string = new ValueType();
		  string.setClassName("java.lang.Double");
		  string.setNumeric(true);
		  return string;
	  }
	  
	  
	  public static RowAlias getRowAlias(String alias, int length, ValueType val) {
		  RowAlias rowAlias = new RowAlias();
		  rowAlias.setAlias(alias);
		  rowAlias.setLength(length);
		  rowAlias.setValueType(val);
		  return rowAlias;			
	  }
	  
	  public static ColumnAlias getColAlias(String alias, int length, String columnFamily, 
			  String qualifier, ValueType val) {
		  ColumnAlias colAlias = new ColumnAlias();
		  colAlias.setAlias(alias);
		  colAlias.setQualifier(qualifier);
		  colAlias.setColumnFamily(columnFamily);
		  colAlias.setValueType(val);
		  return colAlias;			
	  }
	
	public static Function getFunction(String name, String clazz, boolean isAggregate) {
		Function fn1 = new Function();
		fn1.setFunctionClass(clazz);
		fn1.setFunctionName(name);
		fn1.setAggregate(isAggregate);
		return fn1;
	}
	
	public static ReportDesign getDesign(String axis, Alias alias, Report report) {
		ReportDesign design = new ReportDesign();
		design.setMappingAxis(axis);
		if (alias instanceof ColumnAlias) {
			design.setColumnAlias((ColumnAlias) alias);
		}
		else {
			design.setRowAlias((RowAlias) alias);
		}
		design.setReport(report);
		return design;		
	}
	
	public static ReportDesignFunction getReportDesignFunction(Function fn, ReportDesign design) {
		ReportDesignFunction desFunction = new ReportDesignFunction();
		desFunction.setFunction(fn);
		desFunction.setReportDesign(design);
		return desFunction;
	}
	
	
	public static Report getReport() {
		Mapping mapping = new Mapping();
		mapping.setTableName(new String(TEST_TABLE));
		
		ColumnAlias alias = new ColumnAlias();
		alias.setValueType(new ValueType());
		alias.setAlias("alias");
		alias.setColumnFamily(new String(TEST_FAMILY));
		alias.setQualifier(new String(TEST_QUALIFIER));
		
		ColumnAlias alias1 = new ColumnAlias();
		alias1.setValueType(new ValueType());
		alias1.setAlias("alias");
		alias1.setColumnFamily(new String(TEST_FAMILY));
		alias1.setQualifier(new String(TEST_MULTI_CQ));
	
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("rowAlias");
		rowAlias.setLength(8);
		mapping.addRowAlias(rowAlias);
		mapping.addColumnAlias(alias);
		mapping.addColumnAlias(alias1);
		
		Report report = new Report();
		report.setName("reportTest");
		
		ReportDesign xDesign = getDesign("x", rowAlias, report);
		ReportDesign yDesign = getDesign("y", alias, report);
		ReportDesign yDesign1 = getDesign("y", alias1, report);
		
		Function sum = getFunction("sum", 
				"co.nubetech.crux.server.functions.SumAggregator", true);
		Function average = getFunction("avergae", "co.nubetech.crux.server.functions.AverageAggregator",
				true);
		Function ceil = getFunction("ceil", "co.nubetech.crux.server.functions.Ceil", false);
		
		List<ReportDesignFunction> xFunctions = new ArrayList<ReportDesignFunction>();
		xFunctions.add(getReportDesignFunction(ceil, xDesign));
		xFunctions.add(getReportDesignFunction(sum, xDesign));
		xDesign.setReportDesignFunctionList(xFunctions);
		
		List<ReportDesignFunction> yFunctions = new ArrayList<ReportDesignFunction>();
		yFunctions.add(getReportDesignFunction(average, yDesign));
		yDesign.setReportDesignFunctionList(yFunctions);
		
		List<ReportDesignFunction> yFunctions1 = new ArrayList<ReportDesignFunction>();
		yFunctions1.add(getReportDesignFunction(average, yDesign1));
		yDesign1.setReportDesignFunctionList(yFunctions1);
		
		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		
		designs.add(xDesign);
		designs.add(yDesign);
		designs.add(yDesign1);
		report.setDesigns(designs);
		
		GroupBys groupBys = new GroupBys();		
		
		List<GroupBy> groupByList = new ArrayList<GroupBy>();
		GroupBy groupBy = new GroupBy();		
		groupBy.setRowAlias(rowAlias);
		groupBy.setPosition(1);
		
		GroupBy groupBy1 = new GroupBy();		
		groupBy1.setRowAlias(rowAlias);
		groupBy1.setPosition(2);
		
		groupByList.add(groupBy);
		groupByList.add(groupBy1);
		groupBys.setGroupBy(groupByList);
		groupBys.setReport(report);
		report.setGroupBys(groupBys);
		return report;
	}
	
	public static Report getReportWithoutAggregateFunctions() {
		Mapping mapping = new Mapping();
		mapping.setTableName(new String(TEST_TABLE));
		
		ValueType string = new ValueType();
		string.setClassName("java.lang.String");
		string.setNumeric(false);
		string.setId(1);
		
		ValueType dbl = new ValueType();
		dbl.setClassName("java.lang.Double");
		dbl.setNumeric(true);
		dbl.setId(2);
		
		ColumnAlias alias = new ColumnAlias();
		alias.setValueType(dbl);
		alias.setAlias("alias");
		alias.setColumnFamily(new String(TEST_FAMILY));
		alias.setQualifier(new String(TEST_QUALIFIER));
		
		ColumnAlias alias1 = new ColumnAlias();
		alias1.setValueType(string);
		alias1.setAlias("alias1");
		alias1.setColumnFamily(new String(TEST_FAMILY));
		alias1.setQualifier(new String(TEST_MULTI_CQ));
	
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("rowAlias");
		rowAlias.setLength(8);
		rowAlias.setValueType(dbl);
		
		mapping.addRowAlias(rowAlias);
		mapping.addColumnAlias(alias);
		mapping.addColumnAlias(alias1);
		
		Report report = new Report();
		report.setName("reportTest");
		
		ReportDesign xDesign = getDesign("x", rowAlias, report);
		ReportDesign yDesign = getDesign("y", alias, report);
		ReportDesign yDesign1 = getDesign("y", alias1, report);
		
		Function upper = getFunction("upper", 
				"co.nubetech.crux.server.functions.UpperCase", false);
		Function ceil = getFunction("ceil", "co.nubetech.crux.server.functions.Ceil", false);
		
		List<ReportDesignFunction> xFunctions = new ArrayList<ReportDesignFunction>();
		xFunctions.add(getReportDesignFunction(ceil, xDesign));
		
		xDesign.setReportDesignFunctionList(xFunctions);
		List<ReportDesignFunction> yFunctions = new ArrayList<ReportDesignFunction>();
		yFunctions.add(getReportDesignFunction(upper, yDesign));
		yDesign.setReportDesignFunctionList(yFunctions);
		
		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		
		designs.add(xDesign);
		designs.add(yDesign);
		designs.add(yDesign1);
		report.setDesigns(designs);
		
		GroupBys groupBys = new GroupBys();		
		
		List<GroupBy> groupByList = new ArrayList<GroupBy>();
		GroupBy groupBy = new GroupBy();		
		groupBy.setRowAlias(rowAlias);
		groupBy.setPosition(1);
		
		GroupBy groupBy1 = new GroupBy();		
		groupBy1.setRowAlias(rowAlias);
		groupBy1.setPosition(2);
		
		groupByList.add(groupBy);
		groupByList.add(groupBy1);
		groupBys.setGroupBy(groupByList);
		groupBys.setReport(report);
		report.setGroupBys(groupBys);
		return report;
	}

}
