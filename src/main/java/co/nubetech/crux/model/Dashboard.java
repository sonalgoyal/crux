package co.nubetech.crux.model;

import java.io.Serializable;

public class Dashboard implements Serializable{

	private long id;
	private int index;
	private int column;

	public Dashboard() {
		this.index = 0;
		this.column = 0;
	}

	public Dashboard(int index, int column) {
		super();
		this.index = index;
		this.column = column;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + index;
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
		Dashboard other = (Dashboard) obj;
		if (column != other.column)
			return false;
		if (id != other.id)
			return false;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dashboard [id=" + id + ", index=" + index + ", column="
				+ column + "]";
	}
}
