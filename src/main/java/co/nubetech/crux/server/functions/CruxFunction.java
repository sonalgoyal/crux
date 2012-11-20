package co.nubetech.crux.server.functions;

import co.nubetech.crux.model.ValueType;


/**
 * This interface can handle both aggregate and non aggregate functions
 * Got non aggregate functions like ceil, round etc, call to execute is enough. 
 * Each function  
 * @author sgoyal
 *
 */
public interface CruxFunction {	
	
	public ValueType getValueType();
	public void setValueType(ValueType valueType);	
	public Object getFromBytes(byte[] b);
	public Object getPromotedType(Object o);
	public boolean isAggregate();

}
