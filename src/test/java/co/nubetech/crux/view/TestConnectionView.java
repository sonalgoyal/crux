package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import co.nubetech.crux.model.Connection;
import co.nubetech.crux.model.ConnectionProperty;
import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.util.CruxConstants;

public class TestConnectionView {
	
	@Test
	public void testConnectionView(){
		
		Datastore datastore = new Datastore();
		datastore.setId(121212);
		datastore.setName("Hbase");
		
		Connection connection = new Connection();
		ConnectionProperty connectionProperty1 = new ConnectionProperty(connection,
				                          CruxConstants.HBASE_ZOOKEEPER_PROPERTY,"value1");
		
		connection.addProperty(connectionProperty1);
		connection.setDatastore(datastore);
		connection.setId(1);
		connection.setName("Con");
		
		ConnectionView connectionView = new ConnectionView(1,connection);
		assertEquals(connectionView.getName(),"Con");
		assertEquals(connectionView.getHbaseRestServerPropertyValue(),
				connection.getProperties().get(CruxConstants.HBASE_ZOOKEEPER_PROPERTY).getValue());
		assertEquals(connectionView.getDatastoreName(),"Hbase");
	}
	
	
	
    
}
