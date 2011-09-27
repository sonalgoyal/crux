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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Mapping;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.util.CruxException;

public class TestMapping extends DBConnection {

	@Test
	public void testUpdateMappingWithOnlyRowAliasChildrens() throws Exception,
			CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest1',1,5)");

		MappingDAO mappingDAO = new MappingDAO();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		valueTypeDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping mapping = new Mapping();
		mapping.setId(mappingDAO.findById(99999l).getId());
		mapping.setName("Mappings");

		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(99999);
		rowAlias1.setAlias("aliasTest1Updated");
		rowAlias1.setValueType(valueTypeDAO.findById(1l));
		rowAlias1.setLength(6);
		mapping.addRowAlias(rowAlias1);

		mappingDAO.transaction = mappingDAO.session.getTransaction();
		long id = mappingDAO.save(mapping);

		assertEquals(id, 99999l);

		mapping = mappingDAO.findById(99999l);
		assertEquals(mapping.getName(), "Mappings");

		Iterator<RowAlias> iterator = mapping.getRowAlias().values().iterator();
		boolean rowAliasUpdated = false;
		while (iterator.hasNext()) {
			RowAlias rowAlias = iterator.next();
			assertEquals(rowAlias.getId(), 99999l);
			assertEquals(rowAlias.getAlias(), "aliasTest1Updated");
			assertEquals(rowAlias.getValueType().getId(), 1l);
			assertEquals(rowAlias.getLength().intValue(), 6);
			rowAliasUpdated = true;
		}
		assertTrue(rowAliasUpdated);
		rowAliasUpdated = false;
		ResultSet rs = stmt
				.executeQuery("select * from rowAlias where mappingId=" + 99999);

		while (rs.next()) {
			assertEquals(rs.getLong("id"), 99999l);
			assertEquals(rs.getString("alias"), "aliasTest1Updated");
			assertEquals(rs.getLong("valueTypeId"), 1l);
			assertEquals(rs.getLong("length"), 6l);
			rowAliasUpdated = true;
		}
		assertTrue(rowAliasUpdated);

		mappingDAO.session.close();
		stmt.executeUpdate("delete from rowAlias where mappingId=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testUpdateMappingWithColumnAliasChildrens() throws Exception,
			CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,6,'cfTest1','qualTest1','aliasTest4')");

		MappingDAO mappingDAO = new MappingDAO();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		valueTypeDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping mapping = new Mapping();
		mapping.setId(mappingDAO.findById(99999l).getId());
		mapping.setName("Mappings");

		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setId(99999);
		columnAlias1.setAlias("aliasTest5Updated");
		columnAlias1.setColumnFamily("cf");
		columnAlias1.setValueType(valueTypeDAO.findById(2l));
		columnAlias1.setQualifier("qual1");
		mapping.addColumnAlias(columnAlias1);

		mappingDAO.transaction = mappingDAO.session.getTransaction();
		long id = mappingDAO.save(mapping);

		assertEquals(id, 99999l);

		mapping = mappingDAO.findById(99999l);
		assertEquals(mapping.getName(), "Mappings");

		boolean columnAliasUpdated = false;
		ResultSet rs = stmt
				.executeQuery("select * from columnAlias where mappingId=" + 99999);

		while (rs.next()) {
			assertEquals(rs.getLong("id"), 99999l);
			assertEquals(rs.getString("columnFamily"), "cf");
			assertEquals(rs.getString("qualifier"), "qual1");
			assertEquals(rs.getString("alias"), "aliasTest5Updated");
			assertEquals(rs.getLong("valueTypeId"), 2l);
			columnAliasUpdated = true;
		}
		assertTrue(columnAliasUpdated);
		mappingDAO.session.close();
		stmt.executeUpdate("delete from columnAlias where mappingId=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testUpdateMappingWithChildrens() throws Exception,
			CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest1',1,5)");
		stmt.executeUpdate("insert into rowAlias values(100000,99999,'aliasTest2',1,5)");
		stmt.executeUpdate("insert into rowAlias values(100001,99999,'aliasTest3',1,5)");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,6,'cfTest1','qualTest1','aliasTest4')");
		stmt.executeUpdate("insert into columnAlias values(100000,99999,6,'cfTest2','qualTest2','aliasTest5')");
		stmt.executeUpdate("insert into columnAlias values(100001,99999,6,'cfTest3','qualTest3','aliasTest6')");

		MappingDAO mappingDAO = new MappingDAO();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		valueTypeDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping mapping = new Mapping();
		mapping.setId(mappingDAO.findById(99999l).getId());
		mapping.setName("Mappings");

		RowAlias rowAlias1 = new RowAlias();
		rowAlias1.setId(99999);
		rowAlias1.setAlias("aliasTest1Updated");
		rowAlias1.setValueType(valueTypeDAO.findById(1l));
		rowAlias1.setLength(6);
		mapping.addRowAlias(rowAlias1);

		RowAlias rowAlias2 = new RowAlias();
		rowAlias2.setAlias("newAlias1Added");
		rowAlias2.setValueType(valueTypeDAO.findById(1l));
		rowAlias2.setLength(7);
		mapping.addRowAlias(rowAlias2);

		RowAlias rowAlias3 = new RowAlias();
		rowAlias3.setId(100001);
		rowAlias3.setAlias("aliasTest3Updated");
		rowAlias3.setValueType(valueTypeDAO.findById(1l));
		rowAlias3.setLength(6);
		mapping.addRowAlias(rowAlias3);

		ColumnAlias columnAlias1 = new ColumnAlias();
		columnAlias1.setId(100000);
		columnAlias1.setAlias("aliasTest5Updated");
		columnAlias1.setColumnFamily("cf");
		columnAlias1.setValueType(valueTypeDAO.findById(2l));
		columnAlias1.setQualifier("qual1");
		mapping.addColumnAlias(columnAlias1);

		ColumnAlias columnAlias2 = new ColumnAlias();
		columnAlias2.setAlias("newAlias2Added");
		columnAlias2.setColumnFamily("cf");
		columnAlias2.setValueType(valueTypeDAO.findById(2l));
		columnAlias2.setQualifier("qual2");
		mapping.addColumnAlias(columnAlias2);

		mappingDAO.transaction = mappingDAO.session.getTransaction();
		long id = mappingDAO.save(mapping);

		assertEquals(id, 99999l);

		mapping = mappingDAO.findById(99999l);
		assertEquals(mapping.getName(), "Mappings");

		mappingDAO.session.close();
		stmt.executeUpdate("delete from rowAlias where mappingId=" + 99999);
		stmt.executeUpdate("delete from columnAlias where mappingId=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testUpdateMapping() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping mapping = mappingDAO.findById(99999l);
		mapping.setName("Mappings");

		mappingDAO.transaction = mappingDAO.session.getTransaction();
		long id = mappingDAO.save(mapping);

		assertEquals(id, 99999l);

		mapping = mappingDAO.findById(99999l);
		assertEquals(mapping.getName(), "Mappings");
		mappingDAO.session.close();
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testGetConnection() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into connectionProperty values(99999,99999,'propertyTest','valueTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");

		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping mapping = mappingDAO.findById(99999l);
		assertEquals(mapping.getName(), "mappingTest");
		assertEquals(mapping.getTableName(), "tableTest");
		assertEquals(mapping.getConnection().getId(), 99999l);

		Map<String, ColumnAlias> details = mapping.getColumnAlias();
		ColumnAlias columnAlias = details.get("aliasTest");

		assertEquals(columnAlias.getId(), 99999l);
		assertEquals(columnAlias.getMapping().getId(), 99999l);
		assertEquals(columnAlias.getColumnFamily(), "columnFamilyTest");
		assertEquals(columnAlias.getAlias(), "aliasTest");
		assertEquals(columnAlias.getValueType().getId(), 1l);
		assertEquals(columnAlias.getQualifier(), "qualifierTest");

		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connectionProperty where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		mappingDAO.session.close();
		closeConnection();
	}

	@Test
	public void testAddDetail() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ColumnAlias columnAlias = new ColumnAlias();
		Mapping mapping = new Mapping();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		valueTypeDAO.transaction = valueTypeDAO.session.getTransaction();
		columnAlias.setAlias("testAlias");
		columnAlias.setColumnFamily("testCF");
		columnAlias.setValueType(valueTypeDAO.findById(1l));
		columnAlias.setQualifier("testQual");
		mapping.addColumnAlias(columnAlias);

		ColumnAlias columnAliasToAssert = mapping.getColumnAlias().get(
				"testAlias");
		assertEquals(columnAliasToAssert.getColumnFamily(), "testCF");
		assertEquals(columnAliasToAssert.getAlias(), "testAlias");
		assertEquals(columnAliasToAssert.getValueType().getId(), 1l);
		assertEquals(columnAliasToAssert.getQualifier(), "testQual");
	}

	@Test
	public void testSaveMapping() throws Exception, CruxException {

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");

		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO colType = new ValueTypeDAO();
		colType.session = session;

		DatastoreDAO datastore = new DatastoreDAO();
		datastore.session = session;

		ConnectionDAO conn = new ConnectionDAO();
		conn.session = session;

		MappingDAO mappingDAO = new MappingDAO();

		Mapping mapping = new Mapping();
		mapping.setConnection(conn.findById(99999l));
		mapping.setName("mappingTest");
		mapping.setTableName("tableTest");

		ColumnAlias details = new ColumnAlias();
		details.setAlias("aliastest");
		details.setColumnFamily("columnFamilyTest");
		details.setValueType(colType.findById(1l));
		details.setMapping(mapping);
		details.setQualifier("qualifierTest");

		Map<String, ColumnAlias> mapColumnAlias = new HashMap<String, ColumnAlias>();
		mapColumnAlias.put("aliastest", details);
		mapping.setColumnAlias(mapColumnAlias);

		mappingDAO.session = session;
		mappingDAO.transaction = mappingDAO.session.getTransaction();
		// mappingDAO.transaction.begin();
		mapping.setColumnAlias(mapColumnAlias);
		mappingDAO.save(mapping);
		// mappingDAO.transaction.commit();
		long mappingId = mapping.getId();
		long detailsId = details.getId();

		ResultSet rs = stmt.executeQuery("select * from mapping where id="
				+ mappingId);

		while (rs.next()) {
			assertEquals(rs.getLong("connectionId"), 99999l);
			assertEquals(rs.getString("name"), "mappingTest");
			assertEquals(rs.getString("tableName"), "tableTest");
		}

		rs.close();

		ResultSet rs1 = stmt.executeQuery("select * from columnAlias where id="
				+ detailsId);
		while (rs1.next()) {
			assertEquals(rs1.getLong("mappingId"), mappingId);
			assertEquals(rs1.getLong("valueTypeId"), 1l);
			assertEquals(rs1.getString("columnFamily"), "columnFamilyTest");
			assertEquals(rs1.getString("qualifier"), "qualifierTest");
			assertEquals(rs1.getString("alias"), "aliastest");
		}

		rs1.close();

		stmt.executeUpdate("delete from columnAlias where id=" + detailsId);
		stmt.executeUpdate("delete from mapping where id=" + mappingId);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testDeleteMapping() throws Exception, CruxException {

		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		MappingDAO maps = new MappingDAO();
		maps.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();

		Mapping resultmaps = maps.findById(99999l);

		maps.transaction = maps.session.getTransaction();
		// maps.transaction.begin();
		long id = maps.delete(resultmaps);

		assertEquals(id, 99999l);

		// maps.transaction.commit();
		maps.session.close();

		ResultSet rs = stmt
				.executeQuery("select * from mapping where id = 99999");
		while (rs.next()) {
			assertTrue(false);
		}
		rs.close();

		ResultSet rs1 = stmt
				.executeQuery("select * from columnAlias where id = 99999");
		while (rs1.next()) {
			assertTrue(false);
		}
		rs1.close();

		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testRowAliasFetchOrder() throws Exception, CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest1',1,5)");
		stmt.executeUpdate("insert into rowAlias values(100000,99999,'aliasTest2',1,5)");
		stmt.executeUpdate("insert into rowAlias values(100001,99999,'aliasTest3',1,5)");

		MappingDAO mappingDAO = new MappingDAO();
		Mapping mapping = new Mapping();
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		mappingDAO.session = session;
		mapping = mappingDAO.findById(99999l);
		Map<String, RowAlias> rowAliasMap = mapping.getRowAlias();
		Set<Entry<String, RowAlias>> mapSet = rowAliasMap.entrySet();
		String[] exectedOrderOfAlias = new String[] { "aliasTest1",
				"aliasTest2", "aliasTest3" };
		int count = 0;
		assertEquals(rowAliasMap.size(), 3);
		for (Map.Entry<String, RowAlias> entry : mapSet) {
			assertEquals(exectedOrderOfAlias[count++], entry.getKey());
		}
		mappingDAO.session.close();
		stmt.executeUpdate("delete from rowAlias where mappingId=" + 99999);
		stmt.executeUpdate("delete from mapping where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);
		closeConnection();
	}

	@Test
	public void testDeleteMappingWhenChildrenExists() throws Exception,
			CruxException {
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest1')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest2',1,3)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',0)");
		stmt.executeUpdate("insert into reportDesign values(99999,99999,99999,99999,'x')");

		MappingDAO mappingDAO = new MappingDAO();
		Mapping mapping = new Mapping();
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		mappingDAO.session = session;
		mapping = mappingDAO.findById(99999l);
		mappingDAO.transaction = mappingDAO.session.getTransaction();

		boolean exceptionExists = false;
		try {
			long mapsId = mappingDAO.delete(mapping);
		} catch (CruxException e) {
			exceptionExists = true;
		}
		assertTrue(exceptionExists);
		stmt.executeUpdate("delete from reportDesign where id=" + 99999);
		stmt.executeUpdate("delete from report where id=" + 99999);
		stmt.executeUpdate("delete from rowAlias where id=" + 99999);
		stmt.executeUpdate("delete from columnAlias where id=" + 99999);
		stmt.executeUpdate("delete from mapping  where id=" + 99999);
		stmt.executeUpdate("delete from connection where id=" + 99999);

		mappingDAO.session.close();
		closeConnection();
	}

	@Test
	public void testFindByIdWhenIdNotExist() throws Exception, CruxException {
		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		mappingDAO.transaction = mappingDAO.session.getTransaction();

		Mapping mapping = null;
		boolean mappingNotExists = false;
		try {
			mapping = mappingDAO.findById(99999l);
		} catch (CruxException e) {
			mappingNotExists = true;

		}
		assertTrue(mappingNotExists);
		assertEquals(null, mapping);
		mappingDAO.session.close();
	}

	@Test
	public void testUpdateWrongId() throws Exception, CruxException {
		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Mapping mapping = null;
		boolean mappingNotExists = false;
		try {
			mapping = mappingDAO.findById(99999l);
		} catch (CruxException e) {
			mappingNotExists = true;

		}
		assertTrue(mappingNotExists);
		mappingDAO.transaction = mappingDAO.session.getTransaction();
		long mappingId = mappingDAO.save(mapping);
		assertEquals(0, mappingId);
	}

	@Test(expected = NullPointerException.class)
	public void testDeleteWrongId() throws Exception, CruxException {
		MappingDAO mappingDAO = new MappingDAO();
		mappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		Mapping mapping = null;
		boolean mappingNotExists = false;
		try {
			mapping = mappingDAO.findById(99999l);
		} catch (CruxException e) {
			mappingNotExists = true;

		}
		assertTrue(mappingNotExists);
		mappingDAO.transaction = mappingDAO.session.getTransaction();
		mappingDAO.delete(mapping);
	}

}
