package co.nubetech.crux.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestReportDesign {
	@Test
	public void testEquals() {

		Report report = new Report();
		ColumnAlias columnAlias = new ColumnAlias();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(columnAlias);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);

		ReportDesign design1 = new ReportDesign();
		design1.setId(1);
		design1.setColumnAlias(columnAlias);
		design1.setMappingAxis("x");
		design1.setReport(report);
		design1.setRowAlias(rowAlias);

		assertTrue(design.equals(design1));
	}

	@Test
	public void testEqualsForNullValue() {

		Report report = new Report();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(null);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);

		ReportDesign design1 = new ReportDesign();
		design1.setId(1);
		design1.setColumnAlias(null);
		design1.setMappingAxis("x");
		design1.setReport(report);
		design1.setRowAlias(rowAlias);

		assertTrue(design.equals(design1));
	}

	@Test
	public void testEqualsForNotEquals() {

		Report report = new Report();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(null);
		design.setMappingAxis("x");
		design.setReport(null);
		design.setRowAlias(rowAlias);

		ReportDesign design1 = new ReportDesign();
		design1.setId(1);
		design1.setColumnAlias(null);
		design1.setMappingAxis("x");
		design1.setReport(report);
		design1.setRowAlias(rowAlias);

		assertTrue(!design.equals(design1));
	}

	@Test
	public void testHashCode() {

		Report report = new Report();
		ColumnAlias columnAlias = new ColumnAlias();
		RowAlias rowAlias = new RowAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(columnAlias);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(rowAlias);

		ReportDesign design1 = new ReportDesign();
		design1.setId(1);
		design1.setColumnAlias(columnAlias);
		design1.setMappingAxis("x");
		design1.setReport(report);
		design1.setRowAlias(rowAlias);

		assertTrue(design.hashCode() == design1.hashCode());
	}

	@Test
	public void testHashCodeForNullValue() {

		Report report = new Report();
		ColumnAlias columnAlias = new ColumnAlias();

		ReportDesign design = new ReportDesign();
		design.setId(1);
		design.setColumnAlias(columnAlias);
		design.setMappingAxis("x");
		design.setReport(report);
		design.setRowAlias(null);

		ReportDesign design1 = new ReportDesign();
		design1.setId(1);
		design1.setColumnAlias(columnAlias);
		design1.setMappingAxis("x");
		design1.setReport(report);
		design1.setRowAlias(null);

		assertTrue(design.hashCode() == design1.hashCode());
	}
}
