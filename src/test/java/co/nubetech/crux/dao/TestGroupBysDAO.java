package co.nubetech.crux.dao;

import static org.junit.Assert.*;

import java.sql.Statement;

import org.junit.Test;
import org.junit.Assert.*;

import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.util.CruxException;

public class TestGroupBysDAO extends DBConnection{
	
	@Test
	public void testGetGroupBys() throws Exception{
		Statement stmt = null;
		try{
			stmt = getStatement();
			stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
			stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
			stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
			stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,3)");
			stmt.executeUpdate("insert into rowAlias values(19999,99999,'aliasTest1',1,3)");
			stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',null,25)");
			stmt.executeUpdate("insert into groupBys(id, reportId) values(1, 99999)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId) values(1,1,99999)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId) values(2,1,19999)");
			
			GroupBysDAO dao = new GroupBysDAO();
			dao.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
					.getNewSession();
			GroupBys groupBys = dao.findById(1l); 
			
			assertEquals(1, groupBys.getId());
			assertEquals(99999, groupBys.getReport().getId());
			assertEquals("reportTest", groupBys.getReport().getName());
			assertEquals(2, groupBys.getGroupBy().size());
		}
		catch (CruxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unexpected CruxException" + e);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception " + e);
		} 
		finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

}
