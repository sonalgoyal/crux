package co.nubetech.crux.server.filter;

import java.util.List;

import co.nubetech.crux.model.RowAliasFilter;

public class RangeFilters {
	
	private List<RowAliasFilter> lesserRowFilters;
	private List<RowAliasFilter> greaterRowFilters;
	
	public RangeFilters() {
		
	}
	
	public RangeFilters(List<RowAliasFilter> lesserRowFilters, 
			List<RowAliasFilter> greaterRowFilters) {
		this.lesserRowFilters = lesserRowFilters;
		this.greaterRowFilters = greaterRowFilters;
	}

	public List<RowAliasFilter> getLesserRowFilters() {
		return lesserRowFilters;
	}

	public void setLesserRowFilters(List<RowAliasFilter> lesserRowFilters) {
		this.lesserRowFilters = lesserRowFilters;
	}

	public List<RowAliasFilter> getGreaterRowFilters() {
		return greaterRowFilters;
	}

	public void setGreaterRowFilters(List<RowAliasFilter> greaterRowFilter) {
		this.greaterRowFilters = greaterRowFilter;
	}
	
	/**
	 * Need to find out if a filter is part of the range scan
	 * @param filter
	 * @return
	 */
	
	public boolean contains(RowAliasFilter filter) {
		boolean isContained = false;
		if (greaterRowFilters != null) {
			isContained = greaterRowFilters.contains(filter);
		}
		if (!isContained) {
			if (lesserRowFilters != null) {
				isContained = lesserRowFilters.contains(filter);
			}	
		}
		return isContained;
	}

}
