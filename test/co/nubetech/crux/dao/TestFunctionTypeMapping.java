package co.nubetech.crux.dao;

import static org.junit.Assert.*;

import java.sql.Statement;

import org.junit.Test;

import co.nubetech.crux.model.FunctionTypeMapping;

public class TestFunctionTypeMapping extends DBConnection {
	
	@Test
	public void testFindById() throws Exception {
		FunctionTypeMappingDAO functionTypeMappingDAO = new FunctionTypeMappingDAO();
		functionTypeMappingDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
		.getNewSession();
		
		Statement stmt = getStatement();
		stmt.executeUpdate("insert into function values(99999,'sum','class.Sum',1)");
		stmt.executeUpdate("insert into functionTypeMapping values(99999, 99999, 1, 1)");
		
		ValueTypeDAO valueTypeDAO = new ValueTypeDAO();
		valueTypeDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
		.getNewSession();
		
		FunctionDAO functionDAO = new FunctionDAO();
		functionDAO.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
		.getNewSession();

		
		FunctionTypeMapping functionTypeMapping = new FunctionTypeMapping(functionDAO.findById(99999l),
				valueTypeDAO.findById(1l), valueTypeDAO.findById(1l));
		functionTypeMapping.setId(99999l);
		
		assertEquals(functionTypeMappingDAO.findById(99999l), functionTypeMapping);
		
		stmt.executeUpdate("delete from functionTypeMapping where id=99999");
		stmt.executeUpdate("delete from function where id=99999");
		closeConnection();

	}

}
