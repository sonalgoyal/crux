package co.nubetech.crux.server.functions;


/**
 * This interface can handle both aggregate and non aggregate functions
 * Got non aggregate functions like ceil, round etc, call to execute is enough. 
 * Each function  
 * @author sgoyal
 *
 */
public interface CruxFunction {	
	
	public void setValueType(String valueType);
	
	public String getValueType();
	
	public void setReturnValueType(String returnValueType);
	
	public String getReturnValueType();
	
	public boolean isAggregate();

}
