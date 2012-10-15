package co.nubetech.crux.server.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.GroupBy;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.ReportDesignFunction;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.server.functions.CruxFunction;
import co.nubetech.crux.util.CruxException;

public class TestGroupingAggregationImpl {
	
	private Function getFunction(String name, String clazz, boolean isAggregate) {
		Function fn1 = new Function();
		fn1.setFunctionClass(clazz);
		fn1.setFunctionName(name);
		fn1.setAggregate(isAggregate);
		return fn1;
	}
	
	private ReportDesign getDesign(String axis, Alias alias, Report report) {
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
	
	private ReportDesignFunction getReportDesignFunction(Function fn, ReportDesign design) {
		ReportDesignFunction desFunction = new ReportDesignFunction();
		desFunction.setFunction(fn);
		desFunction.setReportDesign(design);
		return desFunction;
	}
	
	
	public Report getReport() {
		ColumnAlias alias = new ColumnAlias();
		alias.setValueType(new ValueType());
		alias.setAlias("alias");
		alias.setColumnFamily("cf");
		alias.setQualifier("qual");
	
		RowAlias rowAlias = new RowAlias();
		rowAlias.setAlias("rowAlias");
		
		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setAlias("rowAlias1");
		
		Report report = new Report();
		report.setName("reportTest");
		
		ReportDesign xDesign = getDesign("x", rowAlias, report);
		ReportDesign yDesign = getDesign("y", rowAlias1, report);
		ReportDesign yDesign1 = getDesign("y", alias, report);
		
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
		groupBy1.setRowAlias(rowAlias1);
		groupBy1.setPosition(2);
		
		groupByList.add(groupBy);
		groupByList.add(groupBy1);
		groupBys.setGroupBy(groupByList);
		groupBys.setReport(report);
		report.setGroupBys(groupBys);
		return report;
	}
	
	
	@Test
	public void testGetAggregators() throws CruxException{
		GroupingAggregationImpl impl = new GroupingAggregationImpl();
		Report report = getReport(); 
		List<Stack<CruxFunction>> functionList = impl.getFunctions(report);
		assertEquals(3, functionList.size());
		Stack<CruxFunction> xFnStack = functionList.get(0);
		assertEquals(2,xFnStack.size());
		assertEquals(co.nubetech.crux.server.functions.SumAggregator.class, xFnStack.pop().getClass());
		assertEquals(co.nubetech.crux.server.functions.Ceil.class, xFnStack.pop().getClass());
		Stack<CruxFunction> yFnStack = functionList.get(1);
		assertEquals(1, yFnStack.size());
		assertEquals(co.nubetech.crux.server.functions.AverageAggregator.class, yFnStack.pop().getClass());
		Stack<CruxFunction> yFnStack1 = functionList.get(2);
		assertEquals(0, yFnStack1.size());		
	}
	
	@Test
	public void testGetAggregatorsNoFunctions() throws CruxException{
		GroupingAggregationImpl impl = new GroupingAggregationImpl();
		Report report = getReport(); 
		for (ReportDesign design: report.getDesigns()) {
			design.setReportDesignFunctionList(null);
		}
		List<Stack<CruxFunction>> functionList = impl.getFunctions(report);
		assertEquals(3, functionList.size());
		Stack<CruxFunction> xFnStack = functionList.get(0);
		assertEquals(0,xFnStack.size());
		Stack<CruxFunction> yFnStack = functionList.get(1);
		assertEquals(0, yFnStack.size());
		Stack<CruxFunction> yFnStack1 = functionList.get(2);
		assertEquals(0, yFnStack1.size());		
	}


}
