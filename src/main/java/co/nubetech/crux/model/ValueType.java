/**
 * Copyright 2011 Nube Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package co.nubetech.crux.model;

import org.apache.hadoop.hbase.util.Bytes;

public class ValueType {

	private long id;
	private Datastore datastore;
	private String name;
	private String className;
	private String promotedValueClassName;
	private boolean numeric;

	public ValueType() {

	}
	
	/*
	 * By default, set promoted value type as classname
	 */
	public ValueType(long id, Datastore datastore, String name,
			String className, boolean isNumeric) {
		super();
		this.id = id;
		this.datastore = datastore;
		this.name = name;
		this.className = className;
		this.promotedValueClassName = className;
		this.numeric = isNumeric;
	}

	public ValueType(long id, Datastore datastore, String name,
			String className, String promotedValueClassName, boolean isNumeric) {
		super();
		this.id = id;
		this.datastore = datastore;
		this.name = name;
		this.className = className;
		this.promotedValueClassName = promotedValueClassName;
		this.numeric = isNumeric;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Datastore getDatastore() {
		return datastore;
	}

	public void setDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isNumeric() {
		return numeric;
	}

	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}

	/**
	 * @return the promotedValueClassName
	 */
	public String getPromotedValueClassName() {
		return promotedValueClassName;
	}

	/**
	 * @param promotedValueClassName the promotedValueClassName to set
	 */
	public void setPromotedValueClassName(String promotedValueClassName) {
		this.promotedValueClassName = promotedValueClassName;
	}

	@Override
	public String toString() {
		return "ValueType [id=" + id + ", datastore=" + datastore + ", name="
				+ name + ", className=" + className + ",promotedValueClassName=" 
				+ promotedValueClassName + ", isNumeric=" + numeric
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((datastore == null) ? 0 : datastore.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (numeric ? 1231 : 1237);
		result = prime
				* result
				+ ((promotedValueClassName == null) ? 0
						: promotedValueClassName.hashCode());
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
		if (!(obj instanceof ValueType)) {
			return false;
		}
		ValueType other = (ValueType) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (datastore == null) {
			if (other.datastore != null) {
				return false;
			}
		} else if (!datastore.equals(other.datastore)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (numeric != other.numeric) {
			return false;
		}
		if (promotedValueClassName == null) {
			if (other.promotedValueClassName != null) {
				return false;
			}
		} else if (!promotedValueClassName.equals(other.promotedValueClassName)) {
			return false;
		}
		return true;
	}
	
	public Object fromBytes(byte[] b) {
		if (b != null) {
			if (className.equals("java.lang.String")) {
				return Bytes.toString(b);
			}
			else if (className.equals("java.lang.Integer")) {
				return Bytes.toInt(b);
			}
			else if (className.equals("java.lang.Double")){
				return Bytes.toDouble(b);
			}
			else if (className.equals("java.lang.Long")){
				return Bytes.toLong(b);
			}
			else if (className.equals("java.lang.Float")){
				return Bytes.toFloat(b);
			}
			else if (className.equals("java.lang.Short")){
				return Bytes.toShort(b);
			}
			else if (className.equals("java.lang.Boolean")){
				return Bytes.toBoolean(b);
			}
		}
		return null;
	}

		
	
	

}
