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

import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.util.CruxException;

public class TestRowAliasDAO extends DBConnection {

	@Test
	public void testSaveRowAlias() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;

		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = session;

		RowAliasDAO rowAliasDAO = new RowAliasDAO();
		RowAlias rowAlias = new RowAlias();
		rowAliasDAO.session = session;
		rowAlias.setMapping(mappingDAO.findById(99999l));
		rowAlias.setAlias("alias");
		rowAlias.setValueType(valueTypeDAO.findById(1l));
		rowAlias.setLength(3);

		rowAliasDAO.transaction = rowAliasDAO.session.getTransaction();
		long rowAliasId = rowAliasDAO.save(rowAlias);

		ResultSet rs1 = stmt.executeQuery("select * from rowAlias where id="
				+ rowAliasId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("mappingId"), 99999l);
			assertEquals(rs1.getLong("valueTypeId"), 1l);
			assertEquals(rs1.getLong("length"), 3);
			assertEquals(rs1.getString("alias"), "alias");
		}

		rs1.close();

		stmt.executeUpdate("delete from rowAlias where id=" + rowAliasId);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();

	}

	@Test
	public void testFindById() throws Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,3)");

		RowAliasDAO rowAliasDAO = new RowAliasDAO();
		RowAlias rowAlias = new RowAlias();
		rowAliasDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		rowAlias = rowAliasDAO.findById(99999l);
		assertEquals(rowAlias.getValueType().getId(), 1l);
		assertEquals(rowAlias.getLength().intValue(), 3);
		assertEquals(rowAlias.getAlias(), "aliasTest");
		assertEquals(rowAlias.getMapping().getId(), 99999l);

		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		rowAliasDAO.session.close();
		stmt.close();

	}

	@Test
	public void testDeleteRowAlias() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,3)");

		RowAliasDAO rowAliasDAO = new RowAliasDAO();
		RowAlias rowAlias = new RowAlias();
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		rowAliasDAO.session = session;
		rowAlias = rowAliasDAO.findById(99999l);
		rowAliasDAO.transaction = rowAliasDAO.session.getTransaction();
		long rowAliasId = rowAliasDAO.delete(rowAlias);
		assertEquals(rowAliasId, 99999l);

		ResultSet rs1 = stmt
				.executeQuery("select * from rowAlias where id = 99999");
		while (rs1.next()) {
			assertTrue(false);
		}
		rs1.close();

		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}

	@Test
	public void testSaveWhenParentIsDeleted() throws Exception, CruxException {
		boolean exceptionExists = false;
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = session;
		mappingDAO.transaction = mappingDAO.session.getTransaction();
		Mapping mapping = mappingDAO.findById(99999l);
		try {
			mappingDAO.delete(mapping);
		} catch (CruxException e) {
		}

		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		RowAlias rowAlias = new RowAlias();
		rowAlias.setMapping(mapping);
		rowAlias.setAlias("alias");
		rowAlias.setValueType(valueTypeDAO.findById(1l));
		rowAlias.setLength(3);

		RowAliasDAO rowAliasDAO = new RowAliasDAO();
		rowAliasDAO.session = session;
		rowAliasDAO.transaction = rowAliasDAO.session.getTransaction();
		try {
			rowAliasDAO.save(rowAlias);
		} catch (CruxException e) {
			exceptionExists = true;
		}
		assertTrue(exceptionExists);

		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
	}

}
