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

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import co.nubetech.crux.pool.HBaseConnectionPool;
import co.nubetech.crux.server.HBaseFacade;
import co.nubetech.crux.util.CruxConstants;
import co.nubetech.crux.util.CruxError;

import com.opensymphony.xwork2.ActionSupport;

public class CruxAction extends ActionSupport {

	protected static final long serialVersionUID = 1L;

	public CruxError error = new CruxError();

	public CruxError getError() {
		return error;
	}

	public void setError(CruxError error) {
		this.error = error;
	}

	public HBaseFacade getHBaseFacade() {
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		HBaseConnectionPool pool = (HBaseConnectionPool) servletContext
				.getAttribute(CruxConstants.HBASE_POOL);
		HBaseFacade hbaseFacade = new HBaseFacade(pool);
		return hbaseFacade;
	}
}
