package co.nubetech.crux.model;

import java.util.List;

public class GroupBys {
	
	private long id;
	
	private List<GroupBy> groupBy;
	
	public GroupBys() {		
	}

	public GroupBys(long id, List<GroupBy> groupBy) {
		super();
		this.id = id;
		this.groupBy = groupBy;
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
	 * @return the groupBy
	 */
	public List<GroupBy> getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy the groupBy to set
	 */
	public void setGroupBy(List<GroupBy> groupBy) {
		this.groupBy = groupBy;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupBy == null) ? 0 : groupBy.hashCode());
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
		if (!(obj instanceof GroupBys)) {
			return false;
		}
		GroupBys other = (GroupBys) obj;
		if (groupBy == null) {
			if (other.groupBy != null) {
				return false;
			}
		} else if (!groupBy.equals(other.groupBy)) {
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
		return "GroupBys [id=" + id + ", groupBy=" + groupBy + "]";
	}	

}
