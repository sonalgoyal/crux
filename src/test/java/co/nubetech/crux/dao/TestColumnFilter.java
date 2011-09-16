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

import co.nubetech.crux.model.ColumnFilter;
import co.nubetech.crux.util.CruxException;

public class TestColumnFilter extends DBConnection {
	@Test
	public void testFindById() throws Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into columnFilter values(99999,99999,99999,1,'val')");

		ColumnFilterDAO columnFilterDAO = new ColumnFilterDAO();
		columnFilterDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ColumnFilter columnFilter = columnFilterDAO.findById(99999l);
		assertEquals(99999l, columnFilter.getColumnAlias().getId());
		assertEquals(1l, columnFilter.getFilterType().getId());
		assertEquals(99999l, columnFilter.getReport().getId());
		assertEquals("val", columnFilter.getValue());

		stmt.executeUpdate("delete from columnFilter where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		columnFilterDAO.session.close();
		closeConnection();
	}

	@Test
	public void testSaveRowFilter() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ReportDAO reportDAO = new ReportDAO();
		reportDAO.session = session;

		ColumnAliasDAO columnAliasDAO = new ColumnAliasDAO();
		columnAliasDAO.session = session;

		FilterTypeDAO filterTypeDAO = new FilterTypeDAO();
		filterTypeDAO.session = session;

		ColumnFilterDAO columnFilterDAO = new ColumnFilterDAO();
		columnFilterDAO.session = session;
		columnFilterDAO.transaction = session.getTransaction();

		ColumnFilter columnFilter = new ColumnFilter();
		columnFilter.setFilterType(filterTypeDAO.findById(1l));
		columnFilter.setReport(reportDAO.findById(99999l));
		columnFilter.setColumnAlias(columnAliasDAO.findById(99999l));
		columnFilter.setValue("val");

		columnFilterDAO.save(columnFilter);

		long id = columnFilter.getId();
		ResultSet rs = stmt.executeQuery("select * from columnFilter where id="
				+ id);

		while (rs.next()) {
			assertEquals(rs.getLong("filterTypeId"), 1l);
			assertEquals(rs.getLong("reportId"), 99999l);
			assertEquals(rs.getLong("columnAliasId"), 99999l);
			assertEquals(rs.getString("value"), "val");
		}

		rs.close();

		stmt.executeUpdate("delete from columnFilter where id=" + id);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		columnFilterDAO.session.close();
		closeConnection();
	}

	@Test
	public void testDeleteRowFilter() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,1)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into columnFilter values(99999,99999,99999,1,'val')");

		ColumnFilterDAO columnFilterDAO = new ColumnFilterDAO();
		columnFilterDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		columnFilterDAO.transaction = columnFilterDAO.session.getTransaction();
		columnFilterDAO.delete(columnFilterDAO.findById(99999l));

		ResultSet rs = stmt
				.executeQuery("select * from columnFilter where id = 99999");
		while (rs.next()) {
			assertTrue(false);
		}
		rs.close();

		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		columnFilterDAO.session.close();
		closeConnection();

	}
}
