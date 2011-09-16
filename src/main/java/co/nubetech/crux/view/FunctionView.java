package co.nubetech.crux.view;

import co.nubetech.crux.model.FunctionTypeMapping;

public class FunctionView {

	private String functionName;
	private String valueType;
	private String returnType;
	
	public FunctionView(FunctionTypeMapping functionMapping) {
		super();
		this.functionName = functionMapping.getFunction().getFunctionName();
		this.valueType = functionMapping.getValueType().getName();
		this.returnType = functionMapping.getReturnValueType().getName();
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
