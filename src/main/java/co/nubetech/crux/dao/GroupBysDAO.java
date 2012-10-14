package co.nubetech.crux.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.GroupBy;
import co.nubetech.crux.model.GroupBys;
import co.nubetech.crux.util.CruxException;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class GroupBysDAO {
	
	@SessionTarget
	public Session session;
	@TransactionTarget
	public Transaction transaction;
	
	private final static Logger logger = Logger
			.getLogger(GroupBysDAO.class);

	
	public GroupBys findById(Long id) throws CruxException {
		GroupBys groupBys = null;
		try {
			groupBys = (GroupBys) session.get(GroupBys.class, id);
			groupBys.getId();
		}
		catch (NullPointerException e) {
			throw new CruxException(
					"Selected GroupBys does not exist in the database.", e);
		}
		return groupBys;
	}
	
	public long delete(GroupBys groupBys) throws CruxException {
		long id = groupBys.getId();
		try {
			transaction.begin();
			//null the group by
			List<GroupBy> groupByList = groupBys.getGroupBy();
			if (groupByList != null) {
				for (GroupBy grpBy: groupByList) {
					groupByList.remove(grpBy);
				}
			}
			groupBys.getReport().setGroupBys(null);
			session.delete(groupBys);
			transaction.commit();
		} catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(
					"The groupBys could not be deleted ",
					e);
		}
		return id;
	}

	public long save(GroupBys groupBys) throws CruxException {
		try {
			transaction.begin();
			session.saveOrUpdate(groupBys);
			transaction.commit();
		} 
		catch (JDBCException e) {
			transaction.rollback();
			throw new CruxException(
					"Could not save GroupBys",
					e);
		}
		return groupBys.getId();
	}
}
