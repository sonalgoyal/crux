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

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.util.CruxException;

public class TestColumnAliasDAO extends DBConnection {

	@Test
	public void testSaveColumnAlias() throws Exception, CruxException {
	
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		
		ValueTypeDAO colType = new ValueTypeDAO();
		colType.session = session;

		MappingDAO maps = new MappingDAO();
		maps.session = session;

		ColumnAliasDAO columnAliasDAO = new ColumnAliasDAO();

		ColumnAlias columnAlias = new ColumnAlias();
		columnAliasDAO.session = session;
		columnAlias.setAlias("aliasTest");
		columnAlias.setColumnFamily("columnFamily");
		columnAlias.setValueType(colType.findById(1l));
		columnAlias.setMapping(maps.findById(99999l));
		columnAlias.setQualifier("qualifier");
		long mapsId = 0;
		try {	
		columnAliasDAO.transaction = columnAliasDAO.session.getTransaction();
		// columnAliasDAO.transaction.begin();
		mapsId = columnAliasDAO.save(columnAlias);
		// columnAliasDAO.transaction.commit();

		ResultSet rs1 = stmt.executeQuery("select * from columnAlias where id="
				+ mapsId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("mappingId"), 99999l);
			assertEquals(rs1.getLong("valueTypeId"), 1l);
			assertEquals(rs1.getString("columnFamily"), "columnFamily");
			assertEquals(rs1.getString("qualifier"), "qualifier");
			assertEquals(rs1.getString("alias"), "aliasTest");
		}
		 rs1.close();
		}finally {
			stmt.executeUpdate("delete from columnAlias where id=" + mapsId);
			stmt.executeUpdate("delete from mapping where id=" + 99999);
			stmt.executeUpdate("delete from connection where id=" + 99999);
			session.close();
			stmt.close();
		}
	}

	@Test
	public void testFindById() throws Exception {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");

		ColumnAliasDAO mapDetailsDAO = new ColumnAliasDAO();

		ColumnAlias mapDetails = new ColumnAlias();
		mapDetailsDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		try {
		ColumnAlias maps = mapDetailsDAO.findById(99999l);
		assertEquals(maps.getColumnFamily(), "columnFamilyTest");
		assertEquals(maps.getValueType().getId(), 1l);
		assertEquals(maps.getQualifier(), "qualifierTest");
		assertEquals(maps.getAlias(), "aliasTest");
		assertEquals(maps.getMapping().getId(), 99999l);
		} finally {
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		mapDetailsDAO.session.close();
		stmt.close();
		}

	}

	@Test
	public void testDeleteColumnAlias() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		
		ColumnAliasDAO mapDetailsDAO = new ColumnAliasDAO();
		ColumnAlias mapDetails = new ColumnAlias();
		try {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		mapDetailsDAO.session = session;
		mapDetails = mapDetailsDAO.findById(99999l);
		mapDetailsDAO.transaction = mapDetailsDAO.session.getTransaction();
		// mapDetailsDAO.transaction.begin();
		
		long mapsId = mapDetailsDAO.delete(mapDetails);
		// mapDetailsDAO.transaction.commit();
		assertEquals(mapsId, 99999l);

		ResultSet rs1 = stmt
				.executeQuery("select * from columnAlias where id = 99999");
		while (rs1.next()) {
			assertTrue(false);
		}
		rs1.close();
		} finally {
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		mapDetailsDAO.session.close();
		stmt.close();
		}
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
		 

		ColumnAlias columnAlias = new ColumnAlias();
		columnAlias.setMapping(mapping);
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		valueTypeDAO.transaction = valueTypeDAO.session.getTransaction();
		columnAlias.setAlias("testAlias");
		columnAlias.setColumnFamily("testCF");
		columnAlias.setValueType(valueTypeDAO.findById(1l));
		columnAlias.setQualifier("testQual");

		ColumnAliasDAO columnAliasDAO = new ColumnAliasDAO();
		columnAliasDAO.session = session;
		columnAliasDAO.transaction = columnAliasDAO.session.getTransaction();
	
			columnAliasDAO.save(columnAlias);
		} catch (CruxException e) {
			exceptionExists = true;
		} finally {
		stmt.executeUpdate("delete from connection where id=" + 99999);
		session.close();
		stmt.close();
		assertTrue(exceptionExists);
		}
	}

}
