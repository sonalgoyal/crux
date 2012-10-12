package co.nubetech.crux.model;

public class Function {

	private long id;
	private String functionName;
	private String functionClass;
	private boolean aggregate;
	private ValueType valueType;
	private ValueType returnValueType;	

	public Function() {
	}
		
	public Function(long id, String functionName, String functionClass,
			boolean aggregate, ValueType valueType, ValueType returnValueType) {
		super();
		this.id = id;
		this.functionName = functionName;
		this.functionClass = functionClass;
		this.aggregate = aggregate;
		this.valueType = valueType;
		this.returnValueType = returnValueType;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionClass() {
		return functionClass;
	}

	public void setFunctionClass(String functionClass) {
		this.functionClass = functionClass;
	}
	
	public ValueType getValueType() {
		return valueType;
	}

	public ValueType getReturnValueType() {
		return returnValueType;
	}


	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public void setReturnValueType(ValueType returnValueType) {
		this.returnValueType = returnValueType;
	}

	/**
	 * @return the aggregate
	 */
	public boolean isAggregate() {
		return aggregate;
	}

	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aggregate ? 1231 : 1237);
		result = prime * result
				+ ((functionClass == null) ? 0 : functionClass.hashCode());
		result = prime * result
				+ ((functionName == null) ? 0 : functionName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((returnValueType == null) ? 0 : returnValueType.hashCode());
		result = prime * result
				+ ((valueType == null) ? 0 : valueType.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Function)) {
			return false;
		}
		Function other = (Function) obj;
		if (aggregate != other.aggregate) {
			return false;
		}
		if (functionClass == null) {
			if (other.functionClass != null) {
				return false;
			}
		} else if (!functionClass.equals(other.functionClass)) {
			return false;
		}
		if (functionName == null) {
			if (other.functionName != null) {
				return false;
			}
		} else if (!functionName.equals(other.functionName)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (returnValueType == null) {
			if (other.returnValueType != null) {
				return false;
			}
		} else if (!returnValueType.equals(other.returnValueType)) {
			return false;
		}
		if (valueType == null) {
			if (other.valueType != null) {
				return false;
			}
		} else if (!valueType.equals(other.valueType)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Function [id=" + id + ", functionName=" + functionName
				+ ", functionClass=" + functionClass + ", aggregate="
				+ aggregate + ", valueType=" + valueType + ", returnValueType="
				+ returnValueType + "]";
	}





	
}
