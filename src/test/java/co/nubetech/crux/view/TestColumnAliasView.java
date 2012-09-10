package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.ValueType;

public class TestColumnAliasView {
	
	@Test
	public void testColumnAliasView(){
		
		ValueType valueType = new ValueType();
        valueType.setName("value");
		
		ColumnAlias columnAlias = new ColumnAlias();
		columnAlias.setId(1);
		columnAlias.setColumnFamily("cf");
		columnAlias.setQualifier("a");
		columnAlias.setAlias("Alias");
		columnAlias.setValueType(valueType);
		
		ColumnAliasView columnAliasView = new ColumnAliasView(columnAlias);
		
		assertEquals(columnAliasView.getColumnFamily(),"cf");
		assertEquals(columnAliasView.getColumnTypeName(),"value");
		assertEquals(columnAlias.getQualifier(),"a");
		assertEquals(columnAlias.getAlias(),"Alias");
		assertEquals(columnAlias.getId(),1);
	}

}
