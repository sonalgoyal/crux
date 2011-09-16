package co.nubetech.crux.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import co.nubetech.crux.model.FunctionTypeMapping;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;

public class FunctionTypeMappingDAO {
	
	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;
	
	public FunctionTypeMapping findById(Long id) {
		FunctionTypeMapping functionTypeMapping = (FunctionTypeMapping) session.get(FunctionTypeMapping.class, id);
		return functionTypeMapping;
	}
	
	public List<FunctionTypeMapping> findAll() {
		List<FunctionTypeMapping> result = session.createQuery("from FunctionTypeMapping").list();
			return result;
	}

}
