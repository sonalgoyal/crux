package co.nubetech.crux.dao;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import co.nubetech.crux.model.GroupBy;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.util.CruxException;

public class TestGroupBysDAO extends DBConnection{
	Statement stmt;
	
	@Before
	public void createBaseObjects() throws Exception {
		stmt = getStatement();
		stmt.executeUpdate("insert into connection values(99999,1,1,'connectionTest')");
		stmt.executeUpdate("insert into mapping values(99999,99999,'mappingTest','tableTest')");
		stmt.executeUpdate("insert into columnAlias values(99999,99999,1,'columnFamilyTest','qualifierTest','aliasTest')");
		stmt.executeUpdate("insert into rowAlias values(99999,99999,'aliasTest',1,3)");
		stmt.executeUpdate("insert into rowAlias values(19999,99999,'aliasTest1',1,3)");
		stmt.executeUpdate("insert into report values(99999,1,1,'reportTest',null,25)");		
	
	}
	
	@After
	public void cleanUp() throws Exception {
		stmt.executeUpdate("delete from groupBy");
		stmt.executeUpdate("delete from groupBys");
		stmt.executeUpdate("delete from report");
		stmt.executeUpdate("delete from rowAlias");
		stmt.executeUpdate("delete from columnAlias");
		stmt.executeUpdate("delete from mapping");
		stmt.executeUpdate("delete from connection");
		
		stmt.close();

	}
	
	
	
	@Test
	public void testGetGroupBys() throws Exception{
		GroupBysDAO dao = null;
		try{
			stmt.executeUpdate("insert into groupBys(id, reportId) values(1, 99999)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId, position) values(1,1,99999,1)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId, position) values(2,1,19999,2)");
			
			dao = new GroupBysDAO();
			dao.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
					.getNewSession();
			GroupBys groupBys = dao.findById(1l); 
			
			assertEquals(1, groupBys.getId());
			assertEquals(99999, groupBys.getReport().getId());
			assertEquals("reportTest", groupBys.getReport().getName());
			//for (GroupBy groupBy: groupBys.getGroupBy()) {
			assertEquals(2, groupBys.getGroupBy().size());
			//System.out.println("Groupby is " + groupBy);
			//}
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
				dao.session.close();				
			
		}
	}

	
	@Test
	public void testDelete() throws Exception{
		GroupBysDAO dao = null;
		try{
			stmt.executeUpdate("insert into groupBys(id, reportId) values(1, 99999)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId, position) values(1,1,99999,1)");
			stmt.executeUpdate("insert into groupBy(id, groupBysId, rowAliasId, position) values(2,1,19999,2)");
			
			dao = new GroupBysDAO();
			dao.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
					.getNewSession();
			dao.transaction = dao.session.getTransaction();
			GroupBys groupBys = dao.findById(1l); 
			
			assertEquals(1, groupBys.getId());
			assertEquals(99999, groupBys.getReport().getId());
			assertEquals("reportTest", groupBys.getReport().getName());
			assertEquals(2, groupBys.getGroupBy().size());
			dao.delete(groupBys);
			
			ResultSet rs = stmt.executeQuery("select id from groupBys");
			if (rs!= null  && rs.first()) {
				System.out.println(rs.getLong(0));
				fail("GroupBys should have been deleted");
			}
			ResultSet rs1 = stmt.executeQuery("select id from groupBy");
			if (rs1 != null && rs1.first()) {
				fail("Deleting groupbys should have deleted groupby");
			}
			
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
				dao.session.close();
		}
	}
	
	@Test
	public void testSave() throws Exception {
		GroupBysDAO dao = null;
		try{
			dao = new GroupBysDAO();
			dao.session = com.googlecode.s2hibernate.struts2.plugin.util.HibernateSessionFactory
					.getNewSession();
			dao.transaction = dao.session.getTransaction();
			
			RowAlias alias = new RowAlias();
			alias.setId(99999);
			
			RowAlias alias1 = new RowAlias();
			alias1.setId(19999);
			
			GroupBy groupBy = createGroupBy(1, alias);
			GroupBy groupBy1 = createGroupBy(2, alias1);
			List<GroupBy> groupByList = new ArrayList<GroupBy>();
			groupByList.add(groupBy);
			groupByList.add(groupBy1);
			
			Report report = new Report();
			report.setId(99999);
			
			GroupBys groupBys = new GroupBys();
			groupBys.setGroupBy(groupByList);
			groupBys.setReport(report);
			
			long id = dao.save(groupBys);
						
			assertEquals(id, groupBys.getId());
			//assertEquals(99999, groupBys.getReport().getId());
//			/assertEquals("reportTest", groupBys.getReport().getName());
			
			ResultSet rs = stmt.executeQuery("select id, reportId from groupBys");
			if (rs!= null  && rs.first()) {
				assertEquals(id, rs.getLong(1));
				assertEquals(99999, rs.getLong(2));
				rs.close();
			}
			else {
				fail("GroupBys not created");
			}
			ResultSet rs1 = stmt.executeQuery("select id, groupBysId,rowAliasId, position from groupBy");
			if (rs1 != null && rs1.first()) {
				assertNotNull(rs1.getLong(1));
				assertEquals(id, rs1.getLong(2));
				assertEquals(99999, rs1.getLong(3));
				assertEquals(1, rs1.getInt(4));
				rs1.next();
				assertNotNull(rs1.getLong(1));
				assertEquals(id, rs1.getLong(2));
				assertEquals(19999, rs1.getLong(3));
				assertEquals(2, rs1.getInt(4));
				rs1.close();				
			}
			else {				
				fail("Groupbys should have created groupBy");
			}
			
		}
		catch (Throwable e) {
			e.printStackTrace();
			fail("Unexpected exception " + e);
		} 
		finally {
				dao.session.close();
		}
		
	}
	
	public GroupBy createGroupBy(int position, RowAlias alias) {
		GroupBy groupBy = new GroupBy();
		groupBy.setRowAlias(alias);
		groupBy.setPosition(position);
		return groupBy;
	}

}
