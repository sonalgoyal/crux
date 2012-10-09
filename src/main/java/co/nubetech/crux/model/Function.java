package co.nubetech.crux.model;

public class Function {

	private long id;
	private String functionName;
	private String functionClass;
	private boolean aggregate;

	public Function() {
	}

	public Function(String functionName, String functionClass,
			boolean aggregate) {
		super();
		this.functionName = functionName;
		this.functionClass = functionClass;
		this.aggregate = aggregate;
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

	
	@Override
	public String toString() {
		return "Functions [id=" + id + ", functionName=" + functionName
				+ ", functionClass=" + functionClass + ", aggregate="
				+ aggregate + "]";
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
		return true;
	}
}
