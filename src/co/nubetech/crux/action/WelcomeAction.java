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
package co.nubetech.crux.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import co.nubetech.crux.dao.ConnectionDAO;
import co.nubetech.crux.dao.MappingDAO;
import co.nubetech.crux.dao.ReportDAO;
import co.nubetech.crux.model.Report;

public class WelcomeAction extends CruxAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger
			.getLogger(co.nubetech.crux.action.WelcomeAction.class);

	private ConnectionDAO connectionDAO = new ConnectionDAO();
	private MappingDAO mappingDAO = new MappingDAO();
	private ReportDAO reportDAO = new ReportDAO();

	public String welcome() {
		int connectionListSize = connectionDAO.findAll().size();
		int mappingListSize = mappingDAO.findAll().size();

		if (mappingListSize > 0) {
			boolean ifExists = false;
			ArrayList<Report> reportList = new ArrayList<Report>(
					reportDAO.findAll());
			for (Report report : reportList) {
				if (report.getDashboard() != null) {
					ifExists = true;
				}
			}
			if (reportList.size() > 0) {
				if (ifExists) {
					return "dashboard";
				} else {
					return "report";
				}
			} else {
				return "design";
			}
		} else if (connectionListSize > 0 && mappingListSize == 0) {
			return "mapping";
		} else {
			return "connection";
		}
	}

}
