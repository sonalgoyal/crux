package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import co.nubetech.crux.model.Datastore;
import co.nubetech.crux.model.FunctionTypeMapping;
import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.ValueType;

public class TestFunctionView {
	
	@Test
	public void testFunctionView(){
		
		Function function = new Function();
		function.setId(12345);
		function.setFunctionName("fun");
		function.setFunctionClass("class1");
		function.setFunctionType((short)1);
		
		Datastore dataStore = new Datastore();
		dataStore.setId(1212);
		dataStore.setName("DataStore");
		
		ValueType valueType = new ValueType(1,dataStore,"Type","class2",true);
		ValueType returnValueType = new ValueType(2,dataStore,"ReturnType","class3",false);
		
		FunctionTypeMapping functionTypeMapping = new FunctionTypeMapping(function,
				                                             valueType,returnValueType);
		
		FunctionView functionView = new FunctionView(functionTypeMapping);
		
		assertEquals(functionView.getFunctionName(),"fun");
		assertEquals(functionView.getValueType(),"Type");
		assertEquals(functionView.getReturnType(),"ReturnType");
		
	}

}
