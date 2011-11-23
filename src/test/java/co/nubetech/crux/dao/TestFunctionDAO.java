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
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into function values(99999,'sum','class.Sum',1)");
		Function function = new Function("sum","class.Sum",(short)1);
		function.setId(99999l);
		assertEquals(functionDAO.findById(99999l), function);
		functionDAO.session.close();
		stmt.executeUpdate("delete from function where id=99999");
		stmt.close();
	}
}
