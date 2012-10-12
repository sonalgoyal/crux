package co.nubetech.crux.view;

import co.nubetech.crux.model.Function;
import co.nubetech.crux.model.FunctionTypeMapping;

public class FunctionView {

	private String functionName;
	private String valueType;
	private String returnType;
	
	public FunctionView(Function function) {
		super();
		this.functionName = function.getFunctionName();
		this.valueType = function.getValueType().getName();
		this.returnType = function.getReturnValueType().getName();
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	@Override
	public String toString() {
		return "FunctionView [functionName=" + functionName + ", valueType="
				+ valueType + ", returnType=" + returnType + "]";
	}
}
