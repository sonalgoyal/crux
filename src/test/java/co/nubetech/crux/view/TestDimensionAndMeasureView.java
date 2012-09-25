package co.nubetech.crux.view;

import static org.junit.Assert.assertEquals;

import co.nubetech.crux.model.ValueType;
import co.nubetech.crux.model.RowAlias;
import co.nubetech.crux.model.Datastore;

import org.junit.Test;

public class TestDimensionAndMeasureView {

	@Test
	public void testDimensionAndMeasureViewWithTrueValue(){
	
		RowAlias rowAlias = new RowAlias();
		boolean isNumeric = true;
		
		Datastore dataStore = new Datastore();
		
		ValueType valueType = new ValueType(1,dataStore,"Type",null,null,isNumeric);
		
		rowAlias.setAlias("row");
		rowAlias.setValueType(valueType);
		
		DimensionAndMeasureView dimensionAndMeasureView = new DimensionAndMeasureView(rowAlias);
		
		assertEquals(dimensionAndMeasureView.getAlias(),"row");
		assertEquals(dimensionAndMeasureView.getIsDimension(),"false");
		
		
	}
	
	@Test
	public void testDimensionAndMeasureViewWithFalseValue(){
		
		RowAlias rowAlias = new RowAlias();
		boolean isNumeric = false;
		
		Datastore dataStore = new Datastore();
		
		ValueType valueType = new ValueType(1,dataStore,"Type",null,null, isNumeric);
		
		
		rowAlias.setAlias("row1");
		rowAlias.setValueType(valueType);
		
		DimensionAndMeasureView dimensionAndMeasureView = new DimensionAndMeasureView(rowAlias);
		
		assertEquals(dimensionAndMeasureView.getAlias(),"row1");
		assertEquals(dimensionAndMeasureView.getIsDimension(),"true");
		
	}
}
