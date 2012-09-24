package co.nubetech.crux.model;

public class GroupBy {
	private long id;
	private RowAlias alias;
	
	
	public GroupBy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupBy(long id, RowAlias alias) {
		this.id = id;
		this.alias = alias;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the alias
	 */
	public RowAlias getRowAlias() {
		return alias;
	}
	/**
	 * @param alias the alias to set
	 */
	public void setRowAlias(RowAlias alias) {
		this.alias = alias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
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
		if (!(obj instanceof GroupBy)) {
			return false;
		}
		GroupBy other = (GroupBy) obj;
		if (alias == null) {
			if (other.alias != null) {
				return false;
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GroupBy [id=" + id + ", alias=" + alias + "]";
	}
	
}
