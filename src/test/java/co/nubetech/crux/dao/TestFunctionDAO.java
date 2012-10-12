package co.nubetech.crux.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Statement;

import org.junit.Test;

import co.nubetech.crux.model.Function;

public class TestFunctionDAO extends DBConnection {

	@Test
	public void testFindById() throws Exception {
		FunctionDAO functionDAO = new FunctionDAO();
		functionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
				.getNewSession();
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = functionDAO.session;
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into function values(99999,'sum','class.Sum',false,1,1)");
		Function function = new Function(99999,"sum","class.Sum",false,valueTypeDAO.findById(1l), valueTypeDAO.findById(1l));
		assertEquals(function, functionDAO.findById(99999l));
		functionDAO.session.close();
		stmt.executeUpdate("delete from function where id=99999");
		stmt.close();
	}
}
