package co.nubetech.crux.model;

import java.io.Serializable;

public class FunctionTypeMapping implements Serializable{
	
	private long id;
	private Function function;	
	private ValueType valueType;
	private ValueType returnValueType;	
	
	public FunctionTypeMapping() {
	}

	public FunctionTypeMapping(Function function,
			ValueType valueType, ValueType returnValueType) {
		this.function = function;
		this.valueType = valueType;
		this.returnValueType = returnValueType;
	}

	public long getId() {
		return id;
	}

	public Function getFunction() {
		return function;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public ValueType getReturnValueType() {
		return returnValueType;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public void setReturnValueType(ValueType returnValueType) {
		this.returnValueType = returnValueType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
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
		if (!(obj instanceof FunctionTypeMapping)) {
			return false;
		}
		FunctionTypeMapping other = (FunctionTypeMapping) obj;
		if (function == null) {
			if (other.function != null) {
				return false;
			}
		} else if (!function.equals(other.function)) {
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
		return "FunctionTypeMapping [id=" + id + ", function=" + function
				+ ", valueType=" + valueType + ", returnValueType="
				+ returnValueType + "]";
	}

	

}
