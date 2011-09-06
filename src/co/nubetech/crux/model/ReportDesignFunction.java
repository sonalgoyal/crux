package co.nubetech.crux.model;

public class ReportDesignFunction {
	
	private long id;
	private ReportDesign reportDesign;
	private Function function;	
	
	public ReportDesignFunction() {
	}

	public ReportDesignFunction(ReportDesign reportDesign, Function function) {
		this.reportDesign = reportDesign;
		this.function = function;
	}

	public long getId() {
		return id;
	}

	public ReportDesign getReportDesign() {
		return reportDesign;
	}

	public Function getFunction() {
		return function;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setReportDesign(ReportDesign reportDesign) {
		this.reportDesign = reportDesign;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return "ReportDesignFunction [id=" + id + ", reportDesign="
				+ reportDesign.getId() + ", functions=" + function + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((reportDesign == null) ? 0 : new Long(reportDesign.getId()).hashCode());
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
		ReportDesignFunction other = (ReportDesignFunction) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (id != other.id)
			return false;
		if (reportDesign == null) {
			if (other.reportDesign != null)
				return false;
		} else if (!(reportDesign.getId()==other.reportDesign.getId()))
			return false;
		return true;
	}
}
