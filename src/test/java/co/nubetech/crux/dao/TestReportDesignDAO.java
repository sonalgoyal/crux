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
import java.sql.Statement;

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.util.CruxException;

public class TestReportDesignDAO extends DBConnection {

	@Test
	public void testSaveReportDesign() throws Exception, CruxException {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',null,25)");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDesignDAO reportDesignDAO = new ReportDesignDAO();

		ReportDesign reportDesign = new ReportDesign();
		reportDesignDAO.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ReportDAO report = new ReportDAO();
		report.session = session;

		reportDesign.setMappingAxis("x");
		reportDesign.setColumnAlias(detail.findById(99999l));
		reportDesign.setReport(report.findById(99999l));

		reportDesignDAO.transaction = reportDesignDAO.session.getTransaction();
		long reportId = reportDesignDAO.save(reportDesign);

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id=" + reportId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("reportId"), 99999);
			assertEquals(rs1.getLong("columnAliasId"), 99999l);
			assertEquals(rs1.getString("mappingAxis"), "x");
		}

		rs1.close();

		stmt.executeUpdate("delete from reportDesign where id=" + reportId);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}

	@Test
	public void testFindByIdReportDesign() throws Exception {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',null,25)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ReportDesignDAO reportDesignDAO = new ReportDesignDAO();
		ReportDesign reportDesign = new ReportDesign();
		reportDesignDAO.session = session;
		reportDesign = reportDesignDAO.findById(99999l);

		assertEquals(reportDesign.getId(), 99999l);
		assertEquals(reportDesign.getReport().getId(), 99999l);

		assertEquals(reportDesign.getMappingAxis(), "x");

		stmt.executeUpdate("delete from reportDesign where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}

	@Test
	public void testDeleteReportDesign() throws Exception, CruxException {
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',null,25)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,null,'x')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ReportDesignDAO reportDesignDAO = new ReportDesignDAO();
		ReportDesign reportDesign = new ReportDesign();
		reportDesignDAO.session = session;
		reportDesign = reportDesignDAO.findById(99999l);
		reportDesignDAO.transaction = reportDesignDAO.session.getTransaction();

		long id = reportDesignDAO.delete(reportDesign);

		assertEquals(id, 99999);

		ResultSet rs1 = stmt
				.executeQuery("select * from reportDesign where id = 99999");
		while (rs1.next()) {
			assertTrue(false);
		}
		rs1.close();

		stmt.executeUpdate("delete from reportDesign where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}

	// Create reportDesign when parent object report is not present,since report
	// column in database is set not null, thus it throws crux exception (column
	// cannot be null)
	@Test
	public void testSaveReportDesignParentDeleted() throws Exception,
			CruxException {

		boolean exceptionExists = false;
		Statement stmt = getStatement();

		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ColumnAliasDAO detail = new ColumnAliasDAO();
		detail.session = session;

		ReportDesignDAO reportDesignDAO = new ReportDesignDAO();

		ReportDesign reportDesign = new ReportDesign();
		reportDesignDAO.session = session;

		ReportTypeDAO type = new ReportTypeDAO();
		type.session = session;

		ReportDAO report = new ReportDAO();
		report.session = session;

		reportDesign.setMappingAxis("x");
		reportDesign.setColumnAlias(detail.findById(99999l));

		reportDesignDAO.transaction = reportDesignDAO.session.getTransaction();

		try {
			reportDesign.setReport(report.findById(99999l));
			long reportId = reportDesignDAO.save(reportDesign);
		} catch (CruxException e) {
			exceptionExists = true;
		}

		assertTrue(exceptionExists);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}
}
