/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.util.CruxException;

public class TestReport extends DBConnection {

	@Test
	public void testGetReport() throws Exception, CruxException {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report resultRep = reportDAO.findById(99999l);
		assertEquals(resultRep.getName(), "reportTest");
		assertEquals(resultRep.getUser().getId(), 1l);

		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>(
				resultRep.getDesigns());
		ReportDesign design = designs.get(0);

		assertEquals(design.getId(), 99999l);
		assertEquals(design.getReport().getId(), 99999l);
		assertEquals(design.getMappingAxis(), "x");

		stmt.executeUpdate("delete from reportDesign where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	@Test
	public void testSaveReport() throws Exception, CruxException {

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDAO reportDAO = new ReportDAO();
		Report report = new Report();
		report.setName("reportTest");
		report.setUser(user.findById(1l));
		report.setDashboardType((short) 1);

		ReportDesign design = new ReportDesign();
		design.setMappingAxis("x");
		design.setColumnAlias(detail.findById(99999l));
		design.setReport(report);
		report.setReportType(type.findById(1l));

		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		designs.add(design);
		report.setDesigns(designs);

		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		long reportId = reportDAO.save(report);
		long designId = design.getId();

		ResultSet rs = stmt.executeQuery("select * from report where id="
				+ reportId);

		while (rs.next()) {
			assertEquals(rs.getLong("userId"), 1l);
			assertEquals(rs.getString("name"), "reportTest");
			assertEquals(rs.getLong("reportTypeId"), 1l);
			assertEquals(rs.getShort("isDashBoard"),1);
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=" + designId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("reportId"), reportId);
			assertEquals(rs1.getLong("columnAliasId"), 99999l);
			assertEquals(rs1.getString("mappingAxis"), "x");
		}

		rs1.close();

		stmt.executeUpdate("delete from reportDesign where id=" + designId);
		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	@Test
	public void testDeleteReport() throws Exception, CruxException {

		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		ReportDAO reportDAO = new ReportDAO();

		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report resultRep = reportDAO.findById(99999l);
		reportDAO.transaction = reportDAO.session.getTransaction();

		long id = reportDAO.delete(resultRep);

		assertEquals(id, 99999l);

		reportDAO.session.close();

		ResultSet rs = stmt
				.executeQuery("select * from report where id = 99999");
		while (rs.next()) {
			assertTrue(false);
		}
		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id = 99999");
		while (rs1.next()) {
			assertTrue(false);
		}
		rs1.close();

		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	// when id does not exists. It returns null.
	@Test(expected = CruxException.class)
	public void testFindById() throws Exception, CruxException {
		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report resultRep = reportDAO.findById(99999l);
		assertEquals(null, resultRep);
	}

	// If we try to update any object whose id does not exists throws
	// IllegalArgumentException but in our function we check for null and return
	// 0 for id
	@Test(expected = CruxException.class)
	public void testUpdateWrongId() throws Exception, CruxException {
		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report resultRep = reportDAO.findById(99999l);
		reportDAO.transaction = reportDAO.session.getTransaction();
		long id = reportDAO.save(resultRep);
		assertEquals(id, 0);
	}

	// If try to delete any object whose id does not exists throws
	// IllegalArgumentException, but in our code we first try to fetch its id
	// therefore here it throws null pointer exception.
	@Test(expected = CruxException.class)
	public void testDeleteWrongId() throws Exception, CruxException {
		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report resultRep = reportDAO.findById(99999l);
		reportDAO.transaction = reportDAO.session.getTransaction();
		reportDAO.delete(resultRep);

	}

	// If we create report with null reportName value then crux exception
	// appears (Column 'name' cannot be null)
	@Test
	public void testSaveReportForNullValues() throws Exception {

		boolean exceptionExists = false;

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDAO reportDAO = new ReportDAO();
		Report report = new Report();
		report.setName(null);
		report.setUser(user.findById(1l));

		ReportDesign design = new ReportDesign();
		design.setMappingAxis("x");
		design.setColumnAlias(detail.findById(99999l));
		design.setReport(report);
		report.setReportType(type.findById(1l));

		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		designs.add(design);
		report.setDesigns(designs);

		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		try {
			reportDAO.save(report);
		} catch (CruxException e) {
			exceptionExists = true;
		}

		assertTrue(exceptionExists);

		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	// If we try to Save report with same data it generates crux exception with
	// message of duplicate entries for unique keys
	@Test
	public void testSaveReportForDuplicateObjects() throws Exception {
		boolean exceptionExists = false;
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDAO reportDAO = new ReportDAO();
		Report report = new Report();
		report.setName("reportTest");
		report.setUser(user.findById(1l));

		ReportDesign design = new ReportDesign();
		design.setMappingAxis("x");
		design.setColumnAlias(detail.findById(99999l));
		design.setReport(report);
		report.setReportType(type.findById(1l));

		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		designs.add(design);
		report.setDesigns(designs);

		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		try {
			reportDAO.save(report);
		} catch (CruxException e) {
			exceptionExists = true;
		}
		assertTrue(exceptionExists);
		stmt.executeUpdate("delete from reportDesign where id=99999");
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	// If we try to save same report object again it simply overwrites it with
	// update query
	@Test
	public void testUpdateReportForDuplicateObjects() throws Exception,
			CruxException {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		ReportDAO reportDAO = new ReportDAO();

		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Report resultRep = reportDAO.findById(99999l);

		reportDAO.transaction = reportDAO.session.getTransaction();

		long reportId = reportDAO.save(resultRep);

		assertEquals(reportId, 99999l);

		ResultSet rs = stmt.executeQuery("select * from report where id=99999");

		while (rs.next()) {
			assertEquals(rs.getString("name"), "reportTest");
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=99999");
		while (rs1.next()) {

			assertEquals(rs1.getString("mappingAxis"), "x");
		}

		rs1.close();
		stmt.executeUpdate("delete from reportDesign where id=99999");
		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	// when we add reportDesign object to the parent list
	@Test
	public void testUpdateReportForChildrenAdded() throws Exception,
			CruxException {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99998,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamily','qualifier','alias')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = session;

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		Report resultRep = reportDAO.findById(99999l);
		ReportDesign design = new ReportDesign(resultRep,
				detail.findById(99998l), "y",null);
		resultRep.addDesign(design);

		reportDAO.transaction = reportDAO.session.getTransaction();

		long reportId = reportDAO.save(resultRep);

		long designId = design.getId();

		assertEquals(reportId, 99999l);

		ResultSet rs = stmt.executeQuery("select * from report where id=99999");

		while (rs.next()) {
			assertEquals(rs.getString("name"), "reportTest");
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=" + designId);
		while (rs1.next()) {

			assertEquals(rs1.getLong("reportId"), 99999l);
			assertEquals(rs1.getLong("columnAliasId"), 99998l);
			assertEquals(rs1.getString("mappingAxis"), "y");
		}

		rs1.close();
		stmt.executeUpdate("delete from reportDesign where id=99999");
		stmt.executeUpdate("delete from reportDesign where id=" + designId);
		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99998);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	// when we modify the existing reportDesign object
	@Test
	public void testReportUpdateForChildrenModified() throws Exception,
			CruxException {

		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		ReportDAO reportDAO = new ReportDAO();

		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Report resultRep = reportDAO.findById(99999l);
		resultRep.setName("testReport");

		ArrayList<ReportDesign> design = new ArrayList<ReportDesign>(
				resultRep.getDesigns());
		design.get(0).setMappingAxis("y");
		reportDAO.transaction = reportDAO.session.getTransaction();

		long reportId = reportDAO.save(resultRep);

		assertEquals(reportId, 99999l);

		ResultSet rs = stmt.executeQuery("select * from report where id=99999");

		while (rs.next()) {
			assertEquals(rs.getString("name"), "testReport");
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=99999");
		while (rs1.next()) {

			assertEquals(rs1.getString("mappingAxis"), "y");
		}

		rs1.close();
		stmt.executeUpdate("delete from reportDesign where id=99999");
		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	// when we remove any of the existing reportDesign object
	@Test
	public void testReportUpdateForChildrenDeleted() throws Exception,
			CruxException {

		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDAO reportDAO = new ReportDAO();
		Report report = new Report();
		report.setName("reportTest");
		report.setUser(user.findById(1l));
		report.setDesigns(new ArrayList<ReportDesign>());
		report.setReportType(type.findById(1l));
		report.setId(99999l);

		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		long reportId = reportDAO.save(report);

		assertEquals(reportId, 99999l);

		ResultSet rs = stmt.executeQuery("select * from report where id=99999");

		while (rs.next()) {
			assertEquals(rs.getString("name"), "reportTest");
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=" + 99999);
		while (rs1.next()) {
			assertTrue(false);
		}

		rs1.close();

		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}

	@Test
	public void testFiltersSaveWhenReportSaved() throws CruxException,
			Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		UserDAO user = new UserDAO();
		user.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ColumnAliasDAO columnAlias = new ColumnAliasDAO();
		columnAlias.session = session;

		RowAliasDAO rowAlias = new RowAliasDAO();
		rowAlias.session = session;

		FilterTypeDAO filterType = new FilterTypeDAO();
		filterType.session = session;

		ReportDAO reportDAO = new ReportDAO();
		Report report = new Report();
		report.setName("reportTest");
		report.setUser(user.findById(1l));

		ReportDesign design = new ReportDesign();
		design.setMappingAxis("x");
		design.setColumnAlias(columnAlias.findById(99999l));
		design.setReport(report);
		report.setReportType(type.findById(1l));

		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>();
		designs.add(design);
		report.setDesigns(designs);

		RowAliasFilter rowAliasFilter = new RowAliasFilter();
		rowAliasFilter.setReport(report);
		rowAliasFilter.setRowAlias(rowAlias.findById(99999l));
		rowAliasFilter.setFilterType(filterType.findById(1l));
		rowAliasFilter.setValue("rowFilter");

		ArrayList<RowAliasFilter> rowFilters = new ArrayList<RowAliasFilter>();
		rowFilters.add(rowAliasFilter);
		report.setRowAliasFilters(rowFilters);

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setColumnAlias(columnAlias.findById(99999l));
		columnFilter.setFilterType(filterType.findById(1l));
		columnFilter.setReport(report);
		columnFilter.setValue("columnFilter");

		ArrayList<ColumnFilter> columnFilters = new ArrayList<ColumnFilter>();
		columnFilters.add(columnFilter);
		report.setColumnFilters(columnFilters);

		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		long reportId = reportDAO.save(report);
		long designId = design.getId();
		long rowAliasFilterId = rowAliasFilter.getId();
		long columnFilterId = columnFilter.getId();

		ResultSet rs1 = stmt.executeQuery("select * from rowFilter where id="
				+ rowAliasFilterId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("reportId"), reportId);
			assertEquals(rs1.getLong("rowAliasId"), 99999l);
			assertEquals(rs1.getLong("filterTypeId"), 1l);
			assertEquals(rs1.getString("value"), "rowFilter");
		}

		rs1.close();

		ResultSet rs2 = stmt.executeQuery("select * from columnFilter where id="
				+ columnFilterId);
		while (rs2.next()) {
			assertEquals(rs2.getLong("reportId"), reportId);
			assertEquals(rs2.getLong("columnAliasId"), 99999l);
			assertEquals(rs2.getLong("filterTypeId"), 1l);
			assertEquals(rs2.getString("value"), "columnFilter");
		}

		rs2.close();
		
		stmt.executeUpdate("delete from rowFilter where id=" + rowAliasFilterId);
		stmt.executeUpdate("delete from columnFilter where id=" + columnFilterId);
		stmt.executeUpdate("delete from reportDesign where id=" + designId);
		stmt.executeUpdate("delete from report where id=" + reportId);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		reportDAO.session.close();
		closeConnection();
	}
	
	@Test
	public void testFiltersDeleteWhenReportDeleted() throws CruxException,
			Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into rowFilter values(99999,99999,99999,1,'val')");
		stmt.executeUpdate("insert into columnFilter values(99999,99999,99999,1,'val')");

		ReportDAO reportDAO = new ReportDAO();

		reportDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Report report = reportDAO.findById(99999l);
		reportDAO.transaction = reportDAO.session.getTransaction();

		long id = reportDAO.delete(report);

		assertEquals(id, 99999l);

		reportDAO.session.close();

		ResultSet rs1 = stmt.executeQuery("select * from rowFilter where id=99999");
		while (rs1.next()) {
			assertTrue(false);
		}

		rs1.close();

		ResultSet rs2 = stmt.executeQuery("select * from columnFilter where id=99999");
		while (rs2.next()) {
			assertTrue(false);
		}

		rs2.close();
		
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}
	
	@Test
	public void testFiltersUpdatedWhenReportUpdated() throws CruxException,
			Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into rowFilter values(99999,99999,99999,1,'val')");
		stmt.executeUpdate("insert into columnFilter values(99999,99999,99999,1,'val')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
		.getNewSession();
		
		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = session;
		reportDAO.transaction = reportDAO.session.getTransaction();
		
		Report resultRep = reportDAO.findById(99999l);
		resultRep.setName("testReport");

		ArrayList<RowAliasFilter> rowFilter = new ArrayList<RowAliasFilter>(
				resultRep.getRowAliasFilters());
		rowFilter.get(0).setValue("rowFilter");
		

		long reportId = reportDAO.save(resultRep);

		assertEquals(reportId, 99999l);

		ResultSet rs = stmt.executeQuery("select * from report where id=99999");

		while (rs.next()) {
			assertEquals(rs.getString("name"), "testReport");
		}

		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from rowFilter where id=99999");
		while (rs1.next()) {

			assertEquals(rs1.getString("value"), "rowFilter");
		}
		rs1.close();
		
		long id = reportDAO.delete(resultRep);
		
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
		session.close();
	}
}
