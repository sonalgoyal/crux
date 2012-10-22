package co.nubetech.crux.server;

import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;

import co.nubetech.crux.model.Alias;
import co.nubetech.crux.model.ColumnAlias;
import co.nubetech.crux.model.Report;
import co.nubetech.crux.model.ReportDesign;
import co.nubetech.crux.model.RowAlias;

public class ServerUtil {
	
	/**
	 * This is not the most optimized piece of code
	 * we should go to the byte buffers
	 * lets clean this later once the functionality works end to end
	 * @param results
	 * @param alias
	 * @return
	 */
	public static byte[] getValue(List<KeyValue> results, Alias alias, Report report) {
		byte[] value = null;
		if (alias instanceof RowAlias) {
			value = results.get(0).getKey();
		}
		else {
			ColumnAlias colAlias = (ColumnAlias) alias;
			String family = colAlias.getColumnFamily();
			String qualifier = colAlias.getQualifier();
			byte[] familyBytes = family.getBytes();
			byte[] qualifierBytes = qualifier.getBytes();
			for (KeyValue kv: results) {
				if (kv.getFamily().equals(familyBytes)) {
					if (kv.getQualifier().equals(qualifierBytes)) {
						value = kv.getValue();
						break;
					}
				}
			}
		}
		return value;
	}
	
	public static byte[] getValue(Result result, Alias alias, Report report) {
		byte[] value = null;
		if (alias instanceof RowAlias) {
			value = result.getRow();
		}
		else {
			ColumnAlias colAlias = (ColumnAlias) alias;
			String family = colAlias.getColumnFamily();
			String qualifier = colAlias.getQualifier();
			value = result.getColumnLatest(family.getBytes(), qualifier.getBytes()).getValue();
		}
		return value;
	}
	
	public static Alias getAlias(ReportDesign design) {
		Alias alias = design.getRowAlias();
		 if (alias == null) {
			 alias = design.getColumnAlias(); 
		 }
		 return alias;
	}	
	


}
