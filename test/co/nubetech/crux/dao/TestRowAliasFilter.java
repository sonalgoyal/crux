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

import co.nubetech.crux.model.RowAliasFilter;
import co.nubetech.crux.util.CruxException;

public class TestRowAliasFilter extends DBConnection {

	@Test
	public void testFindById() throws Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest')");
		stmt.executeUpdate("insert into rowFilter values(99999,99999,99999,1,'val')");

		RowFilterDAO rowFilterDAO = new RowFilterDAO();
		rowFilterDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		RowAliasFilter rowFilter = rowFilterDAO.findById(99999l);
		assertEquals(99999l, rowFilter.getRowAlias().getId());
		assertEquals(1l, rowFilter.getFilterType().getId());
		assertEquals(99999l, rowFilter.getReport().getId());
		assertEquals("val", rowFilter.getValue());

		stmt.executeUpdate("delete from rowFilter where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		rowFilterDAO.session.close();
		closeConnection();
	}

	@Test
	public void testSaveRowFilter() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = session;

		RowAliasDAO rowAliasDAO = new RowAliasDAO();
		rowAliasDAO.session = session;

		FilterTypeDAO filterTypeDAO = new FilterTypeDAO();
		filterTypeDAO.session = session;

		RowFilterDAO rowFilterDAO = new RowFilterDAO();
		rowFilterDAO.session = session;
		rowFilterDAO.transaction = session.getTransaction();

		RowAliasFilter rowFilter = new RowAliasFilter();
		rowFilter.setFilterType(filterTypeDAO.findById(1l));
		rowFilter.setReport(reportDAO.findById(99999l));
		rowFilter.setRowAlias(rowAliasDAO.findById(99999l));
		rowFilter.setValue("val");

		rowFilterDAO.save(rowFilter);

		long id = rowFilter.getId();
		ResultSet rs = stmt.executeQuery("select * from rowFilter where id="
				+ id);

		while (rs.next()) {
			assertEquals(rs.getLong("filterTypeId"), 1l);
			assertEquals(rs.getLong("reportId"), 99999l);
			assertEquals(rs.getLong("rowAliasId"), 99999l);
			assertEquals(rs.getString("value"), "val");
		}

		rs.close();

		stmt.executeUpdate("delete from rowFilter where id=" + id);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		rowFilterDAO.session.close();
		closeConnection();
	}

	@Test
	public void testDeleteRowFilter() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest')");
		stmt.executeUpdate("insert into rowFilter values(99999,99999,99999,1,'val')");

		RowFilterDAO rowFilterDAO = new RowFilterDAO();
		rowFilterDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		rowFilterDAO.transaction = rowFilterDAO.session.getTransaction();
		rowFilterDAO.delete(rowFilterDAO.findById(99999l));

		ResultSet rs = stmt
				.executeQuery("select * from rowFilter where id = 99999");
		while (rs.next()) {
			assertTrue(false);
		}
		rs.close();

		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		rowFilterDAO.session.close();
		closeConnection();

	}
}
