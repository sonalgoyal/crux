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

import org.hibernate.Session;
import org.junit.Test;

import co.nubetech.crux.model.ValueType;

public class TestValueTypeDAO {

	@Test
	public void testForString() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(1l);
		assertEquals("java.lang.String", valueType.getClassName());
		assertTrue(!valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForBoolean() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(2l);
		assertEquals("java.lang.Boolean", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForInteger() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(3l);
		assertEquals("java.lang.Integer", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForLong() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(4l);
		assertEquals("java.lang.Long", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForFloat() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(5l);
		assertEquals("java.lang.Float", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForDouble() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(6l);
		assertEquals("java.lang.Double", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}

	@Test
	public void testForShort() throws Exception {
		Session session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = session;
		ValueType valueType = valueTypeDAO.findById(7l);
		assertEquals("java.lang.Short", valueType.getClassName());
		assertTrue(valueType.isNumeric());
		valueTypeDAO.session.close();
	}
}
