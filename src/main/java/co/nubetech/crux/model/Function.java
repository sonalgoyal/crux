package co.nubetech.crux.model;

public class Function {

	private long id;
	private String functionName;
	private String functionClass;
	private short functionType;

	public Function() {
	}

	public Function(String functionName, String functionClass,
			short functionType) {
		super();
		this.functionName = functionName;
		this.functionClass = functionClass;
		this.functionType = functionType;
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

	public short getFunctionType() {
		return functionType;
	}

	public void setFunctionType(short functionType) {
		this.functionType = functionType;
	}

	public boolean isAggregateType(){
		boolean result = false;
		if(this.functionType == (short)2){
			result = true;
		}
		return result;
	}
	
	public boolean isRowType(){
		boolean result = false;
		if(this.functionType == (short)1){
			result = true;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Functions [id=" + id + ", functionName=" + functionName
				+ ", functionClass=" + functionClass + ", functionType="
				+ functionType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((functionClass == null) ? 0 : functionClass.hashCode());
		result = prime * result
				+ ((functionName == null) ? 0 : functionName.hashCode());
		result = prime * result + functionType;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Function other = (Function) obj;
		if (functionClass == null) {
			if (other.functionClass != null)
				return false;
		} else if (!functionClass.equals(other.functionClass))
			return false;
		if (functionName == null) {
			if (other.functionName != null)
				return false;
		} else if (!functionName.equals(other.functionName))
			return false;
		if (functionType != other.functionType)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
