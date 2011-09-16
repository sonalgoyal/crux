package co.nubetech.crux.view;

import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.RowAlias;

public class DimensionAndMeasureView {

	private String alias;
	private String valueType;
	private String isDimension;
	
	public DimensionAndMeasureView(){
		
		}
	
	public DimensionAndMeasureView(RowAlias rowAlias) {
		super();
		this.alias = rowAlias.getAlias();
		this.valueType = rowAlias.getValueType().getName();
		this.isDimension = !rowAlias.getValueType().isNumeric() +"";
	}
	
	public DimensionAndMeasureView(ColumnAlias columnAlias) {
		super();
		this.alias = columnAlias.getAlias();
		this.valueType = columnAlias.getValueType().getName();
		this.isDimension = !columnAlias.getValueType().isNumeric()+"";
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	
	public String getIsDimension() {
		return isDimension;
	}

	public void setIsDimension(String isDimension) {
		this.isDimension = isDimension;
	}

	@Override
	public String toString() {
		return "DimensionAndMeasureView [alias=" + alias + ", valueType="
				+ valueType + ", isDimension=" + isDimension + "]";
	}
	
}
